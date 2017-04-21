define(['jquery'], function ($) {
    function zdAPI() {
    }

    // 获取车辆总数
    zdAPI.prototype.fetchTotalCarCount = function () {
        return $.get('/api/getVehicleNum');
    }

    // 模拟数据
    zdAPI.prototype.fetchX1 = function () {
        return Promise.resolve({
            name: "累计行驶里程(km)",
            good: 99999,
            lo: 99,
            hi: 99,
            na: 99
        });
    }

    // 模拟数据
    zdAPI.prototype.fetchX2 = function () {
        return Promise.resolve({
            name: "满电续航里程(km)",
            good: 99999,
            lo: 99,
            hi: 99,
            na: 99
        });
    }

    return new zdAPI();
});