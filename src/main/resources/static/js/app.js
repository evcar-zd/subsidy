"use strict";

requirejs.config({
    paths:{
        text: "//cdn.jsdelivr.net/requirejs.text/2.0.12/text.min",
        jquery: "//cdn.jsdelivr.net/jquery/3.2.1/jquery.min",
        bootstrap: "//cdn.jsdelivr.net/bootstrap/3.3.7/js/bootstrap.min",
        vue: "//cdn.jsdelivr.net/vue/2.2.6/vue.min",
        'vue-router': "//cdn.jsdelivr.net/vue.router/2.4.0/vue-router.min"
    },
    shim:{
        bootstrap: {
            deps: [ 'jquery' ]
        }
    }
});

requirejs(['jquery', 'bootstrap', 'vue', 'vue-router'], function($, b, Vue, VueRouter){
    Vue.use(VueRouter);

    var routes = [
        { path: '/foo', component: { template: '<div>foo</div>'} },
        { path: '/bar', component: { template: '<div>bar</div>'} }
    ];

    var router = new VueRouter({routes});

    var app = new Vue({
        data: { title: "知豆纯电动汽车" },
        router
    });

    app.$mount('#app');

    console.info("ready!");
});