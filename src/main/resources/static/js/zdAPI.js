define(['jquery'], function ($) {
    function zdAPI() {
    }

    // 获取车辆总数
    zdAPI.prototype.fetchTotalCarCount = function () {
        return new Promise(function (resolve, reject) {
            $.get('/api/getVehicleNum').done(resolve);
        });
    }

    return new zdAPI();
});