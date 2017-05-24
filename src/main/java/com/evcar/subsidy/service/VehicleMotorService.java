package com.evcar.subsidy.service;

import com.evcar.subsidy.entity.HisVehicleMotor;
import com.evcar.subsidy.util.*;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.evcar.subsidy.util.Constant.*;

/**
 * Created by Kong on 2017/4/21.
 */
public class VehicleMotorService {
    private static Logger s_logger = LoggerFactory.getLogger(VehicleMotorService.class);



    /**
     * 查询历史整车和电机数据
     * @return
     */
    public static Long getHisVehicleMotorNum(String vinCode, Date startDate, Date endDate, Client client){
        long size = 0L;
        try {
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.matchQuery("vinCode",vinCode))
                    .must(QueryBuilders.rangeQuery("collectTime")
                            .from(ZonedDateTimeUtil.format(startDate))
                            .to(ZonedDateTimeUtil.format(endDate)));
            SearchRequestBuilder search = client.prepareSearch(HISVEHICLE_MOTOR_INDEX).
                    setTypes(HISVEHICLE_MOTOR_TYPE).setQuery(qb);
            SearchResponse sr = search.get();//得到查询结果
            size = sr.getHits().getTotalHits() ;
        }catch (Exception e){
            s_logger.error("Connection is closed"+e.getMessage());
            ESTools.connectionError();
        }
        return size ;//读取数量

    }

    /**
     * 查询历史整车和电机数据
     * @return
     */
    public static List<HisVehicleMotor> getHisVehicleMotor(String vinCode, Date startDate, Date endDate,long sizeNum, Client client){

        List<HisVehicleMotor> list = new ArrayList<>() ;
        try{
            SortBuilder sortBuilder = SortBuilders.fieldSort("collectTime").order(SortOrder.ASC);
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.matchQuery("vinCode",vinCode))
                    .must(QueryBuilders.rangeQuery("collectTime")
                            .from(ZonedDateTimeUtil.format(startDate))
                            .to(ZonedDateTimeUtil.format(endDate)));
            SearchRequestBuilder search = client.prepareSearch(HISVEHICLE_MOTOR_INDEX).
                    setTypes(HISVEHICLE_MOTOR_TYPE)
                    .addSort(sortBuilder)
                    .setQuery(qb)
                    .setFrom(0)
                    .setSize((int)sizeNum);

            SearchResponse sr = search.get();//得到查询结果
            for(SearchHit hits:sr.getHits()){
                String json = JacksonUtil.toJSon(hits.getSource()) ;
                s_logger.debug(json);
                HisVehicleMotor hisVehicleMotor = JacksonUtil.readValue(json, HisVehicleMotor.class);
                list.add(hisVehicleMotor) ;
            }
        }catch (Exception e){
            s_logger.error("Connection is closed"+e.getMessage());
            ESTools.connectionError();
        }
//        s_logger.info("fetched {} hisVehicleMotor", list.size());

        return list ;
    }



    /**
     * 查询历史整车和电机最大里程数据
     * @return
     */
    public static List<HisVehicleMotor> getMaxMileage(String vinCode, Date endDate, Client client){
        List<HisVehicleMotor> list = new ArrayList<>() ;
        try{
            SortBuilder sortBuilder = SortBuilders.fieldSort("mileage").order(SortOrder.DESC);
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.matchQuery("vinCode",vinCode))
                    .must(QueryBuilders.rangeQuery("collectTime")
                            .lte(ZonedDateTimeUtil.format(endDate)));
            SearchRequestBuilder search = client.prepareSearch(HISVEHICLE_MOTOR_INDEX).
                    setTypes(HISVEHICLE_MOTOR_TYPE)
                    .addSort(sortBuilder)
                    .setQuery(qb)
                    .setFrom(0)
                    .setSize(1);

            SearchResponse sr = search.get();//得到查询结果
            for(SearchHit hits:sr.getHits()){
                String json = JacksonUtil.toJSon(hits.getSource()) ;
                s_logger.debug(json);
                HisVehicleMotor hisVehicleMotor = JacksonUtil.readValue(json, HisVehicleMotor.class);
                list.add(hisVehicleMotor) ;
            }
        }catch (Exception e){
            s_logger.error("Connection is closed"+e.getMessage());
            ESTools.connectionError();
        }
        return list ;
    }

    /**
     * 查询所有有SOC的整车和电机数据的数量
     * @param vinCode
     * @return
     */
    public static Long getHisVehicleMotorNumber(String vinCode){
        long size = 0L ;
        try {
            Client client = ESTools.getClient() ;
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.matchQuery("vinCode",vinCode))
                    .must(QueryBuilders.rangeQuery("soc")
                            .from(0)
                            .to(100));
            SearchRequestBuilder search = client.prepareSearch(HISVEHICLE_MOTOR_INDEX).
                    setTypes(HISVEHICLE_MOTOR_TYPE).setQuery(qb);

            SearchResponse sr = search.get();//得到查询结果
            size = sr.getHits().getTotalHits() ;
        }catch (Exception e){
            s_logger.error("Connection is closed"+e.getMessage());
            ESTools.connectionError();
        }
        return size ;//读取数量
    }


    /**
     * 查询从前是否有Can数据
     * @param vinCode
     * @return
     */
    public static long getCanNumber(String vinCode,Date endDate ,Client client){
        long size = 0L ;
        try {
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.matchQuery("vinCode", vinCode))
                    .must(QueryBuilders.rangeQuery("collectTime")
                            .lte(ZonedDateTimeUtil.format(endDate)));
            SearchRequestBuilder search = client.prepareSearch(HISVEHICLE_MOTOR_INDEX)
                    .setTypes(HISVEHICLE_MOTOR_TYPE).setQuery(qb)
                    .setSize(0)
                    .setTerminateAfter(1);
            SearchResponse sr = search.get();//得到查询结果
            size = sr.getHits().getTotalHits() ;
        }catch (Exception e){
            s_logger.error("getCan Connection is closed"+e.getMessage());
            ESTools.connectionError();
        }
        return size ;
    }
}
