define([], function () {

    function zdAPI() {
        this.json = {
            lastMonthData : {"id":"ZD20170521","tm":1495296000000,"gpsNormal":33418,"gpsNearNoData":2102,"gpsNoData":1337,"canNormal":32191,"canNearNoData":3745,"canNoData":921,"mileage":{"normal":34935,"lowSide":0,"highSide":566,"invalids":1356},"limitMileage":{"normal":6463,"lowSide":6614,"highSide":10295,"invalids":13485},"maxEnergyTime":{"normal":2071,"lowSide":18050,"highSide":97,"invalids":16639},"maxElectricPower":{"normal":23401,"lowSide":3381,"highSide":0,"invalids":10075},"avgDailyRunTime":{"normal":36827,"lowSide":0,"highSide":30,"invalids":0},"hundredsKmusePower":{"normal":9488,"lowSide":6080,"highSide":7804,"invalids":13485},"calcTime":1495619526000,"vehicleNum":36857,"version":"dev-calc.80@ba956cf"},

            totalCountTarget : [{"targetNum":36857,"calcTime":1495296000000},{"targetNum":36760,"calcTime":1495209600000},{"targetNum":36760,"calcTime":1495123200000},{"targetNum":36760,"calcTime":1495036800000},{"targetNum":36760,"calcTime":1494950400000},{"targetNum":36760,"calcTime":1494864000000},{"targetNum":36760,"calcTime":1494777600000},{"targetNum":36760,"calcTime":1494691200000},{"targetNum":36760,"calcTime":1494604800000},{"targetNum":36760,"calcTime":1494518400000},{"targetNum":36760,"calcTime":1494432000000},{"targetNum":36760,"calcTime":1494345600000},{"targetNum":36664,"calcTime":1494259200000},{"targetNum":36664,"calcTime":1494172800000},{"targetNum":36664,"calcTime":1494086400000},{"targetNum":36664,"calcTime":1494000000000},{"targetNum":36664,"calcTime":1493913600000},{"targetNum":36664,"calcTime":1493827200000},{"targetNum":36664,"calcTime":1493740800000},{"targetNum":36664,"calcTime":1493654400000},{"targetNum":36664,"calcTime":1493568000000},{"targetNum":36664,"calcTime":1493481600000},{"targetNum":36664,"calcTime":1493395200000},{"targetNum":36664,"calcTime":1493308800000},{"targetNum":36664,"calcTime":1493222400000},{"targetNum":36664,"calcTime":1493136000000},{"targetNum":36664,"calcTime":1493049600000},{"targetNum":36664,"calcTime":1492963200000},{"targetNum":36664,"calcTime":1492876800000},{"targetNum":36664,"calcTime":1492790400000}] ,
            totalMileageTarget : [{"targetNum":94.79,"calcTime":1495296000000},{"targetNum":94.96,"calcTime":1495209600000},{"targetNum":93.85,"calcTime":1495123200000},{"targetNum":93.29,"calcTime":1495036800000},{"targetNum":93.01,"calcTime":1494950400000},{"targetNum":92.67,"calcTime":1494864000000},{"targetNum":92.09,"calcTime":1494777600000},{"targetNum":92.13,"calcTime":1494691200000},{"targetNum":91.97,"calcTime":1494604800000},{"targetNum":91.49,"calcTime":1494518400000},{"targetNum":90.21,"calcTime":1494432000000},{"targetNum":90.39,"calcTime":1494345600000},{"targetNum":89.86,"calcTime":1494259200000},{"targetNum":89.74,"calcTime":1494172800000},{"targetNum":89.36,"calcTime":1494086400000},{"targetNum":88.61,"calcTime":1494000000000},{"targetNum":88.17,"calcTime":1493913600000},{"targetNum":87.87,"calcTime":1493827200000},{"targetNum":87.56,"calcTime":1493740800000},{"targetNum":87.58,"calcTime":1493654400000},{"targetNum":87.76,"calcTime":1493568000000},{"targetNum":87.70,"calcTime":1493481600000},{"targetNum":87.21,"calcTime":1493395200000},{"targetNum":86.58,"calcTime":1493308800000},{"targetNum":86.01,"calcTime":1493222400000},{"targetNum":85.43,"calcTime":1493136000000},{"targetNum":84.49,"calcTime":1493049600000},{"targetNum":84.31,"calcTime":1492963200000},{"targetNum":84.14,"calcTime":1492876800000},{"targetNum":84.08,"calcTime":1492790400000}] ,
            limitMileageTarget : [{"targetNum":17.54,"calcTime":1495296000000},{"targetNum":17.23,"calcTime":1495209600000},{"targetNum":17.63,"calcTime":1495123200000},{"targetNum":16.85,"calcTime":1495036800000},{"targetNum":16.62,"calcTime":1494950400000},{"targetNum":16.65,"calcTime":1494864000000},{"targetNum":16.59,"calcTime":1494777600000},{"targetNum":16.40,"calcTime":1494691200000},{"targetNum":16.20,"calcTime":1494604800000},{"targetNum":15.98,"calcTime":1494518400000},{"targetNum":16.17,"calcTime":1494432000000},{"targetNum":15.86,"calcTime":1494345600000},{"targetNum":15.87,"calcTime":1494259200000},{"targetNum":15.57,"calcTime":1494172800000},{"targetNum":15.33,"calcTime":1494086400000},{"targetNum":15.25,"calcTime":1494000000000},{"targetNum":15.16,"calcTime":1493913600000},{"targetNum":15.02,"calcTime":1493827200000},{"targetNum":14.83,"calcTime":1493740800000},{"targetNum":14.44,"calcTime":1493654400000},{"targetNum":14.32,"calcTime":1493568000000},{"targetNum":14.25,"calcTime":1493481600000},{"targetNum":14.09,"calcTime":1493395200000},{"targetNum":13.80,"calcTime":1493308800000},{"targetNum":13.38,"calcTime":1493222400000},{"targetNum":12.70,"calcTime":1493136000000},{"targetNum":12.08,"calcTime":1493049600000},{"targetNum":11.15,"calcTime":1492963200000},{"targetNum":10.01,"calcTime":1492876800000},{"targetNum":8.74,"calcTime":1492790400000}] ,
            maxEnergyTimeTarget : [{"targetNum":5.62,"calcTime":1495296000000},{"targetNum":5.36,"calcTime":1495209600000},{"targetNum":5.02,"calcTime":1495123200000},{"targetNum":4.38,"calcTime":1495036800000},{"targetNum":3.85,"calcTime":1494950400000},{"targetNum":3.57,"calcTime":1494864000000},{"targetNum":3.17,"calcTime":1494777600000},{"targetNum":2.71,"calcTime":1494691200000},{"targetNum":2.77,"calcTime":1494604800000},{"targetNum":2.46,"calcTime":1494518400000},{"targetNum":2.18,"calcTime":1494432000000},{"targetNum":1.76,"calcTime":1494345600000},{"targetNum":1.81,"calcTime":1494259200000},{"targetNum":1.57,"calcTime":1494172800000},{"targetNum":1.42,"calcTime":1494086400000},{"targetNum":1.30,"calcTime":1494000000000},{"targetNum":1.27,"calcTime":1493913600000},{"targetNum":1.24,"calcTime":1493827200000},{"targetNum":1.16,"calcTime":1493740800000},{"targetNum":1.06,"calcTime":1493654400000},{"targetNum":1.02,"calcTime":1493568000000},{"targetNum":1.01,"calcTime":1493481600000},{"targetNum":0.92,"calcTime":1493395200000},{"targetNum":0.90,"calcTime":1493308800000},{"targetNum":0.89,"calcTime":1493222400000},{"targetNum":0.79,"calcTime":1493136000000},{"targetNum":0.67,"calcTime":1493049600000},{"targetNum":0.54,"calcTime":1492963200000},{"targetNum":0.42,"calcTime":1492876800000},{"targetNum":0.34,"calcTime":1492790400000}] ,
            maxElectricPowerTarget : [{"targetNum":63.50,"calcTime":1495296000000},{"targetNum":62.66,"calcTime":1495209600000},{"targetNum":61.99,"calcTime":1495123200000},{"targetNum":60.61,"calcTime":1495036800000},{"targetNum":58.88,"calcTime":1494950400000},{"targetNum":59.13,"calcTime":1494864000000},{"targetNum":58.73,"calcTime":1494777600000},{"targetNum":56.76,"calcTime":1494691200000},{"targetNum":55.85,"calcTime":1494604800000},{"targetNum":55.53,"calcTime":1494518400000},{"targetNum":54.57,"calcTime":1494432000000},{"targetNum":54.33,"calcTime":1494345600000},{"targetNum":53.22,"calcTime":1494259200000},{"targetNum":53.68,"calcTime":1494172800000},{"targetNum":52.46,"calcTime":1494086400000},{"targetNum":50.98,"calcTime":1494000000000},{"targetNum":49.75,"calcTime":1493913600000},{"targetNum":49.18,"calcTime":1493827200000},{"targetNum":47.85,"calcTime":1493740800000},{"targetNum":46.85,"calcTime":1493654400000},{"targetNum":46.24,"calcTime":1493568000000},{"targetNum":45.29,"calcTime":1493481600000},{"targetNum":44.47,"calcTime":1493395200000},{"targetNum":43.12,"calcTime":1493308800000},{"targetNum":41.49,"calcTime":1493222400000},{"targetNum":38.32,"calcTime":1493136000000},{"targetNum":35.46,"calcTime":1493049600000},{"targetNum":31.91,"calcTime":1492963200000},{"targetNum":26.51,"calcTime":1492876800000},{"targetNum":20.82,"calcTime":1492790400000}] ,
            avgDailyRunTimeTarget : [{"targetNum":99.92,"calcTime":1495296000000},{"targetNum":99.90,"calcTime":1495209600000},{"targetNum":99.91,"calcTime":1495123200000},{"targetNum":99.92,"calcTime":1495036800000},{"targetNum":99.91,"calcTime":1494950400000},{"targetNum":99.88,"calcTime":1494864000000},{"targetNum":99.92,"calcTime":1494777600000},{"targetNum":99.90,"calcTime":1494691200000},{"targetNum":99.90,"calcTime":1494604800000},{"targetNum":99.91,"calcTime":1494518400000},{"targetNum":99.91,"calcTime":1494432000000},{"targetNum":99.90,"calcTime":1494345600000},{"targetNum":99.90,"calcTime":1494259200000},{"targetNum":99.89,"calcTime":1494172800000},{"targetNum":99.91,"calcTime":1494086400000},{"targetNum":99.89,"calcTime":1494000000000},{"targetNum":99.90,"calcTime":1493913600000},{"targetNum":99.89,"calcTime":1493827200000},{"targetNum":99.88,"calcTime":1493740800000},{"targetNum":99.88,"calcTime":1493654400000},{"targetNum":99.88,"calcTime":1493568000000},{"targetNum":99.88,"calcTime":1493481600000},{"targetNum":99.87,"calcTime":1493395200000},{"targetNum":99.87,"calcTime":1493308800000},{"targetNum":99.88,"calcTime":1493222400000},{"targetNum":99.89,"calcTime":1493136000000},{"targetNum":99.87,"calcTime":1493049600000},{"targetNum":99.88,"calcTime":1492963200000},{"targetNum":99.85,"calcTime":1492876800000},{"targetNum":99.91,"calcTime":1492790400000}],
            hundredsKmusePowerTarget : [{"targetNum":25.75,"calcTime":1495296000000},{"targetNum":25.07,"calcTime":1495209600000},{"targetNum":25.57,"calcTime":1495123200000},{"targetNum":24.68,"calcTime":1495036800000},{"targetNum":24.74,"calcTime":1494950400000},{"targetNum":24.78,"calcTime":1494864000000},{"targetNum":24.55,"calcTime":1494777600000},{"targetNum":24.00,"calcTime":1494691200000},{"targetNum":23.90,"calcTime":1494604800000},{"targetNum":23.48,"calcTime":1494518400000},{"targetNum":23.67,"calcTime":1494432000000},{"targetNum":23.49,"calcTime":1494345600000},{"targetNum":16.83,"calcTime":1494259200000},{"targetNum":17.11,"calcTime":1494172800000},{"targetNum":16.95,"calcTime":1494086400000},{"targetNum":16.44,"calcTime":1494000000000},{"targetNum":16.24,"calcTime":1493913600000},{"targetNum":16.18,"calcTime":1493827200000},{"targetNum":15.79,"calcTime":1493740800000},{"targetNum":15.70,"calcTime":1493654400000},{"targetNum":15.60,"calcTime":1493568000000},{"targetNum":15.24,"calcTime":1493481600000},{"targetNum":15.04,"calcTime":1493395200000},{"targetNum":14.83,"calcTime":1493308800000},{"targetNum":14.78,"calcTime":1493222400000},{"targetNum":14.04,"calcTime":1493136000000},{"targetNum":13.63,"calcTime":1493049600000},{"targetNum":12.70,"calcTime":1492963200000},{"targetNum":11.50,"calcTime":1492876800000},{"targetNum":10.01,"calcTime":1492790400000}],

            totalMileageSteps : [{"MidValue":5.0,"Count":18472},{"MidValue":15.0,"Count":7644},{"MidValue":25.0,"Count":4647},{"MidValue":35.0,"Count":2376},{"MidValue":45.0,"Count":1128},{"MidValue":55.0,"Count":515},{"MidValue":65.0,"Count":342},{"MidValue":75.0,"Count":228},{"MidValue":85.0,"Count":144},{"MidValue":95.0,"Count":109},{"MidValue":105.0,"Count":95},{"MidValue":115.0,"Count":72},{"MidValue":125.0,"Count":85},{"MidValue":135.0,"Count":57},{"MidValue":145.0,"Count":58},{"MidValue":155.0,"Count":60},{"MidValue":165.0,"Count":49},{"MidValue":175.0,"Count":57},{"MidValue":185.0,"Count":48},{"MidValue":195.0,"Count":41},{"MidValue":205.0,"Count":43},{"MidValue":215.0,"Count":36},{"MidValue":225.0,"Count":32},{"MidValue":235.0,"Count":29},{"MidValue":245.0,"Count":32},{"MidValue":255.0,"Count":32},{"MidValue":265.0,"Count":31},{"MidValue":275.0,"Count":22},{"MidValue":285.0,"Count":26},{"MidValue":295.0,"Count":22}] ,
            limitMileageSteps : [{"MidValue":5.0,"Count":13515},{"MidValue":15.0,"Count":11},{"MidValue":25.0,"Count":45},{"MidValue":35.0,"Count":172},{"MidValue":45.0,"Count":358},{"MidValue":55.0,"Count":405},{"MidValue":65.0,"Count":458},{"MidValue":75.0,"Count":470},{"MidValue":85.0,"Count":485},{"MidValue":95.0,"Count":374},{"MidValue":105.0,"Count":351},{"MidValue":115.0,"Count":388},{"MidValue":125.0,"Count":536},{"MidValue":135.0,"Count":907},{"MidValue":145.0,"Count":1494},{"MidValue":155.0,"Count":2169},{"MidValue":165.0,"Count":2854},{"MidValue":175.0,"Count":3230},{"MidValue":185.0,"Count":2978},{"MidValue":195.0,"Count":2606},{"MidValue":205.0,"Count":2091},{"MidValue":215.0,"Count":1442},{"MidValue":225.0,"Count":961},{"MidValue":235.0,"Count":656},{"MidValue":245.0,"Count":341},{"MidValue":255.0,"Count":235},{"MidValue":265.0,"Count":93},{"MidValue":275.0,"Count":73},{"MidValue":285.0,"Count":49},{"MidValue":295.0,"Count":15}] ,
            maxEnergyTimeSteps : [{"MidValue":0.25,"Count":32280},{"MidValue":0.75,"Count":2},{"MidValue":1.25,"Count":4},{"MidValue":1.75,"Count":3},{"MidValue":2.25,"Count":8},{"MidValue":2.75,"Count":21},{"MidValue":3.25,"Count":2006},{"MidValue":3.75,"Count":271},{"MidValue":4.25,"Count":51},{"MidValue":4.75,"Count":27},{"MidValue":5.25,"Count":33},{"MidValue":5.75,"Count":38},{"MidValue":6.25,"Count":58},{"MidValue":6.75,"Count":72},{"MidValue":7.25,"Count":28},{"MidValue":7.75,"Count":219},{"MidValue":8.25,"Count":239},{"MidValue":8.75,"Count":218},{"MidValue":9.25,"Count":92},{"MidValue":9.75,"Count":23},{"MidValue":10.25,"Count":26},{"MidValue":10.75,"Count":0},{"MidValue":11.25,"Count":0},{"MidValue":11.75,"Count":0},{"MidValue":12.25,"Count":0},{"MidValue":12.75,"Count":161},{"MidValue":13.25,"Count":271},{"MidValue":13.75,"Count":395},{"MidValue":14.25,"Count":277},{"MidValue":14.75,"Count":79},{"MidValue":15.25,"Count":19},{"MidValue":15.75,"Count":2}] ,
            maxElectricPowerSteps : [{"MidValue":50.0,"Count":10415},{"MidValue":150.0,"Count":10},{"MidValue":250.0,"Count":13},{"MidValue":350.0,"Count":13},{"MidValue":450.0,"Count":22},{"MidValue":550.0,"Count":24},{"MidValue":650.0,"Count":20},{"MidValue":750.0,"Count":12},{"MidValue":850.0,"Count":267},{"MidValue":950.0,"Count":57},{"MidValue":1050.0,"Count":1431},{"MidValue":1150.0,"Count":183},{"MidValue":1250.0,"Count":1078},{"MidValue":1350.0,"Count":419},{"MidValue":1450.0,"Count":2602},{"MidValue":1550.0,"Count":2831},{"MidValue":1650.0,"Count":901},{"MidValue":1750.0,"Count":16},{"MidValue":1850.0,"Count":10},{"MidValue":1950.0,"Count":18},{"MidValue":2050.0,"Count":84},{"MidValue":2150.0,"Count":205},{"MidValue":2250.0,"Count":1371},{"MidValue":2350.0,"Count":8779},{"MidValue":2450.0,"Count":1795},{"MidValue":2550.0,"Count":487},{"MidValue":2650.0,"Count":541},{"MidValue":2750.0,"Count":173},{"MidValue":2850.0,"Count":435},{"MidValue":2950.0,"Count":2027},{"MidValue":3050.0,"Count":581},{"MidValue":3150.0,"Count":37},{"MidValue":3250.0,"Count":5},{"MidValue":3350.0,"Count":0},{"MidValue":3450.0,"Count":0},{"MidValue":3550.0,"Count":0},{"MidValue":3650.0,"Count":0},{"MidValue":3750.0,"Count":0},{"MidValue":3850.0,"Count":0},{"MidValue":3950.0,"Count":0}] ,
            avgDailyRunTimeSteps : [{"MidValue":0.25,"Count":24094},{"MidValue":0.75,"Count":8405},{"MidValue":1.25,"Count":2715},{"MidValue":1.75,"Count":1024},{"MidValue":2.25,"Count":508},{"MidValue":2.75,"Count":232},{"MidValue":3.25,"Count":92},{"MidValue":3.75,"Count":52},{"MidValue":4.25,"Count":27},{"MidValue":4.75,"Count":10},{"MidValue":5.25,"Count":10},{"MidValue":5.75,"Count":5},{"MidValue":6.25,"Count":4},{"MidValue":6.75,"Count":1},{"MidValue":7.25,"Count":1},{"MidValue":7.75,"Count":2},{"MidValue":8.25,"Count":0},{"MidValue":8.75,"Count":3},{"MidValue":9.25,"Count":1},{"MidValue":9.75,"Count":2}] ,
            hundredsKmusePowerSteps : [{"MidValue":0.5,"Count":13523},{"MidValue":1.5,"Count":12},{"MidValue":2.5,"Count":3},{"MidValue":3.5,"Count":1},{"MidValue":4.5,"Count":8},{"MidValue":5.5,"Count":35},{"MidValue":6.5,"Count":330},{"MidValue":7.5,"Count":1733},{"MidValue":8.5,"Count":3999},{"MidValue":9.5,"Count":5074},{"MidValue":10.5,"Count":4419},{"MidValue":11.5,"Count":2617},{"MidValue":12.5,"Count":1328},{"MidValue":13.5,"Count":668},{"MidValue":14.5,"Count":359},{"MidValue":15.5,"Count":275},{"MidValue":16.5,"Count":178},{"MidValue":17.5,"Count":184},{"MidValue":18.5,"Count":137},{"MidValue":19.5,"Count":146}]
        } ;
        this.data = {};
        this.listX = {} ;
    }

    // 获取最新一条指标数
    zdAPI.prototype.monthLastTargetData = function () {
        var _this = this ;
        return Promise.resolve(_this.json.lastMonthData);
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
        return Promise.resolve(_this.json[target+'Target']).then(function(data){
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
        return Promise.resolve(_this.json[target+'Steps']).then(function(data){
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

    return new zdAPI();
});