package com.evcar.subsidy.agg;

import com.evcar.subsidy.GitVer;
import com.evcar.subsidy.entity.*;
import com.evcar.subsidy.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * 层叠式取数据
 */
public class OverlappedLoader {
    private Map<String, OverlappedData> _cache = new HashMap<>();
    private BlockingQueue<TaskFetchAndSave> _blockingVin = new ArrayBlockingQueue<>(100);
    private AtomicBoolean _isRunning = new AtomicBoolean(false);
    private List<Thread> listThreads = new ArrayList<>();
    private Logger s_logger = LoggerFactory.getLogger(OverlappedLoader.class);

    public void start()
    {
        for(int i=0;i<30;i++){
            Thread worker = new Thread(()->{
                try {
                    while(true) {
                        TaskFetchAndSave taskFetchSave = _blockingVin.take();
                        if(taskFetchSave == null) return;

                        if(taskFetchSave.bFetch)
                            cacheData(taskFetchSave.vin, taskFetchSave.start, taskFetchSave.end);
                        else
                            save(taskFetchSave.tobeSaving);

                    }
                }
                catch(Exception ex){
                    s_logger.error(ex.toString());
                }
            });
            worker.start();
            listThreads.add(worker);
        }
    }

    public void stop()
    {
        for(int i=0;i<100;i++){
            _blockingVin.add(null);
        }
    }

    public void preFetch(String vin, Date startDate, Date endDate){
        TaskFetchAndSave task = new TaskFetchAndSave();
        task.vin = vin;
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

    public OverlappedData load(String vinCode){
        final int maxRetry = 1000; // 最多试这么多次了
        int retry = 0;
        // 从cache中找数据,如果没找到就等待
            while(true) {
                retry++;
                if(retry > maxRetry) throw new RuntimeException("没取到数据!!!");

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

    private void cacheData(String vinCode, Date startDate, Date endDate){
        // 取数据
        OverlappedData data = new OverlappedData();

        /** 查询历史整车和电机数据 */
        long sizeNum = VehicleMotorService.getHisVehicleMotorNum(vinCode,startDate,endDate) ;
        if (sizeNum > 0)
            data.hisVehicleMotors = VehicleMotorService.getHisVehicleMotor(vinCode,startDate,endDate,sizeNum) ;

        /** 查询BMS数据 */
        long bmsNum = BmsDataService.getHisBmsDataNum(vinCode,startDate,endDate);
        if (bmsNum > 0 )
            data.bmsDatas = BmsDataService.getHisBmsData(vinCode,startDate,endDate,bmsNum) ;

        /** 查询OBS数据 */
        long obcNum = ObcDataService.getHisObcDataNum(vinCode,startDate,endDate);
        if (bmsNum > 0 )
            data.obcDatas = ObcDataService.getHisObcData(vinCode,startDate,endDate,obcNum) ;

        /** 查询HVAC数据 */
        long hvacNum = HvacDataService.getHvacDataNum(vinCode,startDate,endDate) ;
        if (hvacNum > 0)
            data.hvacDatas = HvacDataService.getHisHvacData(vinCode,startDate,endDate,hvacNum) ;

        /** 获取GPS数据 */
        long gpsCount = GpsDataService.getHisGpsDataNum(vinCode,startDate,endDate) ;
        if (gpsCount > 0)
            data.hisGpsDatas = GpsDataService.getHisGpsData(vinCode,startDate,endDate,gpsCount) ;

        synchronized (_cache){
            _cache.put(vinCode, data);
        }
    }

    private void save(HisCountData data){
        GitVer gitVer = new GitVer() ;
        data.setVersion(gitVer.getVersion());
        HisCountDataService.addHisCountData(data);
    }
}

class TaskFetchAndSave {
    public boolean bFetch;
    public String vin;
    public Date start;
    public Date end;

    public HisCountData tobeSaving;

    public TaskFetchAndSave(){
        bFetch = true;
    }
}


class OverlappedData{
    public List<BmsData> bmsDatas ;

    public List<ObcData> obcDatas ;

    public List<HisVehicleMotor> hisVehicleMotors ;

    public List<HisGpsData> hisGpsDatas ;

    public List<HvacData> hvacDatas ;
}