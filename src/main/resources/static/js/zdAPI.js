define(['jquery'], function ($) {

    function zdAPI() {
    }

    // 获取车辆总数
    zdAPI.prototype.fetchTotalCarCount = function () {
        return $.get('/api/getVehicleNum');
    }

    // 获取指标数
    zdAPI.prototype.monthTargetData = function () {
        return $.get('/api/getTarget');
    }

    // 获取最新一条指标数
    zdAPI.prototype.monthLastTargetData = function () {
        return $.get('/api/getLastTarget');
    }

    // 模拟数据
    zdAPI.prototype.fetchTargetObj = function (data,type) {
        var value = new Object();
        if(type==1){
            value.name = "累计行驶里程(km)",
            value.good = data.mileage.normal ;
            value.lo = data.mileage.lowSide ;
            value.hi = data.mileage.highSide ;
            value.na = data.mileage.invalids ;
        }else if(type==2){
            value.name = "满电续航里程(km)",
            value.good = data.limitMileage.normal ;
            value.lo = data.limitMileage.lowSide ;
            value.hi = data.limitMileage.highSide ;
            value.na = data.limitMileage.invalids ;
        }else if(type==3){
            value.name = "一次充满电所用最少时间",
            value.good = data.maxEnergyTime.normal ;
            value.lo = data.maxEnergyTime.lowSide ;
            value.hi = data.maxEnergyTime.highSide ;
            value.na = data.maxEnergyTime.invalids ;
        }else if(type==4){
            value.name = "最大充电功率",
            value.good = data.maxElectricPower.normal ;
            value.lo = data.maxElectricPower.lowSide ;
            value.hi = data.maxElectricPower.highSide ;
            value.na = data.maxElectricPower.invalids ;
        }else if(type==5){
            value.name = "平均单日运行时间",
            value.good = data.avgDailyRunTime.normal ;
            value.lo = data.avgDailyRunTime.lowSide ;
            value.hi = data.avgDailyRunTime.highSide ;
            value.na = data.avgDailyRunTime.invalids ;
        }else if(type==6){
            value.name = "百公里耗电",
            value.good = data.hundredsKmusePower.normal ;
            value.lo = data.hundredsKmusePower.lowSide ;
            value.hi = data.hundredsKmusePower.highSide ;
            value.na = data.hundredsKmusePower.invalids ;
        }
        return value ;
    }


    // 摸拟数据
    zdAPI.prototype.fetchX1History = function () {
        var data = [];

        // 随机产生模拟数据
        for (var i = 0; i < 30; i++) {
            data.push({ tm: new Date(2017, 3, i), v: Math.ceil(Math.random()*100) });;
        }

        return Promise.resolve(data);
    }

    return new zdAPI();
});