define(['vue', 'text!./zd-stat-list.html', 'zdApi','page'], function (Vue, template, zdAPI, pageService) {
    var addZore = function (num) {
        num = +num
        if (num < 10) {
            return '0' + num
        } else {
            return '' + num
        }
    }

    var vcx = Vue.component('zd-stat-list', {
        template,
        data: function () {
            return {
                searchPaginator: {}
            }
        },
        created () {
            this._getData()
        },
        methods : {
            getTarget: function(){
                $("#inputVin").val("");
                this.$root.searchPaginator.page.currentPage = 1 ;
                zdAPI.getTarget();
            },
            _getData: function () {
                var Paginator = function(page,callback){
                    var param = new Object() ;
                    param.tm = $("#tm").val();
                    param.target = $("#target").val();
                    param.mark = $("#mark").val();
                    param.vinCode = $("#inputVin").val();
                    param.currentPage = page.currentPage ;
                    param.pageSize = page.pageSize ;
                    zdAPI.getVechicleInfo(param).then(callback);
                }
                this.$root.searchPaginator = this.searchPaginator = pageService(Paginator, 10);
            },
            getDateByVin : function(){
                this.$root.searchPaginator && this.$root.searchPaginator._load() ;
            }
        },
        filters: {
            dateFormat: function(inp, format){
                var date = new Date(+inp)
                return date.getFullYear() + '-' + addZore(date.getMonth() + 1) + '-' + addZore(date.getDate());
            }
        }
    });

    return vcx;
});