define(['vue', 'text!./zd-stat.html', '../js/zdAPI'], function (Vue, template, zdAPI) {

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

            zdAPI.fetchX1().then(function (value) {
                Vue.set(_this.stat, 0, value);
            });

            zdAPI.fetchX2().then(function (value) {
                Vue.set(_this.stat, 1, value);
            });
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