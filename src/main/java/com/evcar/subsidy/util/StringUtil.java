package com.evcar.subsidy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Kong on 2017/2/21.
 */
public class StringUtil {

    /**
     * 判断是否为null或空串（去空格了），是返回 true
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        //当str = null时为true，后面的不执行了，所以str = null时不会执行trim()，所以就没问题
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断一个字符串是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        for (int i = 0; i < str.length(); i++){
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    /**
     * 处理字符串
     * @param str
     * @return
     */
    public static String dealStr(String str){
        str = isEmpty(str) ? null : str ;
        return str ;
    }

    public static void clearStr(String str){
        str = null ;
    }

}
