package com.evcar.subsidy.agg;

import com.evcar.subsidy.entity.ExportTarget;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kong on 2017/5/17.
 */
@Component
public class ExportVehicle {

    /**
     * 获取每辆车的指标
     * @param startDate
     * @param endDate
     * @return
     */
    public List<ExportTarget> getExportTarget(Date startDate, Date endDate){
        List<ExportTarget> exportTargets = new ArrayList<>() ;

        return exportTargets ;
    }



}
