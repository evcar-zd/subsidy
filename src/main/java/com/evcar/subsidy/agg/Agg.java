package com.evcar.subsidy.agg;

import com.evcar.subsidy.entity.Vehicle;
import com.evcar.subsidy.entity.VehicleVo;
import com.evcar.subsidy.service.VehicleService;
import com.evcar.subsidy.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.jar.Pack200;

import static com.evcar.subsidy.service.VehicleService.getVehicleNum;

/**
 * Created by Kong on 2017/5/11.
 */
@Component
public class Agg {


    /**
     * 每MAX_SIZE分为一组，避免数据量大出现内存不足情况
     * 后期可以使用多线程去处理（快速）
     */
    public static final Integer MAX_SIZE = 500 ;


    /**
     * 计算L1入口
     * 从startDate至endDate每一天的数据
     * 实现每车每天一条数据
     * @param startDay      当前日期的前startDay天
     * @param endDay        当前日期的前endDay天
     * @param vinCodes      车辆信息
     */
    public void takeAgg(int startDay, int endDay, List<String> vinCodes){
        if (startDay - endDay > 0){
            Date date = new Date() ;
            Date startDate = DateUtil.getDate(date,startDay) ;
            Date endDate = DateUtil.getDate(date,endDay) ;
//            int diffNum = DateUtil.diffDate(startDate,endDate) ;

            List<VehicleVo> vehicleVos = this.talkVehicle(vinCodes);
            if(vehicleVos.size() > 0 ) {
                for (VehicleVo vehicleVo : vehicleVos){
                    Date start = DateUtil.getStartDate(startDate,0) ;
                    Date end = DateUtil.getEndDate(endDate,0) ;
                    VehicleL1 vehicleL1 = new VehicleL1() ;
                    vehicleL1.calc(vehicleVo,start,end);
                }
            }

//            for (int i = 0 ; i < diffNum ;i++){
//
//                Date start = DateUtil.getStartDate(startDate,i) ;
//                Date end = DateUtil.getEndDate(startDate,i) ;
//
//                for (VehicleVo vehicleVo : vehicleVos){
//                    VehicleL1 vehicleL1 = new VehicleL1() ;
//                    vehicleL1.calc(vehicleVo,start,end);
//                }
//            }
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
//        int diffNum = DateUtil.diffDate(startDate,endDate) ;

        List<VehicleVo> vehicleVos = this.talkVehicle(vinCodes);
        if(vehicleVos.size() > 0 ) {
            for (VehicleVo vehicleVo : vehicleVos){
                Date start = DateUtil.getStartDate(startDate,0) ;
                Date end = DateUtil.getEndDate(endDate,0) ;
                VehicleL2 vehicleL2 = new VehicleL2() ;
                vehicleL2.calc(vehicleVo,start ,end ,monthDay);
            }
        }

//        for (int i = 0 ; i < diffNum ;i++){
//
//            Date start = DateUtil.getStartDate(startDate,i) ;
//            Date end = DateUtil.getEndDate(startDate,monthDay) ;
//
//            /** 当不满足monthDay天数时，执行最后一次 */
//            if (DateUtil.compare(end,endDate)){
//                end = endDate ;
//                i = diffNum ;
//            }
//            if (DateUtil.diffDate(end,endDate) == 0){
//                i = diffNum ;
//            }
//
//            List<VehicleVo> vehicleVos = this.talkVehicle(vinCodes);
//            for (VehicleVo vehicleVo : vehicleVos){
//                VehicleL2 vehicleL2 = new VehicleL2() ;
//                vehicleL2.calc(vehicleVo,start ,end );
//            }
//        }
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

        for (int i = 0 ; i < diffNum ;i++){

            Date start = DateUtil.getStartDate(startDate,i) ;
            Date end = DateUtil.getEndDate(startDate,monthDay-1) ;

            /** 当不满足monthDay天数时，执行最后一次 */
            if (DateUtil.compare(end,endDate) || DateUtil.diffDate(end,endDate) == 0){
                i = diffNum ;
            }

            List<VehicleVo> vehicleVos = this.talkVehicle(vinCodes);
            VehicleL3 vehicleL3 = new VehicleL3() ;
            vehicleL3.calc(vehicleVos,start ,end );
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
//                if (j == 1) break;
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

}
