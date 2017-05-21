package com.evcar.subsidy.service;

import com.evcar.subsidy.entity.HisVehicleMotor;
import com.evcar.subsidy.util.Constant;
import com.evcar.subsidy.util.DateUtil;
import com.evcar.subsidy.util.ESTools;
import com.evcar.subsidy.util.JacksonUtil;
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
 * Created by Kong on 2017/4/21.
 */
public class VehicleMotorService {
    private static Logger s_logger = LoggerFactory.getLogger(VehicleMotorService.class);



    /**
     * 查询历史整车和电机数据
     * @return
     */
    public static Long getHisVehicleMotorNum(String vinCode, Date startDate, Date endDate){
        long size = 0L;
        try {
            Client client = ESTools.getClient() ;
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.matchQuery("vinCode",vinCode))
                    .must(QueryBuilders.rangeQuery("collectTime")
                            .from(DateUtil.format(startDate))
                            .to(DateUtil.format(endDate)));
            SearchRequestBuilder search = client.prepareSearch(Constant.HISVEHICLE_MOTOR_INDEX).
                    setTypes(Constant.HISVEHICLE_MOTOR_TYPE).setQuery(qb);
            SearchResponse sr = search.get();//得到查询结果
            size = sr.getHits().getTotalHits() ;
        }catch (Exception e){
            s_logger.error("Connection is closed"+e.getMessage());
        }
        return size ;//读取数量

    }

    /**
     * 查询历史整车和电机数据
     * @return
     */
    public static List<HisVehicleMotor> getHisVehicleMotor(String vinCode, Date startDate, Date endDate,long sizeNum){

        List<HisVehicleMotor> list = new ArrayList<>() ;
        try{
            Client client = ESTools.getClient() ;
            SortBuilder sortBuilder = SortBuilders.fieldSort("collectTime").order(SortOrder.ASC);
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.matchQuery("vinCode",vinCode))
                    .must(QueryBuilders.rangeQuery("collectTime")
                            .from(DateUtil.format(startDate))
                            .to(DateUtil.format(endDate)));
            SearchRequestBuilder search = client.prepareSearch(Constant.HISVEHICLE_MOTOR_INDEX).
                    setTypes(Constant.HISVEHICLE_MOTOR_TYPE)
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
        }
//        s_logger.info("fetched {} hisVehicleMotor", list.size());

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
            SearchRequestBuilder search = client.prepareSearch(Constant.HISVEHICLE_MOTOR_INDEX).
                    setTypes(Constant.HISVEHICLE_MOTOR_TYPE).setQuery(qb);

            SearchResponse sr = search.get();//得到查询结果
            size = sr.getHits().getTotalHits() ;
        }catch (Exception e){
            s_logger.error("Connection is closed"+e.getMessage());
        }
        return size ;//读取数量
    }
}
