define(['vue', 'text!./zd-stat-chart.html', '../js/zdAPI', 'd3'], function (Vue, template, zdAPI, d3) {
    var VisualModel = (function () {
        function VisualModel() {
            this.id = "d3-" + Math.random().toString().substr(2, 6);
            this.selected = 'totalCount' ;

            this.options = {
                totalCount: '车辆总数' ,
                totalMileage: '累计行驶里程(km)' ,
                limitMileage: '续驶里程' ,
                maxEnergyTime: '一次充满电所用最少时间',
                maxElectricPower: '最大充电功率',
                avgDailyRunTime: '平均单日运行时间',
                hundredsKmusePower: '百公里耗电'
            };
        }
        return VisualModel;
    })();


    var vcx = Vue.component('zd-stat-chart', {
        template,
        data: function () {
            var vm = new VisualModel();
//            vm.fetch();
            return vm;
        },
        methods :{
            changeTarget: function () {
                // this.fetch(this.selected);
                this.fetch(this.selected);
            },
            fetch : function(target){
                var _this = this ;
                if(!target) target = this.selected ;
                zdAPI.fetchHistTarget(target).then(function (data){
                    _this.render(data) ;
                });
            },
            render : function(values){
                var holder = d3.select("#" + this.id);
                var width = parseInt(holder.style("width"))-20;
                var height = width * 0.6;
                var margin = 35;

                var svg = holder.select("svg");
                svg.attr("width", width).attr("height", height);

                svg.selectAll("*").remove();

                var fnScaleX = d3.scaleTime()
                    .domain(d3.extent(values, function (d) { return d.tm; }))
                    .range([margin, width]);

                var fnScaleY = d3.scaleLinear()
                    .domain([0, d3.max(values, function (d) { return d.v*1.2 ; })])
                    .range([height - margin, 0]);

                var axisX = d3.axisBottom(fnScaleX);
                svg.append("g")
                    .attr("transform", "translate(0," + (height - margin) + ")")
                    .call(axisX);

                var axisY = d3.axisLeft(fnScaleY);
                svg.append("g")
                    .attr("transform", "translate(" + margin + ",0)")
                    .call(axisY);

                var fnLine = d3.line()
                    .x(function (d) { return fnScaleX(d.tm); })
                    .y(function (d) { return fnScaleY(d.v); })
                    .curve(d3.curveCardinal);
                svg.append("path").datum(values)
                    .style("stroke-width", "1.5px")
                    .attr("class", "pathClass")
                    .attr("d", fnLine);
            }
        },
        mounted: function(){
            this.fetch(this.selected) ;
        }
    });
});