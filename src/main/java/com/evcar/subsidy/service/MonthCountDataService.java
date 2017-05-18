package com.evcar.subsidy.service;

import com.evcar.subsidy.entity.HisCountData;
import com.evcar.subsidy.entity.MonthCountData;
import com.evcar.subsidy.util.Constant;
import com.evcar.subsidy.util.ESTools;
import com.evcar.subsidy.util.JacksonUtil;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
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
 * Created by Kong on 2017/5/12.
 */
public class MonthCountDataService {

    private static Logger s_logger = LoggerFactory.getLogger(MonthCountDataService.class);

    /**
     * 新增计算数据
     * @param monthCountData
     * @return
     */
    public static void addMonthCountData(MonthCountData monthCountData){
        Client client = ESTools.getClient() ;
        try {
            client.prepareIndex(Constant.HISCOUNT_DATAL3_INDEX,Constant.HISCOUNT_DATAL3_TYPE,monthCountData.getId())
                    .setSource(JacksonUtil.toJSon(monthCountData)).execute().get();
        } catch (Exception e) {
            s_logger.error("save monthCountData ERROR");
        }
    }


    /**
     * 删除计算数据
     * @return
     */
    public static boolean deleteMonthCountData(String id){
        Client client = ESTools.getClient() ;
        boolean flag = false ;
        try{
            DeleteResponse deleteResponse = client.prepareDelete(Constant.HISCOUNT_DATAL3_INDEX,Constant.HISCOUNT_DATAL3_TYPE,id).execute().get() ;
            flag = deleteResponse.isFound() ;
        } catch (Exception e) {
            s_logger.error("delete monthCountData ERROR");
        }
        return flag ;
    }


    /**
     * 删除所有数据
     * 慎用
     */
    public static void deleteByIndex(){
        Client client = ESTools.getClient() ;
        IndicesExistsResponse inExistsResponse = client.admin().indices()
                .exists(new IndicesExistsRequest(Constant.HISCOUNT_DATAL3_INDEX)).actionGet();
        if (inExistsResponse.isExists())
            client.admin().indices().prepareDelete(Constant.HISCOUNT_DATAL3_INDEX).get();
    }

    /**
     * 查询计算数据
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<MonthCountData> getLastMonthCountData(Date startDate, Date endDate){

        Client client = ESTools.getClient() ;

        List<MonthCountData> list = new ArrayList<>() ;
        SortBuilder sortBuilder = SortBuilders.fieldSort("calcTime").order(SortOrder.DESC);
        QueryBuilder qb = new BoolQueryBuilder()
                .must(QueryBuilders.rangeQuery("calcTime")
                        .from(startDate.getTime())
                        .to(endDate.getTime()));
        SearchRequestBuilder search = client.prepareSearch(Constant.HISCOUNT_DATAL3_INDEX)
                .setTypes(Constant.HISCOUNT_DATAL3_TYPE).setQuery(qb).addSort(sortBuilder)
                .setFrom(0)
                .setSize(1);
        SearchResponse sr = search.get();//得到查询结果
        for(SearchHit hits:sr.getHits()){
            String json = JacksonUtil.toJSon(hits.getSource()) ;
            MonthCountData monthCountData = JacksonUtil.readValue(json, MonthCountData.class);
            list.add(monthCountData) ;
        }
        s_logger.info("fetched {} monthCountData", list.size());
        return list ;
    }


    /**
     * 查询计算数据
     * @param startDate
     * @param endDate
     * @param number
     * @return
     */
    public static List<MonthCountData> getMonthCountData(Date startDate, Date endDate,Integer number){

        Client client = ESTools.getClient() ;

        List<MonthCountData> list = new ArrayList<>() ;
        SortBuilder sortBuilder = SortBuilders.fieldSort("calcTime").order(SortOrder.DESC);
        QueryBuilder qb = new BoolQueryBuilder()
                .must(QueryBuilders.rangeQuery("calcTime")
                        .from(startDate.getTime())
                        .to(endDate.getTime()));
        SearchRequestBuilder search = client.prepareSearch(Constant.HISCOUNT_DATAL3_INDEX)
                .setTypes(Constant.HISCOUNT_DATAL3_TYPE).setQuery(qb).addSort(sortBuilder)
                .setFrom(0)
                .setSize(number);
        SearchResponse sr = search.get();//得到查询结果
        for(SearchHit hits:sr.getHits()){
            String json = JacksonUtil.toJSon(hits.getSource()) ;
            MonthCountData monthCountData = JacksonUtil.readValue(json, MonthCountData.class);
            list.add(monthCountData) ;
        }
        s_logger.info("fetched {} monthCountData", list.size());
        return list ;
    }


    /**
     * 获取时间段的计算数据条数
     * @param startDate
     * @param endDate
     * @return
     */
    public static Long getMonthCountDataNumber(Date startDate, Date endDate){
        Client client = ESTools.getClient() ;
        Long num = 0L;
        try {
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.rangeQuery("calcTime")
                            .from(startDate.getTime())
                            .to(endDate.getTime()));
            SearchRequestBuilder search = client.prepareSearch(Constant.HISCOUNT_DATAL3_INDEX)
                    .setTypes(Constant.HISCOUNT_DATAL3_TYPE).setQuery(qb);
            SearchResponse sr = search.get();//得到查询结果
            num = sr.getHits().getTotalHits() ;
        } catch (Exception e){
            s_logger.error("hiscount_datal3_v1 is nonentity");
        }
        return num ;
    }

}
