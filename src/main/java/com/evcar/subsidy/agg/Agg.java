package com.evcar.subsidy.agg;

import com.evcar.subsidy.entity.Vehicle;
import com.evcar.subsidy.entity.VehicleVo;
import com.evcar.subsidy.service.VehicleService;
import com.evcar.subsidy.util.DateUtil;
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


    /**
     * 每MAX_SIZE分为一组，避免数据量大出现内存不足情况
     * 后期可以使用多线程去处理（快速）
     */
    public static final Integer MAX_SIZE = 500 ;


    /**
     * 计算L1入口
     * @param startDay
     * @param endDay
     * @param vinCodes
     */
    public void takeAgg(int startDay, int endDay, List<String> vinCodes){
        Date date = new Date() ;
        Date startDate = DateUtil.getDate(date,startDay) ;
        Date endDate = DateUtil.getDate(date,endDay) ;
        int diffNum = DateUtil.diffDate(startDate,endDate) ;

        List<VehicleVo> vehicleVos = this.talkVehicle(vinCodes);
        for (int i = 0 ; i < diffNum ;i++){

            Date start = DateUtil.getStartDate(startDate,i) ;
            Date end = DateUtil.getEndDate(startDate,i) ;

            for (VehicleVo vehicleVo : vehicleVos){
                VehicleL1 vehicleL1 = new VehicleL1() ;
                vehicleL1.calc(vehicleVo,start,end);
            }

        }

    }

    /**
     * 获取需要计算车辆信息
     * @param vinCodes
     * @return
     */
    public List<VehicleVo> talkVehicle(List<String> vinCodes){
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
                    VehicleVo vehicleVo = new VehicleVo(vehicle.getVinCode(),vehicle.getCarType() ,vehicle.getVeDeliveredDate()) ;
                    vehicleVos.add(vehicleVo) ;
                }
                currentPage ++ ;
            }
        }else{
            for (String vinCode : vinCodes){
                Vehicle vehicle = VehicleService.getVehicle(vinCode) ;
                VehicleVo vehicleVo = new VehicleVo(vehicle.getVinCode(),vehicle.getCarType() ,vehicle.getVeDeliveredDate()) ;
                vehicleVos.add(vehicleVo) ;
            }
        }
        return vehicleVos ;
    }


}
