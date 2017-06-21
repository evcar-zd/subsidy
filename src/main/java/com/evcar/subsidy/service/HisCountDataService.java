package com.evcar.subsidy.service;

import com.alibaba.fastjson.JSONObject;
import com.evcar.subsidy.entity.HisCountData;
import com.evcar.subsidy.entity.HisCountDataL2;
import com.evcar.subsidy.util.*;
import com.evcar.subsidy.vo.PageResult;
import com.evcar.subsidy.vo.VehicleVo;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
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
import java.util.Date;
import java.util.List;

import static com.evcar.subsidy.util.Constant.*;

/**
 * Created by Kong on 2017/4/24.
 */
public class HisCountDataService {

    private static Logger s_logger = LoggerFactory.getLogger(HisCountDataService.class);

    /**
     * 新增计算数据
     * @param hisCountData
     * @return
     */
    public static void addHisCountData(HisCountData hisCountData, Client client){
        try {

            String jsonStr = JacksonUtil.toJSon(hisCountData) ;
            JSONObject jsonObject = JSONObject.parseObject(jsonStr) ;
            Date tm = jsonObject.getDate("tm") ;
            Date veDeliveredDate = jsonObject.getDate("veDeliveredDate") ;
            Date releaseTime = jsonObject.getDate("releaseTime") ;
            Date calcTime = jsonObject.getDate("calcTime") ;
            if (tm != null )
                jsonObject.put("tm", DateUtil.dateToStr(tm,DateUtil.DATEFORMATYYYYMMDD)) ;
            if (veDeliveredDate != null )
                jsonObject.put("veDeliveredDate", DateUtil.dateToStr(veDeliveredDate,DateUtil.DATEFORMATYYYYMMDD)) ;
            if (releaseTime != null )
                jsonObject.put("releaseTime", DateUtil.dateToStr(releaseTime,DateUtil.DATEFORMATYYYYMMDD)) ;
            if (calcTime != null )
                jsonObject.put("calcTime", DateUtil.format(calcTime)) ;

            client.prepareIndex(Constant.HISCOUNT_DATA_INDEX, HISCOUNT_DATA_TYPE,hisCountData.getId())
                    .setSource(jsonObject).execute().get();
        } catch (Exception e) {
            s_logger.error("save hisCountData ERROR Connection is closed"+e.getMessage());
            ESTools.connectionError();
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
            ESTools.connectionError();
        }
        return flag ;
    }

//    /**
//     * 删除所有数据
//     * 慎用
//     */
//    public static void deleteByIndex(){
//        Client client = ESTools.getClient() ;
//        IndicesExistsResponse inExistsResponse = client.admin().indices()
//                .exists(new IndicesExistsRequest(Constant.HISCOUNT_DATA_INDEX)).actionGet();
//        if (inExistsResponse.isExists())
//            client.admin().indices().prepareDelete(Constant.HISCOUNT_DATA_INDEX).get();
//    }


    /**
     * 查询计算数据L1
     * @param vinCode
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<HisCountData> getHisCountData(String vinCode ,Date startDate, Date endDate,Client client){
        List<HisCountData> list = new ArrayList<>() ;
        try{
            Long size = getHisCountDataNumber(vinCode,startDate,endDate);
            SortBuilder sortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.matchQuery("vinCode",vinCode))
                    .must(QueryBuilders.rangeQuery("tm")
                            .from(ZonedDateTimeUtil.dateToStr(startDate))
                            .to(ZonedDateTimeUtil.dateToStr(endDate)));
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
        }catch (Exception e){
            s_logger.error("getHisCountData Connection is closed"+e.getMessage());
            ESTools.connectionError();
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
        long size = 0L ;
        try{
            Client client = ESTools.getClient() ;
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.matchQuery("vinCode",vinCode))
                    .must(QueryBuilders.rangeQuery("tm")
                            .from(DateUtil.dateToStr(startDate,DateUtil.DATEFORMATYYYYMMDD))
                            .to(DateUtil.dateToStr(endDate,DateUtil.DATEFORMATYYYYMMDD)));
            SearchRequestBuilder search = client.prepareSearch(Constant.HISCOUNT_DATA_INDEX)
                    .setTypes(Constant.HISCOUNT_DATA_TYPE).setQuery(qb);
            SearchResponse sr = search.get();//得到查询结果
            size = sr.getHits().getTotalHits() ;
        }catch (Exception e){
            s_logger.error("getHisCountDataNumber Connection is closed"+e.getMessage());
            ESTools.connectionError();
        }
        return size ;
    }

    /**
     * 查询计算数据L2
     * @param vinCode
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<HisCountDataL2> getHisCountDataL2(String vinCode ,Date startDate, Date endDate){

        List<HisCountDataL2> list = new ArrayList<>() ;
        try{
            Long size = getHisCountDataNumberL2(vinCode,startDate,endDate);
            Client client = ESTools.getClient() ;
            SortBuilder sortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.matchQuery("vinCode",vinCode))
                    .must(QueryBuilders.rangeQuery("tm")
                            .from(DateUtil.dateToStr(startDate,DateUtil.DATEFORMATYYYYMMDD))
                            .to(DateUtil.dateToStr(endDate,DateUtil.DATEFORMATYYYYMMDD)));
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
        }catch (Exception e){
            s_logger.error("getHisCountDataL2 Connection is closed"+e.getMessage());
            ESTools.connectionError();
        }
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
        long size = 0L ;
        try {
            Client client = ESTools.getClient();
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.matchQuery("vinCode", vinCode))
                    .must(QueryBuilders.rangeQuery("tm")
                            .from(DateUtil.dateToStr(startDate, DateUtil.DATEFORMATYYYYMMDD))
                            .to(DateUtil.dateToStr(endDate, DateUtil.DATEFORMATYYYYMMDD)));
            SearchRequestBuilder search = client.prepareSearch(HISCOUNT_DATAL2_INDEX)
                    .setTypes(HISCOUNT_DATAL2_TYPE).setQuery(qb);
            SearchResponse sr = search.get();//得到查询结果
            size = sr.getHits().getTotalHits() ;
        }catch (Exception e){
            s_logger.error("getHisCountDataNumberL2 Connection is closed"+e.getMessage());
            ESTools.connectionError();
        }
        return size ;
    }

    /**
     * 查询时间段L2的数据
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<HisCountDataL2> getHisCountDataL2(Date startDate, Date endDate,Integer currentPage , Integer pageSize){

        List<HisCountDataL2> list = new ArrayList<>() ;
        try {
            Client client = ESTools.getClient();
            SortBuilder sortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.rangeQuery("tm")
                            .from(DateUtil.dateToStr(startDate, DateUtil.DATEFORMATYYYYMMDD))
                            .to(DateUtil.dateToStr(endDate, DateUtil.DATEFORMATYYYYMMDD)));
            SearchRequestBuilder search = client.prepareSearch(HISCOUNT_DATAL2_INDEX)
                    .setTypes(HISCOUNT_DATAL2_TYPE).setQuery(qb).addSort(sortBuilder)
                    .setFrom((currentPage - 1) * pageSize)
                    .setSize(pageSize);
            SearchResponse sr = search.get();//得到查询结果
            for (SearchHit hits : sr.getHits()) {
                String json = JacksonUtil.toJSon(hits.getSource());
                s_logger.debug(json);
                HisCountDataL2 hisCountData = JacksonUtil.readValue(json, HisCountDataL2.class);
                list.add(hisCountData);
            }
        }catch (Exception e){
            s_logger.error("getHisCountDataL2 Connection is closed"+e.getMessage());
            ESTools.connectionError();
        }
        return list ;
    }

    /**
     * 获取时间段的计算数据条数 L2
     * @param startDate
     * @param endDate
     * @return
     */
    public static Long getHisCountDataNumberL2(Date startDate, Date endDate){
        long size = 0L ;
        try {
            Client client = ESTools.getClient() ;
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.rangeQuery("tm")
                            .from(DateUtil.dateToStr(startDate,DateUtil.DATEFORMATYYYYMMDD))
                            .to(DateUtil.dateToStr(endDate,DateUtil.DATEFORMATYYYYMMDD)));
            SearchRequestBuilder search = client.prepareSearch(HISCOUNT_DATAL2_INDEX)
                    .setTypes(HISCOUNT_DATAL2_TYPE).setQuery(qb);
            SearchResponse sr = search.get();//得到查询结果
            size = sr.getHits().getTotalHits() ;
        }catch (Exception e){
            s_logger.error("getHisCountDataNumberL2 Connection is closed"+e.getMessage());
            ESTools.connectionError();
        }
        return size ;
    }

    /**
     * 获取时间点L2的数据
     * @param vinCode
     * @param date
     * @return
     */
    public static List<HisCountDataL2> getHisCountDataL2(String vinCode ,Date date,Client client){
        List<HisCountDataL2> list = new ArrayList<>() ;
        try {
            SortBuilder sortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.matchQuery("vinCode", vinCode))
                    .must(QueryBuilders.matchQuery("tm", ZonedDateTimeUtil.dateToStr(date)));
            SearchRequestBuilder search = client.prepareSearch(HISCOUNT_DATAL2_INDEX)
                    .setTypes(HISCOUNT_DATAL2_TYPE).setQuery(qb).addSort(sortBuilder)
                    .setFrom(0)
                    .setSize(5);
            SearchResponse sr = search.get();//得到查询结果
            for (SearchHit hits : sr.getHits()) {
                String json = JacksonUtil.toJSon(hits.getSource());
                s_logger.debug(json);
                HisCountDataL2 hisCountData = JacksonUtil.readValue(json, HisCountDataL2.class);
                list.add(hisCountData);
            }
        }catch (Exception e){
            e.printStackTrace();
            s_logger.error("getHisCountDataL2 Connection is closed"+e.getMessage());
            ESTools.connectionError();
        }
        return list ;
    }


    /**
     * 获取最后一天L2数据条数
     * @param startDate
     * @param endDate
     * @return
     */
    public static Long getLastHisCountDataNumberL2(Date startDate, Date endDate){
        long size = 0L ;
        try {
            Client client = ESTools.getClient() ;
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.rangeQuery("tm")
                            .from(DateUtil.dateToStr(startDate,DateUtil.DATEFORMATYYYYMMDD))
                            .to(DateUtil.dateToStr(endDate,DateUtil.DATEFORMATYYYYMMDD)));
            SearchRequestBuilder search = client.prepareSearch(HISCOUNT_DATAL2_INDEX)
                    .setTypes(HISCOUNT_DATAL2_TYPE).setQuery(qb);
            SearchResponse sr = search.get();//得到查询结果
            size = sr.getHits().getTotalHits() ;
        }catch (Exception e){
            s_logger.error("getHisCountDataNumberL2 Connection is closed"+e.getMessage());
            ESTools.connectionError();
        }
        return size ;
    }

    /**
     * 最后一天L2数据
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<HisCountDataL2> getLastHisCountDataL2(Date startDate, Date endDate,Integer currentPage , Integer pageSize){
        List<HisCountDataL2> list = new ArrayList<>() ;
        try {
            Client client = ESTools.getClient();
            SortBuilder sortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.rangeQuery("tm")
                            .from(DateUtil.dateToStr(startDate, DateUtil.DATEFORMATYYYYMMDD))
                            .to(DateUtil.dateToStr(endDate, DateUtil.DATEFORMATYYYYMMDD)));
            SearchRequestBuilder search = client.prepareSearch(HISCOUNT_DATAL2_INDEX)
                    .setTypes(HISCOUNT_DATAL2_TYPE).setQuery(qb).addSort(sortBuilder)
                    .setFrom((currentPage - 1) * pageSize)
                    .setSize(pageSize);
            SearchResponse sr = search.get();//得到查询结果
            for (SearchHit hits : sr.getHits()) {
                String json = JacksonUtil.toJSon(hits.getSource());
                s_logger.debug(json);
                HisCountDataL2 hisCountData = JacksonUtil.readValue(json, HisCountDataL2.class);
                list.add(hisCountData);
            }
        }catch (Exception e){
            s_logger.error("getHisCountDataL2 Connection is closed"+e.getMessage());
            ESTools.connectionError();
        }
        return list ;
    }

    /**
     * 查询计L1是否有CAN数据或者GPS数据
     * @param vinCode
     * @param mark  标志， 0   can,  1 gps
     * @return
     */
    public static long getCanOrGps(String vinCode,int mark,Client client){
        long size = 0L ;
        try {
            String queryFlag = mark == 1 ? "gpsCount" : "canCount";
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.matchQuery("vinCode", vinCode))
                    .must(QueryBuilders.rangeQuery(queryFlag).gt(0));
            SearchRequestBuilder search = client.prepareSearch(HISCOUNT_DATA_INDEX)
                    .setTypes(HISCOUNT_DATA_TYPE).setQuery(qb);
            SearchResponse sr = search.get();//得到查询结果
            size = sr.getHits().getTotalHits() ;
        }catch (Exception e){
            s_logger.error("getCanOrGps Connection is closed"+e.getMessage());
            ESTools.connectionError();
        }

        return size ;
    }

    /**
     * 新增一段时间计算数据
     * @param hisCountData
     * @return
     */
    public static boolean addHisCountDataL2(HisCountDataL2 hisCountData,Client client){
        boolean flag = false ;
        try {
            String jsonStr = JacksonUtil.toJSon(hisCountData) ;
            JSONObject jsonObject = JSONObject.parseObject(jsonStr) ;
            Date tm = jsonObject.getDate("tm") ;
            Date veDeliveredDate = jsonObject.getDate("veDeliveredDate") ;
            Date releaseTime = jsonObject.getDate("releaseTime") ;
            Date calcTime = jsonObject.getDate("calcTime") ;
            if (tm != null )
                jsonObject.put("tm", ZonedDateTimeUtil.dateToStr(tm)) ;
            if (veDeliveredDate != null )
                jsonObject.put("veDeliveredDate", ZonedDateTimeUtil.dateToStr(veDeliveredDate)) ;
            if (releaseTime != null )
                jsonObject.put("releaseTime", ZonedDateTimeUtil.dateToStr(releaseTime)) ;
            if (calcTime != null )
                jsonObject.put("calcTime", ZonedDateTimeUtil.format(calcTime)) ;

            IndexResponse indexResponse = client.prepareIndex(HISCOUNT_DATAL2_INDEX, HISCOUNT_DATAL2_TYPE,hisCountData.getId())
                    .setSource(jsonObject).execute().get();
            flag = indexResponse.isCreated() ;
        } catch (Exception e) {
            s_logger.error("addHisCountDataL2 save hisCountDataL2 ERROR");
            ESTools.connectionError();
        }
        return flag ;
    }

    /**
     * 删除一段时间计算数据
     * @return
     */
    public static boolean deleteHisCountDataL2(String id){
        boolean flag = false ;
        try{
            Client client = ESTools.getClient() ;
            DeleteResponse deleteResponse = client.prepareDelete(HISCOUNT_DATAL2_INDEX, HISCOUNT_DATAL2_TYPE,id).execute().get() ;
            flag = deleteResponse.isFound() ;
        } catch (Exception e) {
            s_logger.error("delete hisCountDataL2 ERROR");
            ESTools.connectionError();
        }
        return flag ;
    }

//    /**
//     * 删除所有数据
//     * 慎用
//     */
//    public static void deleteHisCountDataL2(){
//        Client client = ESTools.getClient() ;
//        IndicesExistsResponse inExistsResponse = client.admin().indices()
//                .exists(new IndicesExistsRequest(HISCOUNT_DATAL2_INDEX)).actionGet();
//        if (inExistsResponse.isExists())
//            client.admin().indices().prepareDelete(HISCOUNT_DATAL2_INDEX).get();
//    }

    public static List<HisCountData> getHisCountDataL1(Date startDate,Date endDate,String vinCode, Client client){
        List<HisCountData> list = new ArrayList<>() ;
        try {
            SortBuilder sortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);
            QueryBuilder qb = new BoolQueryBuilder()
                    .must(QueryBuilders.matchQuery("vinCode", vinCode))
                    .must(QueryBuilders.boolQuery()
                            .should(QueryBuilders.matchQuery("tm", ZonedDateTimeUtil.dateToStr(startDate)))
                            .should(QueryBuilders.matchQuery("tm", ZonedDateTimeUtil.dateToStr(endDate)))
                    );
            SearchRequestBuilder search = client.prepareSearch(Constant.HISCOUNT_DATA_INDEX)
                    .setTypes(Constant.HISCOUNT_DATA_TYPE).setQuery(qb).addSort(sortBuilder)
                    .setFrom(0)
                    .setSize(2);
            SearchResponse sr = search.get();//得到查询结果
            for (SearchHit hits : sr.getHits()) {
                String json = JacksonUtil.toJSon(hits.getSource());
                s_logger.debug(json);
                HisCountData hisCountData = JacksonUtil.readValue(json, HisCountData.class);
                list.add(hisCountData);
            }
        }catch (Exception e){
            s_logger.error("getHisCountDataL1 Connection is closed"+e.getMessage());
            ESTools.connectionError();
        }
        return list ;
    }


    public static void testBulk(List<HisCountData> hisCountDatas) {
        try{
            Client client = ESTools.getClient() ;
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            for (int i = 0 ;i < hisCountDatas.size() ; i++){
                HisCountData hisCountData = hisCountDatas.get(i) ;
                String jsonStr = JacksonUtil.toJSon(hisCountData) ;
                JSONObject jsonObject = JSONObject.parseObject(jsonStr) ;
                Date tm = jsonObject.getDate("tm") ;
                Date veDeliveredDate = jsonObject.getDate("veDeliveredDate") ;
                Date releaseTime = jsonObject.getDate("releaseTime") ;
                Date calcTime = jsonObject.getDate("calcTime") ;
                if (tm != null )
                    jsonObject.put("tm", DateUtil.dateToStr(tm,DateUtil.DATEFORMATYYYYMMDD)) ;
                if (veDeliveredDate != null )
                    jsonObject.put("veDeliveredDate", DateUtil.dateToStr(veDeliveredDate,DateUtil.DATEFORMATYYYYMMDD)) ;
                if (releaseTime != null )
                    jsonObject.put("releaseTime", DateUtil.dateToStr(releaseTime,DateUtil.DATEFORMATYYYYMMDD)) ;
                if (calcTime != null )
                    jsonObject.put("calcTime", DateUtil.format(calcTime)) ;
                bulkRequest.add(client.prepareIndex(HISCOUNT_DATAL2_INDEX, HISCOUNT_DATAL2_TYPE, hisCountData.getId())
                        .setSource(jsonObject));
            }
            BulkResponse response = bulkRequest.get();
        }catch (Exception e){
            s_logger.error("Connection is closed"+e.getMessage());
            ESTools.connectionError();
        }
    }

    /**
     *
     * 获取l2数量
     */
    public static long getVehicleL2Num(){
        Client client = ESTools.getClient() ;
        SearchRequestBuilder search = client.prepareSearch(Constant.HISCOUNT_DATAL2_INDEX).setTypes(Constant.HISCOUNT_DATAL2_TYPE) ;
        SearchResponse sr = search.get();//得到查询结果
        return sr.getHits().getTotalHits();//读取数量
    }


    /**
     *
     * 获取l2详细信息
     */
    public static List<HisCountDataL2> getVehicleL2(Date startday,Date endDate,String vinCode,Client client){
       // Client client = ESTools.getClient() ;
        List<HisCountDataL2> list = new ArrayList<>() ;
        QueryBuilder qb = new BoolQueryBuilder()
                .must(QueryBuilders.matchQuery("vinCode",vinCode))
                .must(QueryBuilders.rangeQuery("tm")
                        .from(ZonedDateTimeUtil.dateToStr(startday))
                        .to(ZonedDateTimeUtil.dateToStr(endDate)));
        SearchRequestBuilder search = client.prepareSearch(Constant.HISCOUNT_DATAL2_INDEX).setTypes(Constant.HISCOUNT_DATAL2_TYPE).setQuery(qb) ;
        SearchResponse sr = search.get();//得到查询结果
        long sizeNum = sr.getHits().getTotalHits();//读取数量

        SortBuilder sortBuilder = SortBuilders.fieldSort("id").order(SortOrder.DESC);
        search = search.addSort(sortBuilder).setFrom(0).setSize((int)sizeNum);
        sr = search.get();//得到查询结果

        for(SearchHit hits:sr.getHits()){
            String json = JacksonUtil.toJSon(hits.getSource());
            HisCountDataL2 hisCountData = JacksonUtil.readValue(json, HisCountDataL2.class);
            list.add(hisCountData);
        }
        s_logger.info("fetched {}HisCountData", list.size());
        return list ;
    }

    /**
     * 指标数据
     * @param tm
     * @return
     */
    public static PageResult getLastHisCountDataL2(Date tm, String target, Integer mark,String vinCode, Integer currentPage , Integer pageSize){

        List<HisCountDataL2> list = new ArrayList<>() ;
        long count = 0 ;
        try {
            Client client = ESTools.getClient();
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder().must(QueryBuilders.matchQuery("tm",ZonedDateTimeUtil.dateToStr(tm)));
            if (!StringUtil.isEmpty(vinCode)){
                boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.matchQuery("vinCode",vinCode)) ;
            }
            QueryBuilder qb = null;
            if("can".equals(target)){
                qb = boolQueryBuilder.must(QueryBuilders.matchQuery("canMark",mark));
            }else if("gps".equals(target)){
                qb = boolQueryBuilder.must(QueryBuilders.matchQuery("gpsMark",mark));
            }else if("totalMileage".equals(target)){    //累计行驶里程(km)
                qb = boolQueryBuilder.must(QueryBuilders.matchQuery("mileageMark",mark));
            }else if("limitMileage".equals(target)){    //续驶里程
                qb = boolQueryBuilder.must(QueryBuilders.matchQuery("limitMileageMark",mark));
            }else if("maxEnergyTime".equals(target)){   //一次充满电所用最少时间
                qb = boolQueryBuilder.must(QueryBuilders.matchQuery("maxEnergyTimeMark",mark));
            }else if("maxElectricPower".equals(target)){    //最大充电功率
                qb = boolQueryBuilder.must(QueryBuilders.matchQuery("maxElectricPowerMark",mark));
            }else if("avgDailyRunTime".equals(target)){     //平均单日运行时间
                qb = boolQueryBuilder.must(QueryBuilders.matchQuery("avgDailyRunTimeMark",mark));
            }else if("hundredsKmusePower".equals(target)){  //百公里耗电
                qb = boolQueryBuilder.must(QueryBuilders.matchQuery("hundredsKmusePowerMark",mark));
            }


            SearchRequestBuilder search = client.prepareSearch(HISCOUNT_DATAL2_INDEX)
                    .setTypes(HISCOUNT_DATAL2_TYPE).setQuery(qb)
                    .setFrom((currentPage - 1) * pageSize)
                    .setSize(pageSize);
            SearchResponse sr = search.get();//得到查询结果
            count = sr.getHits().getTotalHits() ;
            for (SearchHit hits : sr.getHits()) {
                String json = JacksonUtil.toJSon(hits.getSource());
                s_logger.debug(json);
                HisCountDataL2 hisCountData = JacksonUtil.readValue(json, HisCountDataL2.class);
                list.add(hisCountData);
            }
        }catch (Exception e){
            s_logger.error("getHisCountDataL2 Connection is closed"+e.getMessage());
            ESTools.connectionError();
        }

        List<VehicleVo> vehicleVos = OrganizationUtil.getVehicleVo(list) ;

        PageResult pageResult = new PageResult(vehicleVos,currentPage,count) ;

        return pageResult ;
    }
}
