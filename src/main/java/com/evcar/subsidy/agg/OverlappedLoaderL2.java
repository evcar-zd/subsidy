package com.evcar.subsidy.agg;

import com.evcar.subsidy.GitVer;
import com.evcar.subsidy.entity.HisCountData;
import com.evcar.subsidy.entity.HisCountDataL2;
import com.evcar.subsidy.service.HisCountDataService;
import com.evcar.subsidy.util.ESTools;
import com.evcar.subsidy.util.ZonedDateTimeUtil;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * 层叠取数据并保存
 */
public class OverlappedLoaderL2 {

    private Logger s_logger = LoggerFactory.getLogger(OverlappedLoaderL2.class);

    private static Integer THREAD_SIZE = 30;
    private static Integer BLOCKINGQUEUE_SIZE = 100 ;

    private Map<String, OverlappedDateL2> _cache = new HashMap<>();
    private BlockingQueue<TaskFetchL2AndSave> _blockingVin = new ArrayBlockingQueue<>(BLOCKINGQUEUE_SIZE);
    private AtomicBoolean _isRunning = new AtomicBoolean(false);
    private List<Thread> listThreads = new ArrayList<>();


    public void start(){
        for (int i = 0 ; i < THREAD_SIZE ; i++){
            Thread worker = new Thread(()->{
                Client client = ESTools.getClient();
                while(true) {
                    try{
                        TaskFetchL2AndSave taskFetchL2AndSave = _blockingVin.take();
                        if(taskFetchL2AndSave.shouldStop()){
                            long threadId = Thread.currentThread().getId() ;
                            synchronized (listThreads){
                                int x = 0 ;
                                while(x < listThreads.size()){
                                    Thread thread = listThreads.get(x) ;
                                    if (thread.getId() == threadId){
                                        listThreads.remove(x) ;
                                        break;
                                    }
                                    x ++ ;
                                }
                            }
                            return;
                        }

                        if(taskFetchL2AndSave.bFetch)
                            cacheData(taskFetchL2AndSave.vinCode, taskFetchL2AndSave.start, taskFetchL2AndSave.end,taskFetchL2AndSave.monthDay, client);
                        else
                            save(taskFetchL2AndSave.hisCountDataL2, client);
                    }catch (Exception e){
                        s_logger.error(e.toString());
                    }

                }
            });
            worker.start();
            listThreads.add(worker);
        }
    }


    public void stop(){
        for(int i=0;i<THREAD_SIZE;i++){
            _blockingVin.add(new TaskFetchL2AndSave());
        }
        listThreads.clear();
        _isRunning.set(false);
        int max = 1000 ;
        int retry = 0 ;
        while (true){
            try {
                retry ++ ;
                if (retry > max){
                    throw new RuntimeException("出事情了！") ;
                }
                Thread.sleep(100);
                synchronized (listThreads){
                    if (listThreads.size()==0)return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void preFetch(String vinCode, Date startDate, Date endDate,Integer monthDay){
        TaskFetchL2AndSave task = new TaskFetchL2AndSave();
        task.vinCode = vinCode;
        task.start = startDate;
        task.end = endDate;
        task.monthDay = monthDay ;
        _blockingVin.add(task);

        if(_isRunning.compareAndSet(false, true)){
            s_logger.info("start prefetch...");
            start();
        }
    }

    public void asyncSave(HisCountDataL2 hisCountDataL2){
        TaskFetchL2AndSave taskSave = new TaskFetchL2AndSave();
        taskSave.bFetch = false ;
        taskSave.hisCountDataL2 = hisCountDataL2 ;

        _blockingVin.add(taskSave);
    }

    public OverlappedDateL2 load(String vinCode, Date startDate,Date endDate,Integer monthDay){
        final int maxRetry = 1000; // 最多试这么多次了
        int retry = 0;
        // 从cache中找数据,如果没找到就等待
        int tryNumber = 0 ;
        while(true) {
            retry++;
            if(retry > maxRetry){
                if (tryNumber == 2) return new OverlappedDateL2(false);

                cacheData(vinCode, startDate, endDate,monthDay, null);
                tryNumber ++ ;
                s_logger.info("retry {} number & vinCode {}",tryNumber,vinCode);
                retry = 0 ;
            }
            synchronized (_cache) {
                if (_cache.containsKey(vinCode)) {
                    OverlappedDateL2 data = _cache.get(vinCode);
                    _cache.remove(vinCode);
                    return data;
                }
            }
            try {
                // 等100毫秒再试
                Thread.sleep(100);
                // s_logger.warn("load {} failed, retry {}...", vinCode, retry);
            }catch(Exception ex){
                s_logger.error(ex.toString());
            }
        }
    }

    public void cacheData(String vinCode, Date startDate, Date endDate, int monthDay , Client client){
        if (client == null ) client = ESTools.getClient() ;
        OverlappedDateL2 data = new OverlappedDateL2() ;
        /** 获取 startDate 前 monthDay的数据*/
        Date startDate2 = ZonedDateTimeUtil.getDate(startDate,monthDay) ;
        Date start = ZonedDateTimeUtil.getStartDate(startDate,1) ;
        Date end = ZonedDateTimeUtil.getStartDate(startDate2,1) ;

        data.hisCountDatasL1 = HisCountDataService.getHisCountDataL1(start,startDate2,vinCode,client) ;
        data.hisCountDataL2s = HisCountDataService.getHisCountDataL2(vinCode,end,client) ;
        /** 获取L1前monthDay的数据 */
        data.hisCountDatas = HisCountDataService.getHisCountData(vinCode,startDate,endDate,client) ;

        data.canhisCount = HisCountDataService.getCanOrGps(vinCode,0,client);
        data.gpshisCount = HisCountDataService.getCanOrGps(vinCode,1,client);

        synchronized (_cache){
            _cache.put(vinCode, data);
        }
    }

    public void save(HisCountDataL2 hisCountData, Client client){
        GitVer gitVer = new GitVer() ;
        hisCountData.setVersion(gitVer.getVersion());
        HisCountDataService.addHisCountDataL2(hisCountData,client) ;
    }
}

class TaskFetchL2AndSave{
    public boolean bFetch;
    public String vinCode;
    public Date start;
    public Date end;
    public Integer monthDay ;

    public HisCountDataL2 hisCountDataL2 ;

    public TaskFetchL2AndSave(){
        bFetch = true;
    }

    public boolean shouldStop(){
        return vinCode == null && hisCountDataL2 == null ;
    }

}

class OverlappedDateL2{

    protected List<HisCountData> hisCountDatas ;
    protected List<HisCountData> hisCountDatasL1 ;
    protected List<HisCountDataL2> hisCountDataL2s ;

    protected Long canhisCount ;
    protected Long gpshisCount ;

    public OverlappedDateL2(){

    }

    public OverlappedDateL2(Boolean endResult){
        if (!endResult){
            hisCountDatas = new ArrayList<>();
            hisCountDatasL1 = new ArrayList<>() ;
            hisCountDataL2s = new ArrayList<>() ;
            canhisCount = 0L ;
            gpshisCount = 0L ;
        }
    }

}