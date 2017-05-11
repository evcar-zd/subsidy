package com.evcar.subsidy.service;

import com.evcar.subsidy.util.Constant;
import com.evcar.subsidy.util.DateUtil;
import com.evcar.subsidy.util.ESTools;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

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
        return sr.getHits().getTotalHits();//读取数量
    }
}
