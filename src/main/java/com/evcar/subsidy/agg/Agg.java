package com.evcar.subsidy.agg;

import com.evcar.subsidy.entity.ExportTarget;
import com.evcar.subsidy.entity.Vehicle;
import com.evcar.subsidy.entity.VehicleVo;
import com.evcar.subsidy.service.VehicleService;
import com.evcar.subsidy.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.evcar.subsidy.service.VehicleService.getVehicleNum;

/**
 * Created by Kong on 2017/5/11.
 */
@Component
public class Agg {

    private static Logger s_logger = LoggerFactory.getLogger(Agg.class);

    /**
     * 执行计算指标数据L1~L3
     * @param startDay  前N天开始
     * @param endDay    前N天结束
     * @param monthDay  计算monthDay数据
     * @param vinCodes
     */
    public void takeTarget(int startDay ,int endDay ,int monthDay,List<String> vinCodes){
        int diff = startDay - endDay ;
        diff = diff == 0 ? 1 : diff ;
        if (diff > 0){
            Date date = new Date() ;
            Date startDate = DateUtil.getDate(date,startDay) ;
//            Date endDate = DateUtil.getDate(date,endDay) ;
            List<VehicleVo> vehicleVos = this.talkVehicle(vinCodes);

            /**根据日期执行*/
            for (int i = 0 ; i < diff ; i++){
                /**
                 * L1执行时间
                 * */
                Date start = DateUtil.getStartDate(startDate,i) ;
                Date end = DateUtil.getEndDate(startDate,i) ;

                /** L2/L3执行时间 */
//                Date month = DateUtil.getDate(start,monthDay-1) ;

                int prgL1 = 0;
                if(vehicleVos.size() > 0 ) {
                    try {
                        long startTimeL1 = System.currentTimeMillis() ;
                        s_logger.info("start L1 {} count", start);
                        final int fetchSize = 5;
                        for (int j=0;j<vehicleVos.size();j++){
                            VehicleVo vehicleVo = vehicleVos.get(i);
                            VehicleL1 vehicleL1 = new VehicleL1() ;
                            // pre fetch
                            if(j == 0) {
                                for (int k = j; k < j + fetchSize && k < vehicleVos.size(); k++) {
                                    VehicleVo vvoFetch = vehicleVos.get(k);
                                    vehicleL1.preFetch(vvoFetch.getVinCode(), start, end);
                                }
                            }
                            else{
                                if((j + fetchSize - 1) < vehicleVos.size()) {
                                    VehicleVo vvoFetch = vehicleVos.get(j + fetchSize - 1);
                                    vehicleL1.preFetch(vvoFetch.getVinCode(), start, end);
                                }
                            }

                            vehicleL1.calc(vehicleVo,start,end);
                            prgL1++;
                            if(prgL1 % 100 == 0)
                                s_logger.info("L1 progress: {} / {}", prgL1, vehicleVos.size());
                        }
                        s_logger.info("end L1 cost {}", (System.currentTimeMillis()-startTimeL1));
                        Thread.sleep(200);
                        long startTimeL2 = System.currentTimeMillis() ;
                        s_logger.info("start L2 {} count", start);
                        for (VehicleVo vehicleVo : vehicleVos){
                            /** L2计算 */
                            VehicleL2 vehicleL2 = new VehicleL2() ;
                            vehicleL2.calc(vehicleVo,start ,end ,monthDay);
                        }
                        s_logger.info("end L2 cost {}", (System.currentTimeMillis()-startTimeL2));
                        Thread.sleep(2000);
                        VehicleL3 vehicleL3 = new VehicleL3() ;
                        vehicleL3.calcL3(start ,end );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 多线程计算L1
     * @param vehicleVos
     * @param start
     * @param end
     */
    private void threadL1(List<VehicleVo> vehicleVos , Date start, Date end){
        System.out.println(start+"L1开始计算");
        int groupNum = vehicleVos.size()/THREAD_SIZE ;
        groupNum = vehicleVos.size()%THREAD_SIZE > 0 ? groupNum+1 : groupNum ;
        for(int i = 0 ; i < groupNum ; i++ ){
            ExecutorService executor = Executors.newScheduledThreadPool(THREAD_SIZE+10) ;
            try {
                for (int j = 0; j < THREAD_SIZE; j++) {
                    int y = i*THREAD_SIZE + j ;
                    executor.execute(() -> {
                        if (y < vehicleVos.size()){
                            VehicleVo vehicleVo = vehicleVos.get(y) ;
                            VehicleL1 vehicleL1 = new VehicleL1() ;
                            vehicleL1.calc(vehicleVo,start,end);
                        }
                    }) ;
                }
                executor.shutdown();
                while (true) {
                    if (executor.isTerminated()) {
                        executor.shutdownNow() ;
                        break;
                    }
                    Thread.sleep(200);
                }
            }catch (Exception e){
                executor.shutdownNow() ;
            }
        }
        System.out.println(start+"L1计算结束");
    }


    /**
     * 多线程计算L2
     * @param vehicleVos
     * @param start
     * @param end
     * @param monthDay
     */
    private void threadL2(List<VehicleVo> vehicleVos ,Date start ,Date end ,int monthDay){
        System.out.println(start+"L2开始计算");
        if(vehicleVos.size() > 0 ) {
            int groupNum = vehicleVos.size()/THREAD_SIZE ;
            groupNum = vehicleVos.size()%THREAD_SIZE > 0 ? groupNum+1 : groupNum ;
            for(int i = 0 ; i < groupNum ; i++ ){
                ExecutorService executor = Executors.newScheduledThreadPool(THREAD_SIZE+20) ;
                try {
                    for (int j = 0; j < THREAD_SIZE; j++) {
                        int y = i*THREAD_SIZE+j ;
                        executor.execute(() -> {
                            if (y < vehicleVos.size()){
                                VehicleVo vehicleVo = vehicleVos.get(y) ;
                                VehicleL2 vehicleL2 = new VehicleL2() ;
                                vehicleL2.calc(vehicleVo,start ,end ,monthDay);
                            }
                        }) ;
                    }
                    executor.shutdown();
                    while (true) {
                        if (executor.isTerminated()) {
                            executor.shutdownNow() ;
                            break;
                        }
                        Thread.sleep(200);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    executor.shutdownNow() ;
                }
            }
        }
        System.out.println(start+"L2计算结束");
    }


    /**
     * 每MAX_SIZE分为一组，避免数据量大出现内存不足情况
     * 后期可以使用多线程去处理（快速）
     */
    public static final Integer MAX_SIZE = 500 ;


    public static final Integer THREAD_SIZE = 10 ;

    /**
     * 计算L1入口
     * 从startDate至endDate每一天的数据
     * 实现每车每天一条数据
     * @param startDay      当前日期的前startDay天
     * @param endDay        当前日期的前endDay天
     * @param vinCodes      车辆信息
     */
    public void takeAgg(int startDay, int endDay, List<String> vinCodes){
        int diff = startDay - endDay ;
        diff = diff == 0 ? 1 : diff ;
        if (diff > 0){
            Date date = new Date() ;
            Date startDate = DateUtil.getDate(date,startDay) ;
            Date endDate = DateUtil.getDate(date,endDay) ;

            List<VehicleVo> vehicleVos = this.talkVehicle(vinCodes);
            if(vehicleVos.size() > 0 ) {

                Date start = DateUtil.getStartDate(startDate) ;
                Date end = DateUtil.getEndDate(endDate) ;

                int groupNum = vehicleVos.size()/THREAD_SIZE ;
                groupNum = vehicleVos.size()%THREAD_SIZE > 0 ? groupNum+1 : groupNum ;
                for(int i = 0 ; i < groupNum ; i++ ){
//                    long startTime = System.currentTimeMillis() ;
                    ExecutorService executor = Executors.newScheduledThreadPool(THREAD_SIZE+10) ;
                    try {
                        for (int j = 0; j < THREAD_SIZE; j++) {
                            int y = i*THREAD_SIZE + j ;
                            executor.execute(() -> {
                                if (y < vehicleVos.size()){
                                    VehicleVo vehicleVo = vehicleVos.get(y) ;
                                    VehicleL1 vehicleL1 = new VehicleL1() ;
                                    vehicleL1.calc(vehicleVo,start,end);
                                }
                            }) ;
                        }
                        executor.shutdown();
                        while (true) {
                            if (executor.isTerminated()) {
                                executor.shutdownNow() ;
//                                System.out.println(i+"ExecutorTime"+(System.currentTimeMillis()-startTime));
                                break;
                            }
                            Thread.sleep(200);
                        }
                    }catch (Exception e){
                        executor.shutdownNow() ;
                    }
                }
            }
        }

    }

    /***
     * L2计算入口
     * 取L1中startDate至endDate的数据
     * 每monthDay天分为一组数据
     * 如果startDate和endDate日期差大于monthDay，则线性移动。
     * 实现每车每天一条数据
     * @param startDay  当前日期的前startDay天
     * @param endDay    当前日期的前endDay天
     * @param monthDay  每monthDay天，当做一组数据
     * @param vinCodes  车辆信息
     */
    public void takeVehicleL2(int startDay, int endDay, int monthDay ,List<String> vinCodes){
        Date date = new Date() ;
        Date startDate = DateUtil.getDate(date,startDay) ;
        Date endDate = DateUtil.getDate(date,endDay) ;

        Date start = DateUtil.getStartDate(startDate) ;
        Date end = DateUtil.getEndDate(endDate) ;
        List<VehicleVo> vehicleVos = this.talkVehicle(vinCodes);

        if(vehicleVos.size() > 0 ) {
            int groupNum = vehicleVos.size()/THREAD_SIZE ;
            groupNum = vehicleVos.size()%THREAD_SIZE > 0 ? groupNum+1 : groupNum ;
            for(int i = 0 ; i < groupNum ; i++ ){
                ExecutorService executor = Executors.newScheduledThreadPool(THREAD_SIZE+10) ;
                try {
                    for (int j = 0; j < THREAD_SIZE; j++) {
                        int y = i*THREAD_SIZE+j ;
                        executor.execute(() -> {
                            if (y < vehicleVos.size()){
                                VehicleVo vehicleVo = vehicleVos.get(y) ;
                                VehicleL2 vehicleL2 = new VehicleL2() ;
                                vehicleL2.calc(vehicleVo,start ,end ,monthDay);
                            }
                        }) ;
                    }
                    executor.shutdown();
                    while (true) {
                        if (executor.isTerminated()) {
                            executor.shutdownNow() ;
                            break;
                        }
                        Thread.sleep(200);
                    }
                }catch (Exception e){
                    executor.shutdownNow() ;
                }
            }
        }
    }

    /**
     * 计算L3数据入口
     * 取L2中startDate至endDate的数据，
     * 每monthDay天分为一组数据
     * 如果startDate和endDate日期差大于monthDay，则线性移动。
     * 现实每组每天一条数据
     * @param startDay
     * @param endDay
     * @param monthDay
     * @param vinCodes
     */
    public void takeVehicleL3(int startDay, int endDay, int monthDay ,List<String> vinCodes){
        Date date = new Date() ;
        Date startDate = DateUtil.getDate(date,startDay) ;
        Date endDate = DateUtil.getDate(date,endDay) ;
        int diffNum = DateUtil.diffDate(startDate,endDate) ;
        diffNum = diffNum == 0 ? 1 : diffNum ;
        List<VehicleVo> vehicleVos = this.talkVehicle(vinCodes);

        try {
            for (int i = 0 ; i < diffNum ;i++){
                Date start = DateUtil.getStartDate(startDate,i) ;
                Date end = DateUtil.getEndDate(startDate,monthDay-1) ;
                /** 当不满足monthDay天数时，执行最后一次 */
                if (DateUtil.compare(end,endDate) || DateUtil.diffDate(end,endDate) == 0){
                    i = diffNum ;
                }
                VehicleL3 vehicleL3 = new VehicleL3() ;
//                vehicleL3.calc(vehicleVos,start ,end );
            }
        }catch (Exception e){
        }
    }


    /**
     * 获取需要计算车辆信息
     * @param vinCodes 如果vinCodes是null，则查询全部车辆信息
     * @return
     */
    private List<VehicleVo> talkVehicle(List<String> vinCodes){
        List<VehicleVo> vehicleVos = new ArrayList<>() ;
        if (vinCodes == null || vinCodes.size() == 0){
            /** 车辆分组--避免数据辆过大 */
            Long vehicleNum = getVehicleNum() ;
            Integer num = Integer.valueOf(String.valueOf(vehicleNum)) ;
            int groupNum = num/MAX_SIZE ;
            groupNum = num%MAX_SIZE > 0 ? groupNum+1 : groupNum ;
            Integer currentPage = 1 ;
            Integer pageSize = MAX_SIZE ;
            for (int j = 0 ; j < groupNum ; j++ ) {
                List<Vehicle> vehicleList = VehicleService.getVehicleByPage(currentPage, pageSize);
                for (Vehicle vehicle : vehicleList){
                    if (vehicle == null) continue;
                    VehicleVo vehicleVo = new VehicleVo(vehicle.getVinCode(),vehicle.getCarType() ,vehicle.getVeDeliveredDate(),vehicle.getReleaseTime()) ;
                    vehicleVos.add(vehicleVo) ;
                }
                currentPage ++ ;
            }
        }else{
            for (String vinCode : vinCodes){
                Vehicle vehicle = VehicleService.getVehicle(vinCode) ;
                if (vehicle == null) continue;
                VehicleVo vehicleVo = new VehicleVo(vehicle.getVinCode(),vehicle.getCarType() ,vehicle.getVeDeliveredDate(),vehicle.getReleaseTime()) ;
                vehicleVos.add(vehicleVo) ;
            }
        }
        return vehicleVos ;
    }


    public List<ExportTarget> getExport(Date date){
        Date start = DateUtil.getStartDate(date) ;
        Date end = DateUtil.getEndDate(date) ;
        List<VehicleVo> vehicleVos = this.talkVehicle(null);
        for (VehicleVo vehicleVo : vehicleVos){

        }
        ExportVehicle exportVehicle = new ExportVehicle();
        return exportVehicle.getExportTarget(null,null) ;
    }

}
