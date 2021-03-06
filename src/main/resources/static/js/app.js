"use strict";

var App = (function () {

    function App() {
        this.cfg = {
           paths: {
               text: "/lib/text/text",
               bluebird: "/lib/bluebird/js.browser/bluebird.min",
               jquery: "/lib/jquery/dist/jquery.min",
               bootstrap: "/lib/bootstrap/dist/js/bootstrap.min",
               vue: "/lib/vue/dist/vue.min",
               'vue-router': "/lib/vue-router/dist/vue-router.min",
               d3: "/lib/d3/d3.min" ,
               zdApi : "./zdAPI",
               page : "./pageService"
           },
           shim: {
               bootstrap: {
                   deps: ['jquery']
               }
           }
        };
    }

    App.prototype.config = function () {
        var _this = this ;
        requirejs.config(_this.cfg);
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
        var _this = this ;
        requirejs(['jquery', 'bootstrap', 'vue'], function ($, b, Vue) {
            // application
            var app = new Vue({
                data: { title: "知豆纯电动汽车" }
            });

            $.get("/api/getOfflineMode").then(function(data){
                if(data){
                    _this.cfg.paths.zdApi = './d521' ;
                    requirejs.config(_this.cfg);
                }
                // global vcx modules
                requirejs([
                    '../vcx/zd-car-count',
                    '../vcx/zd-stat',
                    '../vcx/zd-stat-chart',
                    '../vcx/zd-stat-list'
                ], function () {
                    app.$mount('#app');
                    console.info("ready!");
                });
            })
        });
    }

    return App;
})();

var app = new App();
app.run();