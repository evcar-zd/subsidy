package com.evcar.subsidy.util;

import com.evcar.subsidy.entity.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kong on 2017/5/15.
 */
public class OrganizationUtil {

    /**
     * 获取某段时间内的HisVehicleMotor
     * @param hisVehicleMotors
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<HisVehicleMotor> getHisVehicleMotors(List<HisVehicleMotor> hisVehicleMotors, Date startDate,Date endDate){
        List<HisVehicleMotor> vehicleMotors = new ArrayList<>() ;
        if (hisVehicleMotors != null && hisVehicleMotors.size() > 0){
            for (HisVehicleMotor hisVehicleMotor : hisVehicleMotors){
                Date collectTime = hisVehicleMotor.getCollectTime() ;
                if (collectTime.getTime()>= startDate.getTime() && collectTime.getTime() <= endDate.getTime()){
                    vehicleMotors.add(hisVehicleMotor) ;
                }
            }
        }
        return vehicleMotors ;
    }


    /**
     * 获取某段时间内的ObcData
     * @param obcDatas
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<ObcData> getObcDatas(List<ObcData> obcDatas, Date startDate, Date endDate){
        List<ObcData> obcDataList = new ArrayList<>() ;
        if (obcDatas != null && obcDatas.size() > 0){
            for (ObcData obcData : obcDatas){
                Date collectTime = obcData.getCollectTime() ;
                if (collectTime.getTime()>= startDate.getTime() && collectTime.getTime() <= endDate.getTime()){
                    obcDataList.add(obcData) ;
                }
            }
        }
        return obcDataList ;
    }

    /**
     * 获取某段时间内的BmsData
     * @param bmsDatas
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<BmsData> getBmsDatas(List<BmsData> bmsDatas, Date startDate, Date endDate){
        List<BmsData> bmsDataList = new ArrayList<>() ;
        if (bmsDatas != null && bmsDatas.size() > 0){
            for (BmsData bmsData : bmsDatas){
                Date collectTime = bmsData.getCollectTime() ;
                if (collectTime.getTime()>= startDate.getTime() && collectTime.getTime() <= endDate.getTime()){
                    bmsDataList.add(bmsData) ;
                }
            }
        }
        return bmsDataList ;
    }

    /**
     * 获取某段时间内的GpsNumber
     * @param gpsDatas
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getGpsNumber(List<HisGpsData> gpsDatas, Date startDate, Date endDate){
        List<HisGpsData> gpsDataList = new ArrayList<>() ;
        if (gpsDatas != null && gpsDatas.size() > 0){
            for (HisGpsData hisGpsData : gpsDatas){
                Date collectTime = hisGpsData.getCollectTime() ;
                if (collectTime.getTime()>= startDate.getTime() && collectTime.getTime() <= endDate.getTime()){
                    gpsDataList.add(hisGpsData) ;
                }
            }
        }
        return gpsDataList.size() ;
    }

    /**
     * 获取某段时间内的BmsData
     * @param hvacDatas
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<HvacData> getHvacDatas(List<HvacData> hvacDatas, Date startDate, Date endDate){
        List<HvacData> hvacDataList = new ArrayList<>() ;
        if (hvacDatas != null && hvacDatas.size() > 0){
            for (HvacData hvacData : hvacDatas){
                Date collectTime = hvacData.getCollectTime() ;
                if (collectTime.getTime()>= startDate.getTime() && collectTime.getTime() <= endDate.getTime()){
                    hvacDataList.add(hvacData) ;
                }
            }
        }
        return hvacDataList ;
    }

    /**
     * 获取某段时间内的HisCountData
     * @param hisCountDatas
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<HisCountData> getHisCountData(List<HisCountData> hisCountDatas, Date startDate, Date endDate){
        List<HisCountData> hisCountDataList = new ArrayList<>() ;
        if (hisCountDatas != null && hisCountDatas.size() > 0){
            for (HisCountData hisCountData : hisCountDatas){
                Date collectTime = hisCountData.getTm() ;
                if (collectTime.getTime()>= startDate.getTime() && collectTime.getTime() <= endDate.getTime()){
                    hisCountDataList.add(hisCountData) ;
                }
            }
        }
        return hisCountDataList ;
    }
}
