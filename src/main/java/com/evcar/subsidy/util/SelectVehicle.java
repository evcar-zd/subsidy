package com.evcar.subsidy.util;


import com.evcar.subsidy.entity.Vehicle;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kong on 2017/4/19.
 */
public class SelectVehicle{
    private static Logger s_logger = LoggerFactory.getLogger(SelectVehicle.class);


    public static List<Vehicle> getVehicleList(Client client) {

        List<Vehicle> list = new ArrayList<>() ;

        SearchRequestBuilder search = client.prepareSearch(Constant.VEHICLE_INDEX).setTypes(Constant.VEHICLE_TYPE) ;
        SearchResponse r = search.get();//得到查询结果
        for(SearchHit hits:r.getHits()){
            //只能获取addFields里面添加的字段
            //	System.out.println(hits.getFields().get("userId").getValue());
            //默认可会source里面获取所需字段
            String json = JacksonUtil.toJSon(hits.getSource()) ;
            s_logger.debug(json);
            Vehicle vehicle = JacksonUtil.readValue(json, Vehicle.class);
            list.add(vehicle) ;
            //注意不支持data.subjectName这样的访问方式
            //System.out.println(hits.getId()+""+hits.score()+""+data.get("subjectName"));
            //如果是个嵌套json，需要转成map后，访问其属性
            //	Map data=(Map) hits.getSource().get("data");
            //	System.out.println(hits.getId()+""+hits.score()+""+data.get("subjectName"));	}
//            long hitsnum =r.getHits().getTotalHits();//读取命中数量
//            System.out.println(hitsnum);
        }

        s_logger.info("fetched {} vehicles", list.size());

        return list ;
    }

}
