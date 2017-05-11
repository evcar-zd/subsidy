package com.evcar.subsidy.service;

import com.evcar.subsidy.entity.BmsData;
import com.evcar.subsidy.entity.ObcData;
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
public class ObcDataService {

    private static Logger s_logger = LoggerFactory.getLogger(ObcDataService.class);

    /**
     * 查询历史OBC数据NUM
     * @return
     */
    public static Long getHisObcDataNum(String vinCode, Date startDate, Date endDate){

        Client client = ESTools.getClient() ;
        QueryBuilder qb = new BoolQueryBuilder()
                .must(QueryBuilders.matchQuery("vinCode",vinCode))
                .must(QueryBuilders.rangeQuery("collectTime")
                        .from(DateUtil.format(startDate))
                        .to(DateUtil.format(endDate)));
        SearchRequestBuilder search = client.prepareSearch(Constant.HIS_OBC_INDEX).
                setTypes(Constant.HIS_OBC_TYPE).setQuery(qb);
        SearchResponse sr = search.get();//得到查询结果
        return sr.getHits().getTotalHits();//读取数量
    }

    /**
     * 查询历史OBC数据
     * @return
     */
    public static List<ObcData> getHisObcData(String vinCode, Date startDate, Date endDate, long sizeNum){
        Client client = ESTools.getClient() ;
        List<ObcData> list = new ArrayList<>() ;
        SortBuilder sortBuilder = SortBuilders.fieldSort("collectTime").order(SortOrder.ASC);
        QueryBuilder qb = new BoolQueryBuilder()
                .must(QueryBuilders.matchQuery("vinCode",vinCode))
                .must(QueryBuilders.rangeQuery("collectTime")
                        .from(DateUtil.format(startDate))
                        .to(DateUtil.format(endDate)));
        SearchRequestBuilder search = client.prepareSearch(Constant.HIS_OBC_INDEX).
                setTypes(Constant.HIS_OBC_TYPE)
                .addSort(sortBuilder)
                .setQuery(qb)
                .setFrom(0)
                .setSize((int)sizeNum);

        SearchResponse sr = search.get();//得到查询结果
        for(SearchHit hits:sr.getHits()){
            String json = JacksonUtil.toJSon(hits.getSource()) ;
            s_logger.debug(json);
            ObcData obcData = JacksonUtil.readValue(json, ObcData.class);
            list.add(obcData) ;
        }
        s_logger.info("fetched {} hisObcData", list.size());
        return list ;
    }
}
