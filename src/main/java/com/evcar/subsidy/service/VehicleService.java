package com.evcar.subsidy.service;


import com.evcar.subsidy.entity.HisCountData;
import com.evcar.subsidy.entity.Vehicle;
import com.evcar.subsidy.util.Constant;
import com.evcar.subsidy.util.ESTools;
import com.evcar.subsidy.util.JacksonUtil;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
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
import java.util.List;

/**
 * Created by Kong on 2017/4/19.
 */
public class VehicleService {
    private static Logger s_logger = LoggerFactory.getLogger(VehicleService.class);

    /**
     * 获取车辆总数
     * @return
     */
    public static long getVehicleNum(){
        Client client = ESTools.getClient() ;
        SearchRequestBuilder search = client.prepareSearch(Constant.VEHICLE_INDEX).setTypes(Constant.VEHICLE_TYPE) ;
        SearchResponse sr = search.get();//得到查询结果
        return sr.getHits().getTotalHits();//读取数量
    }

    /**
     * 查询所有车辆信息
     * @return
     */
    public static List<Vehicle> getVehicleList() {

        Client client = ESTools.getClient() ;
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
     * 查询所有车辆信息
     * @return
     */
    public static List<Vehicle> getVehicleByPage(Integer currentPage , Integer pageSize) {

        Client client = ESTools.getClient() ;
        List<Vehicle> list = new ArrayList<>() ;
        SortBuilder sortBuilder = SortBuilders.fieldSort("produceTime").order(SortOrder.ASC);
        SearchRequestBuilder search = client.prepareSearch(Constant.VEHICLE_INDEX)
                .setTypes(Constant.VEHICLE_TYPE)
                .addSort(sortBuilder)
                .setFrom((currentPage-1)*pageSize)
                .setSize(currentPage*pageSize);

        SearchResponse sr = search.get();//得到查询结果

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
     * 根据vinCode查询车辆对象
     * @param vinCode
     * @return
     */
    public static Vehicle getVehicle(String vinCode){
        Client client = ESTools.getClient() ;
        QueryBuilder qb = new BoolQueryBuilder()
                .must(QueryBuilders.matchQuery("vinCode",vinCode)) ;
        SearchRequestBuilder search = client.prepareSearch(Constant.VEHICLE_INDEX)
                .setTypes(Constant.VEHICLE_TYPE).setQuery(qb) ;
        SearchResponse sr = search.get() ;

        Vehicle vehicle = null ;
        for(SearchHit hits:sr.getHits()){
            String json = JacksonUtil.toJSon(hits.getSource()) ;
            vehicle = JacksonUtil.readValue(json, Vehicle.class);
            break;
        }
        return vehicle ;
    }


}
