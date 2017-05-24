package com.evcar.subsidy.service;

import com.evcar.subsidy.entity.HisGpsData;
import com.evcar.subsidy.util.ESTools;
import com.evcar.subsidy.util.JacksonUtil;
import com.evcar.subsidy.util.ZonedDateTimeUtil;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.evcar.subsidy.util.Constant.HISGPS_INDEX;
import static com.evcar.subsidy.util.Constant.HISGPS_TYPE;

/**
 * Created by Kong on 2017/4/24.
 */
public class GpsDataService {
    private static Logger s_logger = LoggerFactory.getLogger(GpsDataService.class);


    /**
     * 获取GPS数据条数
     * @param vinCode
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getHisGpsDataNum(String vinCode, Date startDate,Date endDate, Client client){
        long size = 0L ;
        try{
            QueryBuilder qb = new BoolQueryBuilder() ;
            if (startDate != null && endDate != null ){
                qb = new BoolQueryBuilder()
                        .must(QueryBuilders.matchQuery("vinCode",vinCode))
                        .must(QueryBuilders.rangeQuery("collectTime")
                                .from(ZonedDateTimeUtil.format(startDate))
                                .to(ZonedDateTimeUtil.format(endDate)));
            }
            SearchRequestBuilder search = client.prepareSearch(HISGPS_INDEX)
                    .setTypes(HISGPS_TYPE).setQuery(qb) ;
            SearchResponse sr = search.get();//得到查询结果
            size = sr.getHits().getTotalHits();//读取数量
        }catch (Exception e){
            s_logger.error("Connection is closed"+e.getMessage());
            ESTools.connectionError();
        }

        return size ;
    }



    /**
     * 查询历史GpsData数据
     * @return
     */
    public static List<HisGpsData> getHisGpsData(String vinCode, Date startDate, Date endDate , long sizeNum, Client client){

        List<HisGpsData> list = new ArrayList<>() ;
        try{
            SortBuilder sortBuilder = SortBuilders.fieldSort("collectTime").order(SortOrder.ASC);
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.matchQuery("vinCode",vinCode))
                    .must(QueryBuilders.rangeQuery("collectTime")
                            .from(ZonedDateTimeUtil.format(startDate))
                            .to(ZonedDateTimeUtil.format(endDate)));
            SearchRequestBuilder search = client.prepareSearch(HISGPS_INDEX).
                    setTypes(HISGPS_TYPE)
                    .addSort(sortBuilder)
                    .setQuery(qb)
                    .setFrom(0)
                    .setSize((int)sizeNum);

            SearchResponse sr = search.get();//得到查询结果
            for(SearchHit hits:sr.getHits()){
                String json = JacksonUtil.toJSon(hits.getSource()) ;
                s_logger.debug(json);
                HisGpsData hisGpsData = JacksonUtil.readValue(json, HisGpsData.class);
                list.add(hisGpsData) ;
            }
        }catch (Exception e){
            s_logger.error("Connection is closed"+e.getMessage());
            ESTools.connectionError();
        }
//        s_logger.info("fetched {} hisGpsData", list.size());
        return list ;
    }


    /**
     * 查询从前是否有GPS数据
     * @param vinCode
     * @return
     */
    public static long getGpsNumber(String vinCode,Date endDate ,Client client){
        long size = 0L ;
        try {
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.matchQuery("vinCode", vinCode))
                    .must(QueryBuilders.rangeQuery("collectTime")
                            .lte(ZonedDateTimeUtil.format(endDate)));

            SearchRequestBuilder search = client.prepareSearch(HISGPS_INDEX)
                    .setTypes(HISGPS_TYPE).setQuery(qb)
                    .setSize(0)
                    .setTerminateAfter(1);
            SearchResponse sr = search.get();//得到查询结果
            size = sr.getHits().getTotalHits() ;
        }catch (Exception e){
            s_logger.error("getCanOrGps Connection is closed"+e.getMessage());
            ESTools.connectionError();
        }
        return size ;
    }

}
