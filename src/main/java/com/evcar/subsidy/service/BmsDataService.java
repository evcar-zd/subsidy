package com.evcar.subsidy.service;

import com.evcar.subsidy.entity.BmsData;
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
 * Created by Kong on 2017/5/10.
 */
public class BmsDataService {

    private static Logger s_logger = LoggerFactory.getLogger(BmsDataService.class);

    /**
     * 查询历史BMS数据NUM
     * @return
     */
    public static Long getHisBmsDataNum(String vinCode, Date startDate, Date endDate){
        long size = 0L ;
        try{
            Client client = ESTools.getClient() ;
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.matchQuery("vinCode",vinCode))
                    .must(QueryBuilders.rangeQuery("collectTime")
                            .from(DateUtil.format(startDate))
                            .to(DateUtil.format(endDate)));
            SearchRequestBuilder search = client.prepareSearch(Constant.HIS_BMS_INDEX).
                    setTypes(Constant.HIS_BMS_TYPE).setQuery(qb);
            SearchResponse sr = search.get();//得到查询结果
            size = sr.getHits().getTotalHits();//读取数量
        }catch (Exception e){
            s_logger.error("Connection is closed"+e.getMessage());
            ESTools.connectionError();
        }

        return size ;
    }

    /**
     * 查询历史BMS数据
     * @return
     */
    public static List<BmsData> getHisBmsData(String vinCode, Date startDate, Date endDate, long sizeNum){

        List<BmsData> list = new ArrayList<>() ;
        try {
            Client client = ESTools.getClient() ;
            SortBuilder sortBuilder = SortBuilders.fieldSort("collectTime").order(SortOrder.ASC);
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.matchQuery("vinCode",vinCode))
                    .must(QueryBuilders.rangeQuery("collectTime")
                            .from(DateUtil.format(startDate))
                            .to(DateUtil.format(endDate)));
            SearchRequestBuilder search = client.prepareSearch(Constant.HIS_BMS_INDEX).
                    setTypes(Constant.HIS_BMS_TYPE)
                    .addSort(sortBuilder)
                    .setQuery(qb)
                    .setFrom(0)
                    .setSize((int)sizeNum);

            SearchResponse sr = search.get();//得到查询结果
            for(SearchHit hits:sr.getHits()){
                String json = JacksonUtil.toJSon(hits.getSource()) ;
                s_logger.debug(json);
                BmsData bmsData = JacksonUtil.readValue(json, BmsData.class);
                list.add(bmsData) ;
            }
        }catch (Exception e){
            s_logger.error("Connection is closed"+e.getMessage());
            ESTools.connectionError();
        }
//        s_logger.info("fetched {} hisBmsData", list.size());

        return list ;
    }
}
