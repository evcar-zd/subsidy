"use strict";

var App = (function () {

    function App() {
    }

    App.prototype.config = function () {
        requirejs.config({
            paths: {
                text: "//cdn.jsdelivr.net/requirejs.text/2.0.12/text.min",
                bluebird: "//cdn.jsdelivr.net/bluebird/3.5.0/bluebird.min",
                jquery: "//cdn.jsdelivr.net/jquery/3.2.1/jquery.min",
                bootstrap: "//cdn.jsdelivr.net/bootstrap/3.3.7/js/bootstrap.min",
                vue: "//cdn.jsdelivr.net/vue/2.2.6/vue.min",
                'vue-router': "//cdn.jsdelivr.net/vue.router/2.4.0/vue-router.min",
                d3: "//cdn.jsdelivr.net/d3js/4.8.0/d3.min"
            },
            shim: {
                bootstrap: {
                    deps: ['jquery']
                }
            }
        });
    };

    App.prototype.run = function () {
        this.config();

        // 对于不支持Promise的浏览器填充bluebird
        if (Promise) {
            this.startApp();
        }
        else {
            var _this = this;
            requirejs(['bluebird'], function () {
                _this.startApp();
            });
        }
    };

    App.prototype.startApp = function () {
        requirejs(['jquery', 'bootstrap', 'vue'], function ($, b, Vue) {
            // application
            var app = new Vue({
                data: { title: "知豆纯电动汽车" }
            });

            // global vcx modules
            requirejs([
                '../vcx/zd-car-count',
                '../vcx/zd-stat',
                '../vcx/zd-stat-chart'
            ], function () {
                app.$mount('#app');
                console.info("ready!");
            });
        });
    }

    return App;
})();

var app = new App();
app.run();