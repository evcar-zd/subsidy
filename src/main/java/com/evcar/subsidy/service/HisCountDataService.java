package com.evcar.subsidy.service;

import com.evcar.subsidy.entity.HisCountData;
import com.evcar.subsidy.entity.HisCountDataL2;
import com.evcar.subsidy.util.Constant;
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

import static com.evcar.subsidy.util.Constant.*;

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
                PutMappingRequest mapping = Requests.putMappingRequest(Constant.HISCOUNT_DATA_INDEX).type(HISCOUNT_DATA_TYPE).source(mappingBuilder);
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
            client.prepareIndex(Constant.HISCOUNT_DATA_INDEX, HISCOUNT_DATA_TYPE,hisCountData.getId())
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
            DeleteResponse deleteResponse = client.prepareDelete(Constant.HISCOUNT_DATA_INDEX, HISCOUNT_DATA_TYPE,id).execute().get() ;
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
     * 查询计算数据L1
     * @param vinCode
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<HisCountData> getHisCountData(String vinCode ,Date startDate, Date endDate){

        Long size = getHisCountDataNumber(vinCode,startDate,endDate);

        Client client = ESTools.getClient() ;

        List<HisCountData> list = new ArrayList<>() ;
        SortBuilder sortBuilder = SortBuilders.fieldSort("tm").order(SortOrder.ASC);
        QueryBuilder qb = new BoolQueryBuilder()
                .must(QueryBuilders.matchQuery("vinCode",vinCode))
                .must(QueryBuilders.rangeQuery("tm")
                        .from(startDate.getTime())
                        .to(endDate.getTime()));
        SearchRequestBuilder search = client.prepareSearch(Constant.HISCOUNT_DATA_INDEX)
                .setTypes(Constant.HISCOUNT_DATA_TYPE).setQuery(qb).addSort(sortBuilder)
                .setFrom(0)
                .setSize(Integer.valueOf(String.valueOf(size)));
        SearchResponse sr = search.get();//得到查询结果
        for(SearchHit hits:sr.getHits()){
            String json = JacksonUtil.toJSon(hits.getSource()) ;
            s_logger.debug(json);
            HisCountData hisCountData = JacksonUtil.readValue(json, HisCountData.class);
            list.add(hisCountData) ;
        }

//        s_logger.info("fetched {} hisCountData", list.size());
        return list ;
    }

    /**
     * 获取时间段的计算数据条数 L1
     * @param vinCode
     * @param startDate
     * @param endDate
     * @return
     */
    public static Long getHisCountDataNumber(String vinCode ,Date startDate, Date endDate){
        Client client = ESTools.getClient() ;
        QueryBuilder qb = new BoolQueryBuilder()
                .must(QueryBuilders.matchQuery("vinCode",vinCode))
                .must(QueryBuilders.rangeQuery("tm")
                        .from(startDate.getTime())
                        .to(endDate.getTime()));
        SearchRequestBuilder search = client.prepareSearch(Constant.HISCOUNT_DATA_INDEX)
                .setTypes(Constant.HISCOUNT_DATA_TYPE).setQuery(qb);
        SearchResponse sr = search.get();//得到查询结果
        return sr.getHits().getTotalHits() ;
    }

    /**
     * 查询计算数据L2
     * @param vinCode
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<HisCountDataL2> getHisCountDataL2(String vinCode ,Date startDate, Date endDate){

        Long size = getHisCountDataNumberL2(vinCode,startDate,endDate);

        Client client = ESTools.getClient() ;

        List<HisCountDataL2> list = new ArrayList<>() ;
        SortBuilder sortBuilder = SortBuilders.fieldSort("tm").order(SortOrder.ASC);
        QueryBuilder qb = new BoolQueryBuilder()
                .must(QueryBuilders.matchQuery("vinCode",vinCode))
                .must(QueryBuilders.rangeQuery("tm")
                        .from(startDate.getTime())
                        .to(endDate.getTime()));
        SearchRequestBuilder search = client.prepareSearch(HISCOUNT_DATAL2_INDEX)
                .setTypes(HISCOUNT_DATAL2_TYPE).setQuery(qb).addSort(sortBuilder)
                .setFrom(0)
                .setSize(Integer.valueOf(String.valueOf(size)));
        SearchResponse sr = search.get();//得到查询结果
        for(SearchHit hits:sr.getHits()){
            String json = JacksonUtil.toJSon(hits.getSource()) ;
            s_logger.debug(json);
            HisCountDataL2 hisCountData = JacksonUtil.readValue(json, HisCountDataL2.class);
            list.add(hisCountData) ;
        }

//        s_logger.info("fetched {} hisCountData", list.size());
        return list ;
    }

    /**
     * 获取时间段的计算数据条数 L2
     * @param vinCode
     * @param startDate
     * @param endDate
     * @return
     */
    public static Long getHisCountDataNumberL2(String vinCode ,Date startDate, Date endDate){
        Client client = ESTools.getClient() ;
        QueryBuilder qb = new BoolQueryBuilder()
                .must(QueryBuilders.matchQuery("vinCode",vinCode))
                .must(QueryBuilders.rangeQuery("tm")
                        .from(startDate.getTime())
                        .to(endDate.getTime()));
        SearchRequestBuilder search = client.prepareSearch(HISCOUNT_DATAL2_INDEX)
                .setTypes(HISCOUNT_DATAL2_TYPE).setQuery(qb);
        SearchResponse sr = search.get();//得到查询结果
        return sr.getHits().getTotalHits() ;
    }


    /**
     * 查询计L1是否有CAN数据或者GPS数据
     * @param vinCode
     * @param mark  标志， 0   can,  1 gps
     * @return
     */
    public static List<HisCountData> getCanOrGps(String vinCode,int mark){
        Client client = ESTools.getClient() ;
        String queryFlag = mark == 1 ? "gpsCount" : "canCount" ;

        List<HisCountData> list = new ArrayList<>() ;
        SortBuilder sortBuilder = SortBuilders.fieldSort("tm").order(SortOrder.ASC);
        QueryBuilder qb = new BoolQueryBuilder()
                .must(QueryBuilders.matchQuery("vinCode",vinCode))
                .must(QueryBuilders.rangeQuery(queryFlag).from(1).to(100));
        SearchRequestBuilder search = client.prepareSearch(Constant.HISCOUNT_DATA_INDEX)
                .setTypes(HISCOUNT_DATA_TYPE).setQuery(qb).addSort(sortBuilder)
                .setFrom(0)
                .setSize(10);
        SearchResponse sr = search.get();//得到查询结果
        for(SearchHit hits:sr.getHits()){
            String json = JacksonUtil.toJSon(hits.getSource()) ;
            s_logger.debug(json);
            HisCountData hisCountData = JacksonUtil.readValue(json, HisCountData.class);
            list.add(hisCountData) ;
        }
        return list ;
    }


    /**
     * 创建计算数据索引V2
     */
    public static void createHisCountIndexL2(){

        Client client = ESTools.getClient() ;
        IndicesExistsResponse inExistsResponse = client.admin().indices().exists(new IndicesExistsRequest(HISCOUNT_DATAL2_INDEX)).actionGet();
        if (!inExistsResponse.isExists()){
            // settings
            Settings settings = Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 2).build();
            // mapping
            XContentBuilder mappingBuilder;
            try {
                mappingBuilder = XContentFactory.jsonBuilder()
                        .startObject()
                        .startObject(HISCOUNT_DATAL2_INDEX)
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
                CreateIndexResponse response = indicesAdminClient.prepareCreate(HISCOUNT_DATAL2_INDEX)
                        .setSettings(settings)
                        .addMapping(HISCOUNT_DATAL2_INDEX, mappingBuilder)
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
     * @param hisCountData
     * @return
     */
    public static boolean addHisCountDataL2(HisCountDataL2 hisCountData){
        Client client = ESTools.getClient() ;
        boolean flag = false ;
        try {
            IndexResponse indexResponse = client.prepareIndex(HISCOUNT_DATAL2_INDEX, HISCOUNT_DATAL2_TYPE,hisCountData.getId())
                    .setSource(JacksonUtil.toJSon(hisCountData)).execute().get();
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
            DeleteResponse deleteResponse = client.prepareDelete(HISCOUNT_DATAL2_INDEX, HISCOUNT_DATAL2_TYPE,id).execute().get() ;
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
                .exists(new IndicesExistsRequest(HISCOUNT_DATAL2_INDEX)).actionGet();
        if (inExistsResponse.isExists())
            client.admin().indices().prepareDelete(HISCOUNT_DATAL2_INDEX).get();
    }


}
