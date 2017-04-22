define(['vue', 'text!./zd-car-count.html', '../js/zdAPI'], function (Vue, template, zdAPI) {

    var VisualModel = (function () {
        function VisualModel() {
            this.total = "loading...";
        }

        VisualModel.prototype.fetch = function () {
            var _this = this;
            // 所有车辆数目
            zdAPI.fetchTotalCarCount().then(function (count) {
                _this.total = count;
            }, function (jqXHR, text) {
                _this.total = text;
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