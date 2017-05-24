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

    protected List<HisVehicleMotor> lastMileages ;

    protected List<HisGpsData> hisGpsDatas ;

    protected List<HisCountData> hisCountDatas ;

    protected List<HvacData> hvacDatas ;

    protected List<HisCountData> hisCountDatasL1 ;

    protected List<HisCountDataL2> hisCountDataL2s ;

    protected Long canhisCount ;
    protected Long gpshisCount ;

    protected static OverlappedLoader s_loader = new OverlappedLoader();

    protected static OverlappedLoaderL2 loaderL2 = new OverlappedLoaderL2();

    /**
     * 载入数L1据
     * @param vinCode
     * @param startDate
     * @param endDate
     */
    protected void load(String vinCode, Date startDate, Date endDate){
        OverlappedData data = s_loader.load(vinCode, startDate, endDate);
        this.bmsDatas = data.bmsDatas;
        this.obcDatas = data.obcDatas;
        this.hisVehicleMotors = data.hisVehicleMotors;
        this.hisGpsDatas = data.hisGpsDatas;
        this.hvacDatas = data.hvacDatas;
        this.lastMileages = data.lastMileages ;
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
    protected void loadL1(String vinCode, Date startDate,Date endDate,Integer monthDay){
        OverlappedDateL2 data = loaderL2.load(vinCode, startDate, endDate, monthDay) ;

        this.hisCountDatasL1 = data.hisCountDatasL1 ;
        this.hisCountDataL2s = data.hisCountDataL2s ;
        this.hisCountDatas = data.hisCountDatas ;
        this.canhisCount = data.canhisCount ;
        this.gpshisCount = data.gpshisCount ;
    }


    /**
     * 保存L2数据
     * @param hisCountData
     */
    protected void saveL2(HisCountDataL2 hisCountData){
        loaderL2.asyncSave(hisCountData);
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


}
