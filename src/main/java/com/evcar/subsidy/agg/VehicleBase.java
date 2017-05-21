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

    protected static OverlappedLoader s_loader = new OverlappedLoader();

    /**
     * 载入数L1据
     * @param vinCode
     * @param startDate
     * @param endDate
     */
    protected void load(String vinCode, Date startDate, Date endDate){
        OverlappedData data = s_loader.load(vinCode);
        this.bmsDatas = data.bmsDatas;
        this.obcDatas = data.obcDatas;
        this.hisVehicleMotors = data.hisVehicleMotors;
        this.hisGpsDatas = data.hisGpsDatas;
        this.hvacDatas = data.hvacDatas;
    }

    /**
     * 保存L1数据
     * @param hisCountData
     */
    protected void save(HisCountData hisCountData){
        s_loader.asyncSave(hisCountData);
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
