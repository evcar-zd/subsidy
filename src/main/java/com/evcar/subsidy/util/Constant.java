package com.evcar.subsidy.util;

/**
 * 常量类
 * Created by Kong on 2017/4/20.
 */
public class Constant {

    /** 排除输出电流大于MAX_CURRENT的数据 */
    public static final Integer MAX_CURRENT         = 19 ;

    public static final String LOGO                     =   "ZD" ;

    /** ES 车辆  index&type*/
    public static final String VEHICLE_INDEX            =   "vehicle_archives_v1" ;
    public static final String VEHICLE_TYPE             =   "vehicle_info" ;


    /**  ES 实时GPRS数据 */
    public static final String RTGPS_INDEX              =   "i_rt_gprs_data_v1" ;
    public static final String RTGPS_TYPE               =   "rt_gprs_data" ;

    /**  ES 历史GPRS数据 */
    public static final String HISGPS_INDEX             =   "i_his_gprs_data_v1" ;
    public static final String HISGPS_TYPE              =   "his_gprs_data" ;

    /**  ES 实时整车和电机数据  */
    public static final String RTVEHICLE_MOTOR_INDEX    =   "i_rt_vehicle_motor_data_v2" ;
    public static final String RTVEHICLE_MOTOR_TYPE     =   "rt_vehicle_motor_data" ;

    /**  ES 历史整车和电机数据  */
    public static final String HISVEHICLE_MOTOR_INDEX   =   "i_his_vehicle_motor_data_v2" ;
    public static final String HISVEHICLE_MOTOR_TYPE    =   "his_vehicle_motor_data" ;

    /**  ES 实时电池数据  */
    public static final String RTBATTERY_INDEX          =   "i_rt_battery_data_v1" ;
    public static final String RTBATTERY_TYPE           =   "rt_battery_data" ;

    /**  ES 实时电池数据  */
    public static final String HISBATTERY_INDEX         =   "i_his_battery_data_v1" ;
    public static final String HISBATTERY_TYPE          =   "his_battery_data" ;

    /**  ES BMS数据  */
    public static final String RT_BMS_INDEX             =   "i_rt_bms_data_v1" ;
    public static final String RT_BMS_TYPE              =   "rt_bms_data" ;

    /**  ES BMS数据  */
    public static final String HIS_BMS_INDEX             =   "i_his_bms_data_v1" ;
    public static final String HIS_BMS_TYPE              =   "his_bms_data" ;

    /**  ES OBC数据  */
    public static final String RT_OBC_INDEX             =   "i_rt_obc_data_v1" ;
    public static final String RT_OBC_TYPE              =   "rt_obc_data" ;

    /**  ES OBC历史数据  */
    public static final String HIS_OBC_INDEX            =   "i_his_obc_data_v1" ;
    public static final String HIS_OBC_TYPE             =   "his_obc_data" ;

    /**  ES OBC历史数据  */
    public static final String HIS_HVAC_INDEX           =   "i_his_hvac_data_v1" ;
    public static final String HIS_HVAC_TYPE            =   "his_hvac_data" ;

    /** ES算法数据 */
    public static final String HISCOUNT_DATA_INDEX      =   "hiscount_data_v1" ;
    public static final String HISCOUNT_DATA_TYPE       =   "hiscount_data" ;

    public static final String HISCOUNT_DATAL2_INDEX    =   "hiscount_datal2_v1" ;
    public static final String HISCOUNT_DATAL2_TYPE     =   "hiscount_datal2" ;

    public static final String HISCOUNT_DATAL3_INDEX    =   "hiscount_datal3_v1" ;
    public static final String HISCOUNT_DATAL3_TYPE     =   "hiscount_datal3" ;


    /**  指标常量  **/
    public static final int MILEAGE                     =   1 ;
    public static final int LIMITMILEAGE                =   2 ;
    public static final int MAXENERGYTIME               =   3 ;
    public static final int MAXELECTRICPOWER            =   4 ;
    public static final int AVGDAILYRUNTIME             =   5 ;
    public static final int HUNDREDSKMUSEPOWER          =   6 ;

}
