package com.evcar.subsidy.agg;

import com.evcar.subsidy.entity.*;
import com.evcar.subsidy.util.CountUtil;
import com.evcar.subsidy.util.DateUtil;
import com.evcar.subsidy.util.OrganizationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.evcar.subsidy.util.Constant.MAX_CURRENT;

/**
 * 单车计算
 * Created by Kong on 2017/5/11.
 */
@Component
public class VehicleL1 extends VehicleBase{

    private static Logger s_logger = LoggerFactory.getLogger(VehicleL1.class);

    private static ESBean esBean;
    @Autowired
    void setEsBean(ESBean esBean) { this.esBean = esBean;}

    protected HisCountData hisCountData ;

    /**
     * 计算单车方法
     * @param vehicleVo
     * @param startDate
     * @param endDate
     */
    protected void calc(VehicleVo vehicleVo, Date startDate , Date endDate){
        this.load(vehicleVo.getVinCode(),startDate,endDate);
        this.calcVehicle(vehicleVo,startDate,endDate);
    }

    public void preFetch(String vin, Date startDate, Date endDate){
        s_loader.preFetch(vin, startDate, endDate);
    }

    public void stopQueue(){
        s_loader.stop();
    }

    /**
     * 获取startDate到endDate的Motors、BMS、OBC等数据处理
     * 支持一天到多天的数据处理
     * 暂时是一天数据处理
     * 处理每辆车每天数据
     * @param vehicleVo
     * @param startDate
     * @param endDate
     */
    private void calcVehicle(VehicleVo vehicleVo,Date startDate,Date endDate){

        int diffNum = DateUtil.diffDate(startDate,endDate) ;
        diffNum = diffNum == 0 ? 1 : diffNum ;
        for (int i = 0 ; i < diffNum ;i++){
            Date start = DateUtil.getStartDate(startDate,i) ;
            Date end = DateUtil.getEndDate(startDate,i) ;
            /**
             * 数据处理，避免多次请求
             */
            List<HisVehicleMotor> hisVehicleMotors = OrganizationUtil.getHisVehicleMotors(this.hisVehicleMotors,start,end) ;
            List<ObcData> obcDatas = OrganizationUtil.getObcDatas(this.obcDatas, start, end);
            List<BmsData> bmsDatas = OrganizationUtil.getBmsDatas(this.bmsDatas, start, end) ;
            int gpsCount = OrganizationUtil.getGpsNumber(this.hisGpsDatas,start,end) ;

            this.calcTarget(vehicleVo,start,bmsDatas,obcDatas,hisVehicleMotors,gpsCount);

            this.save(this.hisCountData);
        }
    }

    /**
     * 计算六项指标
     */
    private void calcTarget(VehicleVo vehicleVo,Date startDate,List<BmsData> bmsDatas ,List<ObcData> obcDatas,List<HisVehicleMotor> hisVehicleMotors,int gpsCount){
        String vinCode = vehicleVo.getVinCode() ;
        String id = vinCode + DateUtil.getDateStryyyyMMdd(startDate);
        String carType = vehicleVo.getCarType() ;
        Date veDeliveredDate = vehicleVo.getVeDeliveredDate() ;
        Date releaseTime = vehicleVo.getReleaseTime() ;
        this.hisCountData = new HisCountData() ;

        this.hisCountData.setId(id);
        this.hisCountData.setTm(startDate);
        this.hisCountData.setVinCode(vinCode);
        this.hisCountData.setCarType(carType);
        this.hisCountData.setVeDeliveredDate(veDeliveredDate);
        this.hisCountData.setReleaseTime(releaseTime);
        this.hisCountData.setGpsCount(gpsCount);

        /**
         * BMS数据计算
         * 近似线性中段充电SOC
         * 似线性中段充电时间(单位：S)
         * */
        this.calcBMSData(bmsDatas);
        /**
         * 计算OBC数据
         * 最大输入电功率
         * 最大输出电功率
         */
        this.calcOBCData(obcDatas);
        /**
         * 计算VehicleMotor数据
         * CAN数据条数
         * 总里程
         * 近似线性中段当日总放电SOC
         * 近似线性中段当日总行驶里程
         */
        this.calcVehicleMotor(hisVehicleMotors);

        this.hisCountData.setCalcTime(new Date());
    }


    /** 当充电前时长大于30分钟，则不取该值，避免大的误差 */
    public static final Integer MAX_TIME = 1800 ;

    /**
     * 计算BMS数据
     * 近似线性中段充电SOC
     * 近似线性中段充电时间(单位：S)
     */
    private void calcBMSData(List<BmsData> bmsDatas){
        Integer chargeMidSoc1 = 0 ;                 //近似线性中段充电SOC-Model1
        Long chargeMidSec1 = 0L ;                   //近似线性中段充电时间(单位：S)-Model1
        Integer chargeMidSoc2 = 0 ;                 //近似线性中段充电SOC-Model2
        Long chargeMidSec2 = 0L ;                   //近似线性中段充电时间(单位：S)-Model2
        Integer chargeMidSoc3 = 0 ;                 //近似线性中段充电SOC-Model3
        Long chargeMidSec3 = 0L ;                   //近似线性中段充电时间(单位：S)-Model3
        Long dischargeTotalSec = 0L ;              //放电总时长(单位：S)

        /** 计算SOC&Time相关数据 */
        if(bmsDatas != null ){
            BmsData beforeBmsData = null ;
            for (BmsData bmsData : bmsDatas){

                bmsData.getAlowableCurrent() ;
                if (beforeBmsData != null){
                    if (bmsData.getSoc() != null && bmsData.getSoc() <= esBean.getMaxSoc() && bmsData.getSoc() >= esBean.getMinSoc()){
                        /** 只用取充电数据 */
                        if (bmsData.getBatteryGroupStatus() != null && bmsData.getBatteryGroupStatus() == 3){

                            Integer soc = bmsData.getSoc() - beforeBmsData.getSoc() ;
                            /** 是否选取该值 */
                            boolean choose = true ;
                            /** 时间算法 */
                            if(bmsData.getCollectTime() != null && beforeBmsData.getCollectTime() != null){
                                Long time =  DateUtil.getSeconds(beforeBmsData.getCollectTime(),bmsData.getCollectTime());
                                /** 去掉不符合标准的数据 */
                                if(time > MAX_TIME){
                                    if (beforeBmsData.getBatteryGroupStatus()!=3 ){
                                        choose = false ;
                                    }
                                }

                                BigDecimal alowableCurrent = bmsData.getAlowableCurrent() ;

                                /** 获取充电模式 */
                                int model = CountUtil.getChargeModel(alowableCurrent) ;

                                /** 负载类型 = 0（电池时，才计入数据） */
                                boolean loadType = bmsData.getLoadType() != null && bmsData.getLoadType() == 0 ? true : false ;
                                /**
                                 * 如果time大于0，正常
                                 * 如果time小于0，说明出现补传
                                 * 如果soc大于0，说明正在充电
                                 *  如果soc小于0，说明正在放电
                                 *  soc等于0，说明静止不动
                                 */
                                /** 充电时差 */
                                if (choose && soc > 0 && loadType){
//                                    System.out.println("soc:"+soc+"****sec:"+time+"****save:"+choose);
                                    if (model == 1){
                                        chargeMidSec1 += (time > 0 && bmsData.getBatteryGroupStatus() == 3) ? time : 0 ;
                                        chargeMidSoc1 += soc > 0 && bmsData.getBatteryGroupStatus() == 3 ? soc : 0 ;//充电
                                    }else if(model == 2){
                                        chargeMidSec2 += (time > 0 && bmsData.getBatteryGroupStatus() == 3) ? time : 0 ;
                                        chargeMidSoc2 += soc > 0 && bmsData.getBatteryGroupStatus() == 3 ? soc : 0 ;//充电
                                    }else if (model == 3){
                                        chargeMidSec3 += (time > 0 && bmsData.getBatteryGroupStatus() == 3) ? time : 0 ;
                                        chargeMidSoc3 += soc > 0 && bmsData.getBatteryGroupStatus() == 3 ? soc : 0 ;//充电
                                    }

                                    if (time < 0){
                                        s_logger.info("replenish data :{}",bmsData.getVinCode());
                                    }
                                }
                            }
                        }
                    }
                    int maxDisCharge = 300 ;
                    /** 放电总时长 */
                    if(bmsData.getCollectTime() != null && beforeBmsData.getCollectTime() != null) {
                        Long time = DateUtil.getSeconds(beforeBmsData.getCollectTime(), bmsData.getCollectTime());
                        dischargeTotalSec += (time <= maxDisCharge && time > 0 && bmsData.getBatteryGroupStatus() != null && bmsData.getBatteryGroupStatus() == 1) ? time : 0 ;
                    }

                }
                beforeBmsData = bmsData ;
            }
        }
        this.hisCountData.setChargeMidSoc1(chargeMidSoc1);
        this.hisCountData.setChargeMidSec1(chargeMidSec1);
        this.hisCountData.setChargeMidSoc2(chargeMidSoc2);
        this.hisCountData.setChargeMidSec2(chargeMidSec2);
        this.hisCountData.setChargeMidSoc3(chargeMidSoc3);
        this.hisCountData.setChargeMidSec3(chargeMidSec3);
        this.hisCountData.setDischargeTotalSec(dischargeTotalSec) ;
    }

    /**
     * 计算OBC数据
     * 最大输入电功率
     * 最大输出电功率
     */
    private void calcOBCData(List<ObcData> obcDatas){
        BigDecimal maxInChargerPower = BigDecimal.ZERO ;      //最大输入电功率
        BigDecimal maxOutChargerPower = BigDecimal.ZERO ;     //最大输出电功率
        /** 计算最大电功率 ** 排除电流大于MAX_CURRENT */
        if(obcDatas != null){
            for (ObcData obcData : obcDatas){
                /** 最大电功率 1 2 */
                BigDecimal inTotalVoltage = obcData.getInVoltage() != null ? obcData.getInVoltage() : BigDecimal.ZERO ;
                BigDecimal inTotalCurrent = obcData.getInCurrent() != null ? obcData.getInCurrent() : BigDecimal.ZERO ;
                BigDecimal inElectricPower = inTotalCurrent.compareTo(BigDecimal.valueOf(MAX_CURRENT)) == 1 ? BigDecimal.ZERO : inTotalVoltage.multiply(inTotalCurrent) ;
                maxInChargerPower = inElectricPower.compareTo(maxInChargerPower) == 1 ? inElectricPower : maxInChargerPower ;

                BigDecimal outTotalVoltage = obcData.getOutVoltage() != null ? obcData.getOutVoltage() : BigDecimal.ZERO ;
                BigDecimal outTotalCurrent = obcData.getOutCurrent() != null ? obcData.getOutCurrent() : BigDecimal.ZERO ;
                BigDecimal outElectricPower = outTotalCurrent.compareTo(BigDecimal.valueOf(MAX_CURRENT)) == 1 ? BigDecimal.ZERO : outTotalVoltage.multiply(outTotalCurrent) ;
                maxOutChargerPower = outElectricPower.compareTo(maxOutChargerPower) == 1 ? outElectricPower : maxOutChargerPower ;
            }
        }

        this.hisCountData.setMaxInChargerPower(maxInChargerPower);
        this.hisCountData.setMaxOutChargerPower(maxOutChargerPower);
    }

    /**
     * 计算VehicleMotor数据
     * CAN数据条数
     * 总里程
     * 近似线性中段当日总放电SOC
     * 近似线性中段当日总行驶里程
     */
    private void calcVehicleMotor(List<HisVehicleMotor> hisVehicleMotors){

        Integer canCount = 0 ;                                  //CAN数据条数
        BigDecimal mileageTotal = null;                         //总里程
        Integer dischargeMidSoc = 0 ;                           //近似线性中段当日总放电SOC
        BigDecimal dischargeMidMileage = BigDecimal.ZERO;       //近似线性中段当日总行驶里程

        /** 计算线性SOC、近线性里程相关数据 */
        if (hisVehicleMotors != null ){
            HisVehicleMotor beforeVehicleMotor = null ;

            /** 变量定义 放电SOC & 行驶里程 */
            Integer lineSoc = 0 ;
            BigDecimal lineMileage = BigDecimal.ZERO ;
            for (HisVehicleMotor vehicleMotor : hisVehicleMotors){
                if (beforeVehicleMotor != null){
                    Date start = beforeVehicleMotor.getCollectTime() ;
                    Date end = vehicleMotor.getCollectTime() ;
                    List<HvacData> hvacDatas = OrganizationUtil.getHvacDatas(this.hvacDatas, start, end) ;
                    /** 空调开启，不计入近似线性中段当日总放电SOC、近似线性中段当日总行驶里程 */
                    boolean execute = true ;
                    for (int i = 0 ; i < hvacDatas.size() ; i++){
                        HvacData hvacData = hvacDatas.get(i) ;
                        if (hvacData.getRunStatus() != null && hvacData.getRunStatus() != 0) {
                            execute = false ;
                            break;
                        }
                    }
                    if (execute){

                        /** SOC、里程 处理 */
                        if (vehicleMotor.getMileage() != null && beforeVehicleMotor.getMileage() != null && vehicleMotor.getSoc() != null
                                && beforeVehicleMotor.getSoc() != null && vehicleMotor.getSoc() <= esBean.getMaxSoc() && vehicleMotor.getSoc() >= esBean.getMinSoc()){
                            /**
                             * 如果mileage大于0，说明正在运动
                             * 如果mileage小于0，说明里程跳变，后期处理
                             */
                            BigDecimal mileage = vehicleMotor.getMileage().subtract(beforeVehicleMotor.getMileage());

                            Integer soc = vehicleMotor.getSoc() - beforeVehicleMotor.getSoc() ;

                            if (mileage.compareTo(BigDecimal.ZERO) != -1 && soc <= 0){
                                lineMileage = lineMileage.add(mileage) ;
                                lineSoc -= soc ;
                            }else {
                                if( lineSoc > esBean.getLinearSoc() && lineMileage.compareTo(BigDecimal.valueOf(esBean.getLinearMileage())) == 1){
                                    dischargeMidSoc += lineSoc ;
                                    dischargeMidMileage = dischargeMidMileage.add(lineMileage) ;
                                }
                                lineSoc = 0 ;
                                lineMileage = BigDecimal.ZERO ;
                            }

                            if (mileage.compareTo(BigDecimal.ZERO) == -1){
                                s_logger.info("mileage jump :{}",vehicleMotor.getVinCode());
                            }
                        }
                    }
                }
                beforeVehicleMotor = vehicleMotor ;
                /** CAN数据条数计算 */
                canCount++;
            }

            /** 最后线性SOC、近线性里程处理*/
            if( lineSoc > esBean.getLinearSoc() && lineMileage.compareTo(BigDecimal.valueOf(esBean.getLinearMileage())) == 1){
                dischargeMidSoc += lineSoc ;
                dischargeMidMileage = dischargeMidMileage.add(lineMileage) ;
            }
        }

        /** 总里程算法 去空*/
        if (this.lastMileages != null && this.lastMileages.size() != 0){
            mileageTotal = this.lastMileages.get(0).getMileage() ;
        }
        mileageTotal = mileageTotal == null ? BigDecimal.ZERO : mileageTotal ;

        this.hisCountData.setCanCount(canCount);
        this.hisCountData.setMileageTotal(mileageTotal);
        this.hisCountData.setDischargeMidSoc(dischargeMidSoc);
        this.hisCountData.setDischargeMidMileage(dischargeMidMileage);
    }

}
