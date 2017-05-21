package com.evcar.subsidy.agg;

import com.evcar.subsidy.GitVer;
import com.evcar.subsidy.entity.*;
import com.evcar.subsidy.service.*;
import com.evcar.subsidy.util.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by Kong on 2017/5/11.
 */
public class VehicleBase {

    protected List<BmsData> bmsDatas ;

    protected List<ObcData> obcDatas ;

    protected List<HisVehicleMotor> hisVehicleMotors ;

    protected List<HisGpsData> hisGpsDatas ;

    protected List<HisCountData> hisCountDatas ;

    protected List<HvacData> hvacDatas ;

    protected List<HisCountDataL2> hisCountDataL2s ;

    /**
     * 载入数L1据
     * @param vinCode
     * @param startDate
     * @param endDate
     */
    protected void load(String vinCode, Date startDate, Date endDate){
        /** 查询历史整车和电机数据 */
        long sizeNum = VehicleMotorService.getHisVehicleMotorNum(vinCode,startDate,endDate) ;
        if (sizeNum > 0)
            this.hisVehicleMotors = VehicleMotorService.getHisVehicleMotor(vinCode,startDate,endDate,sizeNum) ;

        /** 查询BMS数据 */
        long bmsNum = BmsDataService.getHisBmsDataNum(vinCode,startDate,endDate);
        if (bmsNum > 0 )
            this.bmsDatas = BmsDataService.getHisBmsData(vinCode,startDate,endDate,bmsNum) ;

        /** 查询OBS数据 */
        long obcNum = ObcDataService.getHisObcDataNum(vinCode,startDate,endDate);
        if (bmsNum > 0 )
            this.obcDatas = ObcDataService.getHisObcData(vinCode,startDate,endDate,obcNum) ;

        /** 查询HVAC数据 */
        long hvacNum = HvacDataService.getHvacDataNum(vinCode,startDate,endDate) ;
        if (hvacNum > 0)
            this.hvacDatas = HvacDataService.getHisHvacData(vinCode,startDate,endDate,hvacNum) ;

        /** 获取GPS数据 */
        long gpsCount = GpsDataService.getHisGpsDataNum(vinCode,startDate,endDate) ;
        if (gpsCount > 0)
            hisGpsDatas = GpsDataService.getHisGpsData(vinCode,startDate,endDate,gpsCount) ;
    }

    /**
     * 保存L1数据
     * @param hisCountData
     */
    protected void save(HisCountData hisCountData){
        GitVer gitVer = new GitVer() ;
        hisCountData.setVersion(gitVer.getVersion());
        HisCountDataService.addHisCountData(hisCountData);
    }

    /**
     * 获取L1的数据
     * @param vinCode
     * @param startDate
     * @param endDate
     */
    protected void loadL1(String vinCode, Date startDate, Date endDate){
        hisCountDatas = HisCountDataService.getHisCountData(vinCode,startDate,endDate) ;
    }


    /**
     * L1 取startDate前一天和endDate当天
     * L2取endDate 前一天
     * @param vinCode
     * @param startDate
     * @param endDate
     */
    protected void calcL2(String vinCode, Date startDate, Date endDate){

        Date start = DateUtil.getStartDate(startDate,1) ;
        hisCountDatas = HisCountDataService.getHisCountDataL1(start,endDate,vinCode) ;
        Date end = DateUtil.getStartDate(endDate,1) ;
        hisCountDataL2s = HisCountDataService.getHisCountDataL2(vinCode,end) ;
    }


    /**
     * 保存L2数据
     * @param hisCountData
     */
    protected void saveL2(HisCountDataL2 hisCountData){
        GitVer gitVer = new GitVer() ;
        hisCountData.setVersion(gitVer.getVersion());
        HisCountDataService.addHisCountDataL2(hisCountData) ;
    }


    /**
     * 保存L3数据
     * @param monthCountData
     */
    protected void saveL3(MonthCountData monthCountData){
        GitVer gitVer = new GitVer() ;
        monthCountData.setVersion(gitVer.getVersion());

        MonthCountDataService.addMonthCountData(monthCountData);
    }

    /**
     * 获取L2数据
     * @param vinCode
     * @param startDate
     * @param endDate
     */
    protected List<HisCountDataL2> loadL2(String vinCode,Date startDate ,Date endDate){
        return HisCountDataService.getHisCountDataL2(vinCode,startDate,endDate) ;
    }


    protected boolean getCanL1(String vinCode){
        long size = HisCountDataService.getCanOrGps(vinCode,0);
        return size > 0 ? true : false ;
    }

    protected boolean getGpsL1(String vinCode){
        long size = HisCountDataService.getCanOrGps(vinCode,1);
        return size > 0 ? true : false ;
    }


}
