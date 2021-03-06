﻿define(['jquery'], function ($) {

    function zdAPI() {
        this.data = {};
        this.listX = {} ;
    }

    // 获取最新一条指标数
    zdAPI.prototype.monthLastTargetData = function () {
        return $.get('/api/getLastTarget');
    }

    zdAPI.prototype.getVechicleInfo = function (param) {
        return $.get('/api/getVechicleInfo',param);
    }

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

    zdAPI.prototype.fetchHistory = function (data) {
        var model = [];
        for (var i = 0; i < data.length; i++) {
            var date = new Date(data[i].calcTime) ;
            var formatDate = this.dealDatePattern(date) ;
            var value = new Object();
            var vehicleNum = data[i].vehicleNum ;
            value.mileage = data[i].mileage.normal/vehicleNum*100 ;
            value.limitMileage = data[i].limitMileage.normal/vehicleNum*100 ;
            value.maxEnergyTime = data[i].maxEnergyTime.normal/vehicleNum*100 ;
            value.maxElectricPower = data[i].maxElectricPower.normal/vehicleNum*100 ;
            value.avgDailyRunTime = data[i].avgDailyRunTime.normal/vehicleNum*100 ;
            value.hundredsKmusePower = data[i].hundredsKmusePower.normal/vehicleNum*100 ;
            value.vehicleNum = vehicleNum ;
            model.push({ tm: formatDate , v: value });;
        }
        return model;
    }

    zdAPI.prototype.dealDatePattern = function(date){
        date = new Date(date);
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        month = month < 10 ? ('0' + month) : month;
        var day = date.getDate();
        day = day < 10 ? ('0' + day) : day;
        return new Date(2017, month, day) ;
    }

    zdAPI.prototype.fetchHistTarget = function(target){
        var _this = this ;
        if(_this.data[target]){
            return Promise.resolve(_this.data[target]) ;
        }
        return $.get('/api/getTargetParams',{target:target}).then(function(data){
            var model = [] ;
            for(var i in data){
                var value = new Object();
                value.tm = new Date(data[i].calcTime);
                value.v = data[i].targetNum ;
                model.push(value);
            }
            _this.data[target] = model ;
            return model;
        });
    }

    zdAPI.prototype.fetchSegments = function(target){
        var _this = this ;
        if(target == 'totalCount'){
            return Promise.resolve([{ x: 0, y: 0 },{ x: 100, y: 0 }]);
        }
        if(_this.listX[target]){
            return Promise.resolve(_this.listX[target]) ;
        }
        return $.get('/api/getTargetSteps',{target:target}).then(function(data){
            var model = [] ;
            for(var i in data){
                var value = new Object();
                value.x = data[i].MidValue;
                value.y = data[i].Count ;
                model.push(value);
            }
            _this.listX[target] = model ;
            return model;
        });

    }

    zdAPI.prototype.getDetail = function(){
        var target = $("#target").val();
        var mark = $("#mark").val();
        $("#listTitle").text(zdAPI.prototype.getTitle(target,mark));
        $("#totalTarget").hide();
        $("#avgTarget").hide();
        $("#count").hide();
        $("#vehicleInfo").hide();
        $("#vinList").show();
    }

    zdAPI.prototype.getTarget = function(){
        $("#totalTarget").show();
        $("#avgTarget").show();
        $("#count").show();
        $("#vehicleInfo").show();
        $("#vinList").hide();
    }

    zdAPI.prototype.getTitle = function(target,mark){
        //0 近期无数据, 1 正常, -1 无数据
        var canOrGpsMap = {
            '-1' : '无',
            '0' : '近期无',
            '1' : '正常'
        } ;
        //-1 无效, 0 偏低, 1 正常, 2 偏高
        var targetMap = {
            '-1' : '无效' ,
            '0' : '偏低' ,
            '1' : '正常' ,
            '2' : '偏高'
        }
        var title = '' ;
        if(target=='can'){
            title = canOrGpsMap[mark] + 'CAN数据' ;
        }else if(target=='gps'){
            title = canOrGpsMap[mark] + 'GPS数据' ;
        }else if(target=='totalMileage'){    //累计行驶里程(km)
            title = '累计行驶里程-' + targetMap[mark];
        }else if(target=='limitMileage'){    //续驶里程
            title = '续驶里程-' + targetMap[mark];
        }else if(target=='maxEnergyTime'){   //一次充满电所用最少时间
            title = '一次充满电所用最少时间-' + targetMap[mark] ;
        }else if(target=='maxElectricPower'){    //最大充电功率
            title = '最大充电功率-' + targetMap[mark] ;
        }else if(target=='avgDailyRunTime'){     //平均单日运行时间
            title = '平均单日运行时间-' + targetMap[mark] ;
        }else if(target=='hundredsKmusePower'){  //百公里耗电
            title = '百公里耗电-' + targetMap[mark] ;
        }
        return title + '车辆信息';
    }

    return new zdAPI();
});