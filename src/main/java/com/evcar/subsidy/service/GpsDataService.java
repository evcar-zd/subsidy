package com.evcar.subsidy.service;

import com.evcar.subsidy.entity.HisGpsData;
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
    public static long getHisGpsDataNum(String vinCode, Date startDate,Date endDate){
        long size = 0L ;
        try{
            Client client = ESTools.getClient() ;
            QueryBuilder qb = new BoolQueryBuilder() ;
            if (startDate != null && endDate != null ){
                qb = new BoolQueryBuilder()
                        .must(QueryBuilders.matchQuery("vinCode",vinCode))
                        .must(QueryBuilders.rangeQuery("collectTime")
                                .from(DateUtil.format(startDate))
                                .to(DateUtil.format(endDate)));
            }
            SearchRequestBuilder search = client.prepareSearch(Constant.HISGPS_INDEX)
                    .setTypes(Constant.HISGPS_TYPE).setQuery(qb) ;
            SearchResponse sr = search.get();//得到查询结果
            size = sr.getHits().getTotalHits();//读取数量
        }catch (Exception e){
            s_logger.error("Connection is closed"+e.getMessage());
        }

        return size ;
    }



    /**
     * 查询历史GpsData数据
     * @return
     */
    public static List<HisGpsData> getHisGpsData(String vinCode, Date startDate, Date endDate , long sizeNum){

        List<HisGpsData> list = new ArrayList<>() ;
        try{
            Client client = ESTools.getClient() ;
            SortBuilder sortBuilder = SortBuilders.fieldSort("collectTime").order(SortOrder.ASC);
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.matchQuery("vinCode",vinCode))
                    .must(QueryBuilders.rangeQuery("collectTime")
                            .from(DateUtil.format(startDate))
                            .to(DateUtil.format(endDate)));
            SearchRequestBuilder search = client.prepareSearch(Constant.HISGPS_INDEX).
                    setTypes(Constant.HISGPS_TYPE)
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
        }
//        s_logger.info("fetched {} hisGpsData", list.size());
        return list ;
    }
}
