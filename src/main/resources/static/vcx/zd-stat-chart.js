define(['vue', 'text!./zd-stat-chart.html', '../js/zdAPI', 'd3'], function (Vue, template, zdAPI, d3) {
    var VisualModel = (function () {
        function VisualModel() {
            this.id = "d3-" + Math.random().toString().substr(2, 6);
            this.selected = '1' ;
            this.options = [
                    { text: '累计行驶里程(km)', value: '1' },
                    { text: '续驶里程', value: '2' },
                    { text: '一次充满电所用最少时间', value: '3' },
                    { text: '最大充电功率', value: '4' },
                    { text: '平均单日运行时间', value: '5' },
                    { text: '百公里耗电', value: '6' }] ;
            this.methods ={
                changeStyle: function () {
                    var _value = document.getElementById("target").value ;

                    var _path = document.getElementsByClassName("pathClass");
                    for(var i=0;i<_path.length;i++){
                        if((_value-1) == i){
                            _path[i].style.display = "block";
                        }else{
                            _path[i].style.display = "none";
                        }
                    }
                }
            }
        }

        VisualModel.prototype.fetch = function () {
            var _this = this;

//            zdAPI.fetchX1History().then(function (values) {
//                _this.render(values);
//            });
            zdAPI.monthTargetData().then(function (data){
                var values = zdAPI.fetchHistory(data) ;
                _this.render(values) ;
            });

        };

        new Vue({
            el: '#change',
            data: {
                selected: null,
                options: []
            }
        })

        VisualModel.prototype.render = function (values) {
            var holder = d3.select("#" + this.id);

            var width = parseInt(holder.style("width"));
            var height = width * 0.6;
            var margin = 25;

            var svg = holder.select("svg");
            svg.attr("width", width).attr("height", height);

            var fnScaleX = d3.scaleTime()
                .domain(d3.extent(values, function (d) { return d.tm; }))
                .range([margin, width]);

            var fnScaleY = d3.scaleLinear()
                .domain([0, 105])
                .range([height - margin, 0]);

            var axisX = d3.axisBottom(fnScaleX);
            svg.append("g")
                .attr("transform", "translate(0," + (height - margin) + ")")
                .call(axisX);

            var axisY = d3.axisLeft(fnScaleY);
            svg.append("g")
                .attr("transform", "translate(" + margin + ",0)")
                .call(axisY);

            /** 1.车辆累积行驶里程 */
            var fnLine1 = d3.line()
                .x(function (d) { return fnScaleX(d.tm); })
                .y(function (d) { return fnScaleY(d.v.mileage); })
                .curve(d3.curveCardinal);
            svg.append("path").datum(values)
                .style("stroke-width", "1.5px")
                .attr("id","xxx")
                .attr("class", "pathClass")
                .attr("d", fnLine1);
            /** 2.续驶里程 */
            var fnLine2 = d3.line()
                .x(function (d) { return fnScaleX(d.tm); })
                .y(function (d) { return fnScaleY(d.v.limitMileage); })
                .curve(d3.curveCardinal);
            svg.append("path").datum(values)
                .style("stroke-width", "1.5px")
                .style("display", "none")
                .attr("class", "pathClass")
                .attr("d", fnLine2);
            /** 3.一次充满电所用最少时间 */
            var fnLine3 = d3.line()
                .x(function (d) { return fnScaleX(d.tm); })
                .y(function (d) { return fnScaleY(d.v.maxEnergyTime); })
                .curve(d3.curveCardinal);
            svg.append("path").datum(values)
                .style("stroke-width", "1.5px")
                .style("display", "none")
                .attr("class", "pathClass")
                .attr("d", fnLine3);
            /** 4.最大充电功率 */
            var fnLine4 = d3.line()
                .x(function (d) { return fnScaleX(d.tm); })
                .y(function (d) { return fnScaleY(d.v.maxElectricPower); })
                .curve(d3.curveCardinal);
            svg.append("path").datum(values)
                .style("stroke-width", "1.5px")
                .style("display", "none")
                .attr("class", "pathClass")
                .attr("d", fnLine4);
            /** 5.平均单日运行时间 */
            var fnLine5 = d3.line()
                .x(function (d) { return fnScaleX(d.tm); })
                .y(function (d) { return fnScaleY(d.v.avgDailyRunTime); })
                .curve(d3.curveCardinal);
            svg.append("path").datum(values)
                .style("stroke-width", "1.5px")
                .style("display", "none")
                .attr("class", "pathClass")
                .attr("d", fnLine5);
            /** 6.百公里耗电 */
            var fnLine6 = d3.line()
                .x(function (d) { return fnScaleX(d.tm); })
                .y(function (d) { return fnScaleY(d.v.hundredsKmusePower); })
                .curve(d3.curveCardinal);
            svg.append("path").datum(values)
                .style("stroke-width", "1.5px")
                .style("display", "none")
                .attr("class", "pathClass")
                .attr("d", fnLine6);
        };

        return VisualModel;
    })();


    var vcx = Vue.component('zd-stat-chart', {
        template,
        data: function () {
            var vm = new VisualModel();
            vm.fetch();
            return vm;
        }
    });
});