package com.evcar.subsidy.agg;

import com.evcar.subsidy.GitVer;
import com.evcar.subsidy.entity.*;
import com.evcar.subsidy.service.*;
import com.evcar.subsidy.util.ESTools;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.currentThread;


/**
 * 层叠式取数据
 */
public class OverlappedLoader {

    private static int BLOCKING_QUEUE_SIZE = 100 ;
    private static int THREAD_SIZE = 30 ;

    private Map<String, OverlappedData> _cache = new HashMap<>();
    private BlockingQueue<TaskFetchAndSave> _blockingVin = new ArrayBlockingQueue<>(BLOCKING_QUEUE_SIZE);
    private AtomicBoolean _isRunning = new AtomicBoolean(false);
    private List<Thread> listThreads = new ArrayList<>();
    private Logger s_logger = LoggerFactory.getLogger(OverlappedLoader.class);

    public void start(){
        for(int i=0;i<THREAD_SIZE;i++){
            Thread worker = new Thread(()->{
                Client client = ESTools.getClient();
                while(true) {
                    try {
                        TaskFetchAndSave taskFetchSave = _blockingVin.take();
                        if(taskFetchSave.shouldStop()){
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

                        if(taskFetchSave.bFetch)
                            cacheData(taskFetchSave.vinCode, taskFetchSave.start, taskFetchSave.end, client);
                        else
                            save(taskFetchSave.tobeSaving, client);
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
            _blockingVin.add(new TaskFetchAndSave());
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

    public void preFetch(String vinCode, Date startDate, Date endDate){
        TaskFetchAndSave task = new TaskFetchAndSave();
        task.vinCode = vinCode;
        task.start = startDate;
        task.end = endDate;
        _blockingVin.add(task);

        if(_isRunning.compareAndSet(false, true)){
            s_logger.info("start prefetch...");
            start();
        }
    }

    public void asyncSave(HisCountData hisCountData){
        TaskFetchAndSave taskSave = new TaskFetchAndSave();
        taskSave.bFetch = false;
        taskSave.tobeSaving = hisCountData;

        _blockingVin.add(taskSave);
    }

    public OverlappedData load(String vinCode, Date startDate, Date endDate){
        final int maxRetry = 1000; // 最多试这么多次了
        int retry = 0;
        // 从cache中找数据,如果没找到就等待
        int tryNumber = 0 ;
        while(true) {
            retry++;
            if(retry > maxRetry){
                if (tryNumber == 2){
                    return new OverlappedData(false);
                }
                cacheData(vinCode, startDate, endDate, null) ;
                retry = 0 ;
                tryNumber ++ ;
                s_logger.info("retry {} number & vinCode {}",tryNumber,vinCode);
            }

            synchronized (_cache) {
                if (_cache.containsKey(vinCode)) {
                    OverlappedData data = _cache.get(vinCode);
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

    private void cacheData(String vinCode, Date startDate, Date endDate, Client client){
        if (client == null ) client = ESTools.getClient() ;
        // 取数据
        OverlappedData data = new OverlappedData();

        /** 查询历史整车和电机数据 */
        long sizeNum = VehicleMotorService.getHisVehicleMotorNum(vinCode,startDate,endDate, client) ;
        if (sizeNum > 0)
            data.hisVehicleMotors = VehicleMotorService.getHisVehicleMotor(vinCode,startDate,endDate,sizeNum, client) ;

        /** 查询BMS数据 */
        long bmsNum = BmsDataService.getHisBmsDataNum(vinCode,startDate,endDate, client);
        if (bmsNum > 0 )
            data.bmsDatas = BmsDataService.getHisBmsData(vinCode,startDate,endDate,bmsNum, client) ;

        /** 查询OBS数据 */
        long obcNum = ObcDataService.getHisObcDataNum(vinCode,startDate,endDate, client);
        if (bmsNum > 0 )
            data.obcDatas = ObcDataService.getHisObcData(vinCode,startDate,endDate,obcNum, client) ;

        /** 查询HVAC数据 */
        long hvacNum = HvacDataService.getHvacDataNum(vinCode,startDate,endDate, client) ;
        if (hvacNum > 0)
            data.hvacDatas = HvacDataService.getHisHvacData(vinCode,startDate,endDate,hvacNum, client) ;

        /** 获取GPS数据 */
        long gpsCount = GpsDataService.getHisGpsDataNum(vinCode,startDate,endDate, client) ;
        if (gpsCount > 0)
            data.hisGpsDatas = GpsDataService.getHisGpsData(vinCode,startDate,endDate,gpsCount, client) ;

        synchronized (_cache){
            _cache.put(vinCode, data);
        }
    }

    private void save(HisCountData data, Client client){
        GitVer gitVer = new GitVer() ;
        data.setVersion(gitVer.getVersion());
        HisCountDataService.addHisCountData(data, client);
    }
}

class TaskFetchAndSave {
    public boolean bFetch;
    public String vinCode;
    public Date start;
    public Date end;

    public HisCountData tobeSaving;

    public TaskFetchAndSave(){
        bFetch = true;
    }

    public boolean shouldStop(){
       return vinCode == null && tobeSaving == null ;
    }
}


class OverlappedData{
    public List<BmsData> bmsDatas ;

    public List<ObcData> obcDatas ;

    public List<HisVehicleMotor> hisVehicleMotors ;

    public List<HisGpsData> hisGpsDatas ;

    public List<HvacData> hvacDatas ;


    public OverlappedData(){

    }

    /** 执行2次不成功，继续往下执行 */
    public OverlappedData(Boolean endResult){
        if (!endResult){

        }
        bmsDatas = new ArrayList<>() ;
        obcDatas = new ArrayList<>() ;
        hisVehicleMotors = new ArrayList<>() ;
        hisGpsDatas = new ArrayList<>() ;
        hvacDatas = new ArrayList<>() ;

    }

}