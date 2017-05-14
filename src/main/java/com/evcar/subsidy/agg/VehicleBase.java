package com.evcar.subsidy.agg;

import com.evcar.subsidy.GitVer;
import com.evcar.subsidy.entity.*;
import com.evcar.subsidy.service.*;
import com.evcar.subsidy.util.Constant;

import java.util.Date;
import java.util.List;

import static com.evcar.subsidy.service.HisCountDataService.getCanOrGps;
import static javafx.scene.input.KeyCode.L;

/**
 * Created by Kong on 2017/5/11.
 */
public class VehicleBase {

    protected List<BmsData> bmsDatas ;

    protected List<ObcData> obcDatas ;

    protected List<HisVehicleMotor> hisVehicleMotors ;

    protected Integer gpsCount ;

    protected List<HisCountData> hisCountDatas ;

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

        /** 获取GPS数据 */
        gpsCount = (int) GpsDataService.getHisGpsDataNum(vinCode,startDate,endDate) ;
    }

    /**
     * 保存L1数据
     * @param hisCountData
     */
    protected void save(HisCountData hisCountData){
        GitVer gitVer = new GitVer() ;
        hisCountData.setVersion(gitVer.getVersion());
        /** 先删除之前数据 */
        HisCountDataService.deleteHisCountData(hisCountData.getId()) ;

        HisCountDataService.addHisCountData(hisCountData);
    }

    /**
     * 获取L1的数据
     * @param vinCode
     * @param startDate
     * @param endDate
     */
    protected void loadL1(String vinCode, Date startDate, Date endDate){
        hisCountDatas = HisCountDataService.getHisCountData(vinCode,startDate,endDate, Constant.HISCOUNT_DATA_INDEX,Constant.HISCOUNT_DATA_TYPE) ;
    }

    /**
     * 保存L2数据
     * @param hisCountData
     */
    protected void saveL2(HisCountData hisCountData){
        GitVer gitVer = new GitVer() ;
        hisCountData.setVersion(gitVer.getVersion());
        /** 先删除之前数据 */
        HisCountDataService.deleteHisCountDataL2(hisCountData.getId()) ;

        HisCountDataService.addHisCountDataL2(hisCountData) ;
    }


    /**
     * 保存L3数据
     * @param monthCountData
     */
    protected void saveL3(MonthCountData monthCountData){
        GitVer gitVer = new GitVer() ;
        monthCountData.setVersion(gitVer.getVersion());

        /** 先删除之前数据 */
        MonthCountDataService.deleteMonthCountData(monthCountData.getId()) ;

        MonthCountDataService.addMonthCountData(monthCountData);
    }

    /**
     * 获取L2数据
     * @param vinCode
     * @param startDate
     * @param endDate
     */
    protected List<HisCountData> loadL2(String vinCode,Date startDate ,Date endDate){
        return HisCountDataService.getHisCountData(vinCode,startDate,endDate, Constant.HISCOUNT_DATAL2_INDEX,Constant.HISCOUNT_DATAL2_TYPE) ;
    }


    protected boolean getCanL1(String vinCode){
        List<HisCountData> hisCountDatas = HisCountDataService.getCanOrGps(vinCode,0);
        return hisCountDatas.size() > 0 ? true : false ;
    }

    protected boolean getGpsL1(String vinCode){
        List<HisCountData> hisCountDatas = HisCountDataService.getCanOrGps(vinCode,1);
        return hisCountDatas.size() > 0 ? true : false ;
    }


}
