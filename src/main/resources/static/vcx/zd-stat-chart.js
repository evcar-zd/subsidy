define(['vue', 'text!./zd-stat-chart.html', '../js/zdAPI', 'd3'], function (Vue, template, zdAPI, d3) {
    var VisualModel = (function () {
        function VisualModel() {
            this.id = "d3-" + Math.random().toString().substr(2, 6);
        }

        VisualModel.prototype.fetch = function () {
            var _this = this;

            zdAPI.fetchX1History().then(function (values) {
                _this.render(values);
            });

        };

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

            var fnLine = d3.line()
                .x(function (d) { return fnScaleX(d.tm); })
                .y(function (d) { return fnScaleY(d.v); })
                .curve(d3.curveCardinal);

            svg.append("path").datum(values)
                .style("stroke-width", "1.5px")
                .attr("d", fnLine);
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