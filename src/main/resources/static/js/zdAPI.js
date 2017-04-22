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

    // 摸拟数据
    zdAPI.prototype.fetchX1History = function () {
        var data = [];

        // 随机产生模拟数据
        for (var i = 0; i < 30; i++) {
            data.push({ tm: new Date(2017, 3, i), v: Math.ceil(Math.random()*100) });;
        }

        return Promise.resolve(data);
    }

    return new zdAPI();
});