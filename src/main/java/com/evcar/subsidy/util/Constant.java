package com.evcar.subsidy.util;

/**
 * 常量类
 * Created by Kong on 2017/4/20.
 */
public class Constant {

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

}
