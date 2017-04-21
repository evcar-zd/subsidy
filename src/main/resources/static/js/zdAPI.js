define(['jquery'], function ($) {
    function zdAPI() {
    }

    zdAPI.prototype.fetchTotalCarCount = function () {
        return Promise.resolve(2033);
    }

    return new zdAPI();
});