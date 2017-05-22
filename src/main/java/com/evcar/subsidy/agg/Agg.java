package com.evcar.subsidy.agg;

import com.evcar.subsidy.entity.ExportTarget;
import com.evcar.subsidy.entity.Vehicle;
import com.evcar.subsidy.entity.VehicleVo;
import com.evcar.subsidy.service.VehicleService;
import com.evcar.subsidy.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                int prgL2 = 0 ;
                if(vehicleVos.size() > 0 ) {
                    try {
                        long startTimeL1 = System.currentTimeMillis() ;
                        s_logger.info("start L1 {} count", start);
                        final int fetchSize = 30;
                        VehicleL1 vehicleL1 = new VehicleL1() ;
                        for (int j=0;j<vehicleVos.size();j++){
                            VehicleVo vehicleVo = vehicleVos.get(j);
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
                        vehicleL1.stopQueue();
                        Thread.sleep(5000);
                        long startTimeL2 = System.currentTimeMillis() ;
                        s_logger.info("start L2 {} count", start);
                        VehicleL2 vehicleL2 = new VehicleL2() ;
                        for (int j=0;j<vehicleVos.size();j++){
                            VehicleVo vehicleVo = vehicleVos.get(j);
                            // pre fetch
                            if(j == 0) {
                                for (int k = j; k < j + fetchSize && k < vehicleVos.size(); k++) {
                                    VehicleVo vvoFetch = vehicleVos.get(k);
                                    vehicleL2.preFetch(vvoFetch.getVinCode(), start, end, monthDay);
                                }
                            }
                            else{
                                if((j + fetchSize - 1) < vehicleVos.size()) {
                                    VehicleVo vvoFetch = vehicleVos.get(j + fetchSize - 1);
                                    vehicleL2.preFetch(vvoFetch.getVinCode(), start, end, monthDay);
                                }
                            }
                            /** L2计算 */
                            vehicleL2.calc(vehicleVo,start ,end ,monthDay);
                            prgL2++;
                            if(prgL2 % 100 == 0)
                                s_logger.info("L2 progress: {} / {}", prgL2, vehicleVos.size());
                        }
                        s_logger.info("end L2 cost {}", (System.currentTimeMillis()-startTimeL2));
                        vehicleL2.stopQueue() ;
                        Thread.sleep(5000);
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
     * 每MAX_SIZE分为一组，避免数据量大出现内存不足情况
     * 后期可以使用多线程去处理（快速）
     */
    public static final Integer MAX_SIZE = 500 ;

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
