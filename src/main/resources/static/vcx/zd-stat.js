define(['vue', 'text!./zd-stat.html', 'zdApi'], function (Vue, template, zdAPI) {

    var VisualModel = (function () {
        function VisualModel() {
            this.stat = [];
            for (var i = 0; i < 6; i++) {
                this.stat.push({
                    name: "loading",
                    good: "",
                    lo: "",
                    hi: "",
                    na: ""
                });
            }
        }

        VisualModel.prototype.fetch = function () {
            var _this = this;

            zdAPI.monthLastTargetData().then(function (data) {
                var mileage = zdAPI.fetchTargetObj(data,1) ;
                Vue.set(_this.stat, 0, mileage);

                var limitMileage = zdAPI.fetchTargetObj(data,2) ;
                Vue.set(_this.stat, 1, limitMileage);

                var maxEnergyTime = zdAPI.fetchTargetObj(data,3) ;
                Vue.set(_this.stat, 2, maxEnergyTime);

                var maxElectricPower = zdAPI.fetchTargetObj(data,4) ;
                Vue.set(_this.stat, 3, maxElectricPower);

                var avgDailyRunTime = zdAPI.fetchTargetObj(data,5) ;
                Vue.set(_this.stat, 4, avgDailyRunTime);

                var hundredsKmusePower = zdAPI.fetchTargetObj(data,6) ;
                Vue.set(_this.stat, 5, hundredsKmusePower);
            }, function (jqXHR, text) {
                _this.star = text ;
            });

//            zdAPI.fetchX1().then(function (value) {
//                Vue.set(_this.stat, 0, value);
//            });
//
//            zdAPI.fetchX2().then(function (value) {
//                Vue.set(_this.stat, 1, value);
//            });
        };

        return VisualModel;
    })();

    var vcx = Vue.component('zd-stat', {
        template,
        data: function () {
            var vm = new VisualModel();
            vm.fetch();
            
            return vm;
        }
    });

    return vcx;
});