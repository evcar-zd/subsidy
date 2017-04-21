define(['vue', 'text!./zd-car-count.html', '../js/zdAPI'], function (Vue, template, zdAPI) {

    var vcx = Vue.component('zd-car-count', {
        template,
        data: function () {
            var model = {
                total: 0
            };

            zdAPI.fetchTotalCarCount().then(function (count) {
                model.total = count;
            }, function (err) {
                model.total = err;
            });

            return model;
        }
    });

    return vcx;
});