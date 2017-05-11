package com.evcar.subsidy.service;

import com.evcar.subsidy.entity.HisCountData;
import com.evcar.subsidy.entity.MonthCountData;
import com.evcar.subsidy.util.Constant;
import com.evcar.subsidy.util.DateUtil;
import com.evcar.subsidy.util.ESTools;
import com.evcar.subsidy.util.JacksonUtil;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
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
public class HisCountDataService {

    private static Logger s_logger = LoggerFactory.getLogger(HisCountDataService.class);

    /**
     * 创建计算数据索引
     */
    public static void createHisCountIndex(){

        Client client = ESTools.getClient() ;
        IndicesExistsResponse inExistsResponse = client.admin().indices()
                .exists(new IndicesExistsRequest(Constant.HISCOUNT_DATA_INDEX)).actionGet();
        if (!inExistsResponse.isExists()){

            client.admin().indices().prepareCreate(Constant.HISCOUNT_DATA_INDEX).execute().actionGet();

            // mapping
            XContentBuilder mappingBuilder;
            try {
                mappingBuilder = XContentFactory.jsonBuilder()
                        .startObject()
                        .startObject(Constant.HISCOUNT_DATA_INDEX)
                        .startObject("properties")
                        .startObject("id").field("type", "string").field("store", "yes").endObject()
                        .startObject("vinCode").field("type", "string").field("store", "yes").endObject()
                        .startObject("gprsCode").field("type", "string").field("store", "yes").endObject()
                        .startObject("carType").field("type", "string").field("store", "yes").endObject()
                        .startObject("consumeSoc").field("type", "integer").field("store", "yes").endObject()
                        .startObject("chargeSoc").field("type", "integer").field("store", "yes").endObject()
                        .startObject("surplusSoc").field("type", "integer").field("store", "yes").endObject()
                        .startObject("mileagePoor").field("type", "double").field("store", "yes").endObject()
                        .startObject("mileageTotal").field("type", "double").field("store", "yes").endObject()
                        .startObject("chargeTime").field("type", "long").field("store", "yes").endObject()
                        .startObject("dischargeTime").field("type", "long").field("store", "yes").endObject()
                        .startObject("maxElectricPower").field("type", "double").field("store", "yes").endObject()
                        .startObject("gpsNumber").field("type", "integer").field("store", "yes").endObject()
                        .startObject("canNumber").field("type", "integer").field("store", "yes").endObject()
                        .startObject("runDate").field("type", "date").field("store", "yes").endObject()
                        .startObject("countDate").field("type", "date").field("store", "yes").endObject()
                        .endObject()
                        .endObject()
                        .endObject();
                PutMappingRequest mapping = Requests.putMappingRequest(Constant.HISCOUNT_DATA_INDEX).type(Constant.HISCOUNT_DATA_TYPE).source(mappingBuilder);
                client.admin().indices().putMapping(mapping).actionGet();
            } catch (Exception e) {
                s_logger.error("--------- createIndex 创建 mapping 失败：",e);
            }
        }
    }

    /**
     * 新增计算数据
     * @param hisCountData
     * @return
     */
    public static void addHisCountData(HisCountData hisCountData){
        Client client = ESTools.getClient() ;
        try {
            client.prepareIndex(Constant.HISCOUNT_DATA_INDEX,Constant.HISCOUNT_DATA_TYPE,hisCountData.getId())
                    .setSource(JacksonUtil.toJSon(hisCountData)).execute().get();
        } catch (Exception e) {
            s_logger.error("save hisCountData ERROR");
        }
    }


    /**
     * 删除计算数据
     * @return
     */
    public static boolean deleteHisCountData(String id){
        Client client = ESTools.getClient() ;
        boolean flag = false ;
        try{
            DeleteResponse deleteResponse = client.prepareDelete(Constant.HISCOUNT_DATA_INDEX,Constant.HISCOUNT_DATA_TYPE,id).execute().get() ;
            flag = deleteResponse.isFound() ;
        } catch (Exception e) {
            s_logger.error("delete hisCountData ERROR");
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
                .exists(new IndicesExistsRequest(Constant.HISCOUNT_DATA_INDEX)).actionGet();
        if (inExistsResponse.isExists())
            client.admin().indices().prepareDelete(Constant.HISCOUNT_DATA_INDEX).get();
    }


    /**
     * 查询计算数据
     * @param startDate
     * @param endDate
     * @param currentPage
     * @param pageSize
     * @return
     */
    public static List<HisCountData> getHisCountData(Date startDate, Date endDate,Integer currentPage,Integer pageSize){

        Client client = ESTools.getClient() ;

        List<HisCountData> list = new ArrayList<>() ;
        SortBuilder sortBuilder = SortBuilders.fieldSort("countDate").order(SortOrder.ASC);
        QueryBuilder qb = new BoolQueryBuilder()
                .must(QueryBuilders.rangeQuery("countDate")
                        .from(startDate.getTime())
                        .to(startDate.getTime()));
        SearchRequestBuilder search = client.prepareSearch(Constant.HISCOUNT_DATA_INDEX)
                .setTypes(Constant.HISCOUNT_DATA_TYPE).setQuery(qb).addSort(sortBuilder)
                .setFrom((currentPage-1)*pageSize)
                .setSize(pageSize);
        SearchResponse sr = search.get();//得到查询结果
        for(SearchHit hits:sr.getHits()){
            String json = JacksonUtil.toJSon(hits.getSource()) ;
            s_logger.debug(json);
            HisCountData hisCountData = JacksonUtil.readValue(json, HisCountData.class);
            list.add(hisCountData) ;
        }

        s_logger.info("fetched {} hisCountData", list.size());
        return list ;
    }

    /**
     * 获取时间段的计算数据条数
     * @param startDate
     * @param endDate
     * @return
     */
    public static Long getHisCountDataNumber(Date startDate, Date endDate){
        Client client = ESTools.getClient() ;
        QueryBuilder qb = new BoolQueryBuilder()
                .must(QueryBuilders.rangeQuery("countDate")
                        .from(startDate.getTime())
                        .to(endDate.getTime()));
        SearchRequestBuilder search = client.prepareSearch(Constant.HISCOUNT_DATA_INDEX)
                .setTypes(Constant.HISCOUNT_DATA_TYPE).setQuery(qb);
        SearchResponse sr = search.get();//得到查询结果
        return sr.getHits().getTotalHits() ;
    }



    /**
     * 创建计算数据索引V2
     */
    public static void createHisCountIndexL2(){

        Client client = ESTools.getClient() ;
        IndicesExistsResponse inExistsResponse = client.admin().indices().exists(new IndicesExistsRequest(Constant.HISCOUNT_DATAL2_INDEX)).actionGet();
        if (!inExistsResponse.isExists()){
            // settings
            Settings settings = Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 2).build();
            // mapping
            XContentBuilder mappingBuilder;
            try {
                mappingBuilder = XContentFactory.jsonBuilder()
                        .startObject()
                        .startObject(Constant.HISCOUNT_DATAL2_INDEX)
                        .startObject("properties")
                        .startObject("id").field("type", "string").field("store", "yes").endObject()
                        .startObject("mileage.normal").field("type", "integer").field("store", "yes").endObject()
                        .startObject("mileage.lowSide").field("type", "integer").field("store", "yes").endObject()
                        .startObject("mileage.highSide").field("type", "integer").field("store", "yes").endObject()
                        .startObject("mileage.invalids").field("type", "integer").field("store", "yes").endObject()
                        .startObject("limitMileage.normal").field("type", "integer").field("store", "yes").endObject()
                        .startObject("limitMileage.lowSide").field("type", "integer").field("store", "yes").endObject()
                        .startObject("limitMileage.highSide").field("type", "integer").field("store", "yes").endObject()
                        .startObject("limitMileage.invalids").field("type", "integer").field("store", "yes").endObject()
                        .startObject("maxEnergyTime.normal").field("type", "integer").field("store", "yes").endObject()
                        .startObject("maxEnergyTime.lowSide").field("type", "integer").field("store", "yes").endObject()
                        .startObject("maxEnergyTime.highSide").field("type", "integer").field("store", "yes").endObject()
                        .startObject("maxEnergyTime.invalids").field("type", "integer").field("store", "yes").endObject()
                        .startObject("maxElectricPower.normal").field("type", "integer").field("store", "yes").endObject()
                        .startObject("maxElectricPower.lowSide").field("type", "integer").field("store", "yes").endObject()
                        .startObject("maxElectricPower.highSide").field("type", "integer").field("store", "yes").endObject()
                        .startObject("maxElectricPower.invalids").field("type", "integer").field("store", "yes").endObject()
                        .startObject("avgDailyRunTime.normal").field("type", "integer").field("store", "yes").endObject()
                        .startObject("avgDailyRunTime.lowSide").field("type", "integer").field("store", "yes").endObject()
                        .startObject("avgDailyRunTime.highSide").field("type", "integer").field("store", "yes").endObject()
                        .startObject("avgDailyRunTime.invalids").field("type", "integer").field("store", "yes").endObject()
                        .startObject("hundredsKmusePower.normal").field("type", "integer").field("store", "yes").endObject()
                        .startObject("hundredsKmusePower.lowSide").field("type", "integer").field("store", "yes").endObject()
                        .startObject("hundredsKmusePower.highSide").field("type", "integer").field("store", "yes").endObject()
                        .startObject("hundredsKmusePower.invalids").field("type", "integer").field("store", "yes").endObject()
                        .startObject("gpsNormal").field("type", "integer").field("store", "yes").endObject()
                        .startObject("gpsNearNoData").field("type", "integer").field("store", "yes").endObject()
                        .startObject("gpsNoData").field("type", "integer").field("store", "yes").endObject()
                        .startObject("canNormal").field("type", "integer").field("store", "yes").endObject()
                        .startObject("canNearNoData").field("type", "integer").field("store", "yes").endObject()
                        .startObject("canNoData").field("type", "integer").field("store", "yes").endObject()
                        .startObject("countDate").field("type", "date").field("store", "yes").endObject()
                        .startObject("vehicleNum").field("type", "integer").field("store", "yes").endObject()
                        .endObject()
                        .endObject()
                        .endObject();
                IndicesAdminClient indicesAdminClient = client.admin().indices();
                CreateIndexResponse response = indicesAdminClient.prepareCreate(Constant.HISCOUNT_DATAL2_INDEX)
                        .setSettings(settings)
                        .addMapping(Constant.HISCOUNT_DATAL2_INDEX, mappingBuilder)
                        .get();
                if (!response.isAcknowledged())
                    s_logger.info("创建索引失败");
            } catch (Exception e) {
                s_logger.error("--------- createIndex 创建 mapping 失败：",e);
            }
        }
    }

    /**
     * 新增一段时间计算数据
     * @param monthCountData
     * @return
     */
    public static boolean addHisCountDataL2(MonthCountData monthCountData){
        Client client = ESTools.getClient() ;
        boolean flag = false ;
        try {
            IndexResponse indexResponse = client.prepareIndex(Constant.HISCOUNT_DATAL2_INDEX,Constant.HISCOUNT_DATAL2_TYPE,monthCountData.getId())
                    .setSource(JacksonUtil.toJSon(monthCountData)).execute().get();
            flag = indexResponse.isCreated() ;
        } catch (Exception e) {
            s_logger.error("save hisCountDataL2 ERROR");
        }
        return flag ;
    }

    /**
     * 删除一段时间计算数据
     * @return
     */
    public static boolean deleteHisCountDataL2(String id){
        Client client = ESTools.getClient() ;
        boolean flag = false ;
        try{
            DeleteResponse deleteResponse = client.prepareDelete(Constant.HISCOUNT_DATAL2_INDEX,Constant.HISCOUNT_DATAL2_TYPE,id).execute().get() ;
            flag = deleteResponse.isFound() ;
        } catch (Exception e) {
            s_logger.error("delete hisCountDataL2 ERROR");
        }
        return flag ;
    }

    /**
     * 删除所有数据
     * 慎用
     */
    public static void deleteHisCountDataL2(){
        Client client = ESTools.getClient() ;
        IndicesExistsResponse inExistsResponse = client.admin().indices()
                .exists(new IndicesExistsRequest(Constant.HISCOUNT_DATAL2_INDEX)).actionGet();
        if (inExistsResponse.isExists())
            client.admin().indices().prepareDelete(Constant.HISCOUNT_DATAL2_INDEX).get();
    }


    /**
     * 查询计算数据
     * @param startDate
     * @param endDate
     * @param sizeNum
     * @return
     */
    public static List<MonthCountData> getMonthCountData(Date startDate, Date endDate,long sizeNum){

        Client client = ESTools.getClient() ;

        List<MonthCountData> list = new ArrayList<>() ;
        SortBuilder sortBuilder = SortBuilders.fieldSort("countDate").order(SortOrder.ASC);
        QueryBuilder qb = new BoolQueryBuilder()
                .must(QueryBuilders.rangeQuery("countDate")
                        .from(startDate.getTime())
                        .to(endDate.getTime()));
        SearchRequestBuilder search = client.prepareSearch(Constant.HISCOUNT_DATAL2_INDEX)
                .setTypes(Constant.HISCOUNT_DATAL2_TYPE).setQuery(qb).addSort(sortBuilder)
                .setFrom(0)
                .setSize((int)sizeNum);
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
        Long num = Long.valueOf(0);
        try {
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.rangeQuery("countDate")
                            .from(startDate.getTime())
                            .to(endDate.getTime()));
            SearchRequestBuilder search = client.prepareSearch(Constant.HISCOUNT_DATAL2_INDEX)
                    .setTypes(Constant.HISCOUNT_DATAL2_TYPE).setQuery(qb);
            SearchResponse sr = search.get();//得到查询结果
            num = sr.getHits().getTotalHits() ;
        } catch (Exception e){
            s_logger.error("hiscount_datal2_v1 is nonentity");
        }

        return num ;
    }


    /**
     * 查询计算数据
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<MonthCountData> getMonthCountData(Date startDate, Date endDate){

        Client client = ESTools.getClient() ;

        List<MonthCountData> list = new ArrayList<>() ;
        SortBuilder sortBuilder = SortBuilders.fieldSort("countDate").order(SortOrder.DESC);
        QueryBuilder qb = new BoolQueryBuilder()
                .must(QueryBuilders.rangeQuery("countDate")
                        .from(startDate.getTime())
                        .to(endDate.getTime()));
        SearchRequestBuilder search = client.prepareSearch(Constant.HISCOUNT_DATAL2_INDEX)
                .setTypes(Constant.HISCOUNT_DATAL2_TYPE).setQuery(qb).addSort(sortBuilder)
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

}
