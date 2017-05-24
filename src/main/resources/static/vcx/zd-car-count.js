define(['vue', 'text!./zd-car-count.html', 'zdApi'], function (Vue, template, zdAPI) {

    var VisualModel = (function () {
        function VisualModel() {
            this.total = "loading...";
            this.canNormal = "loading..." ;
            this.canNearNoData = "loading..." ;
            this.canNoData = "loading..." ;
            this.gpsNormal = "loading..." ;
            this.gpsNearNoData = "loading..." ;
            this.gpsNoData = "loading..." ;
        }

        VisualModel.prototype.fetch = function () {
            var _this = this;
            zdAPI.monthLastTargetData().then(function (data) {
                _this.canNormal = data.canNormal ;
                _this.canNearNoData = data.canNearNoData ;
                _this.canNoData = data.canNoData ;

                _this.gpsNormal = data.gpsNormal ;
                _this.gpsNearNoData = data.gpsNearNoData ;
                _this.gpsNoData = data.gpsNoData ;

                _this.total = data.vehicleNum ;
            }, function (jqXHR, text) {
            });

        };

        return VisualModel;
    })();

    var vcx = Vue.component('zd-car-count', {
        template,
        data: function () {
            var vm = new VisualModel();
            vm.fetch();
            return vm;
        }
    });

    return vcx;
});