package com.evcar.subsidy.agg;

import com.evcar.subsidy.GitVer;
import com.evcar.subsidy.entity.BmsData;
import com.evcar.subsidy.entity.HisCountData;
import com.evcar.subsidy.entity.HisVehicleMotor;
import com.evcar.subsidy.entity.ObcData;
import com.evcar.subsidy.service.*;

import java.util.Date;
import java.util.List;

/**
 * Created by Kong on 2017/5/11.
 */
public class VehicleBase {

    protected List<BmsData> bmsDatas ;

    protected List<ObcData> obcDatas ;

    protected List<HisVehicleMotor> hisVehicleMotors ;

    protected Integer gpsCount ;

    /**
     * 载入数L1据
     * @param vinCode
     * @param startDate
     * @param endDate
     */
    public void load(String vinCode, Date startDate, Date endDate){
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
    public void save(HisCountData hisCountData){
        GitVer gitVer = new GitVer() ;
        hisCountData.setVersion(gitVer.getVersion());
        HisCountDataService.addHisCountData(hisCountData);
    }
}
