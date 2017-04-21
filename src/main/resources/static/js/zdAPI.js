define(['jquery'], function ($) {
    function zdAPI() {
    }

    // 获取车辆总数
    zdAPI.prototype.fetchTotalCarCount = function () {
        return $.get('/api/getVehicleNum');
    }

    return new zdAPI();
});