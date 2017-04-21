package com.evcar.subsidy.util;


import com.evcar.subsidy.entity.HisVehicleMotor;
import com.evcar.subsidy.entity.Vehicle;
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

/**
 * Created by Kong on 2017/4/19.
 */
public class SelectManager{
    private static Logger s_logger = LoggerFactory.getLogger(SelectManager.class);

    /**
     * 获取车辆总数
     * @param client
     * @return
     */
    public static long getVehicleNum(Client client){
        SearchRequestBuilder search = client.prepareSearch(Constant.VEHICLE_INDEX).setTypes(Constant.VEHICLE_TYPE) ;
        SearchResponse sr = search.get();//得到查询结果
        return sr.getHits().getTotalHits();//读取数量
    }

    /**
     * 查询所有车辆信息
     * @param client
     * @return
     */
    public static List<Vehicle> getVehicleList(Client client) {

        List<Vehicle> list = new ArrayList<>() ;
        SearchRequestBuilder search = client.prepareSearch(Constant.VEHICLE_INDEX).setTypes(Constant.VEHICLE_TYPE) ;
        SearchResponse sr = search.get();//得到查询结果
        long sizeNum = sr.getHits().getTotalHits();//读取数量

        SortBuilder sortBuilder = SortBuilders.fieldSort("produceTime").order(SortOrder.ASC);
        search = search.addSort(sortBuilder).setFrom(0).setSize((int)sizeNum);
        sr = search.get();//得到查询结果

        for(SearchHit hits:sr.getHits()){
            String json = JacksonUtil.toJSon(hits.getSource()) ;
            s_logger.debug(json);
            Vehicle vehicle = JacksonUtil.readValue(json, Vehicle.class);
            list.add(vehicle) ;
        }
        s_logger.info("fetched {} vehicles", list.size());
        return list ;
    }

    /**
     * 查询历史整车和电机数据
     * @return
     */
    public static List<HisVehicleMotor> getHisVehicleMotor(Client client,String vinCode,Date startDate,Date endDate){
        List<HisVehicleMotor> list = new ArrayList<>() ;
        QueryBuilder qb = new BoolQueryBuilder()
                .must(QueryBuilders.matchQuery("vinCode",vinCode))
                .must(QueryBuilders.rangeQuery("reciveTime").from("2016-01-03 01:00:00").to("2017-04-03 01:00:00"));
        SearchRequestBuilder search = client.prepareSearch(Constant.HISVEHICLE_MOTOR_INDEX).
                setTypes(Constant.HISVEHICLE_MOTOR_TYPE).setQuery(qb);
        SearchResponse sr = search.get();//得到查询结果
        for(SearchHit hits:sr.getHits()){
            String json = JacksonUtil.toJSon(hits.getSource()) ;
            s_logger.debug(json);
            HisVehicleMotor hisVehicleMotor = JacksonUtil.readValue(json, HisVehicleMotor.class);
            list.add(hisVehicleMotor) ;
        }

        s_logger.info("fetched {} hisVehicleMotor", list.size());

        return list ;
    }

}
