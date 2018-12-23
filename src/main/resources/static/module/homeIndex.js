layui.define(["jquery", "laydate"], function (exports) {
    var $jq = layui.jquery,
        laydate = layui.laydate;

    var inBar, biBar, inLine, biLine;

    var obj = {
        /**
         * 页面初始化
         */
        initView: function () {
            layui.language.render("riskIndex");
            var roleLevel = dataStore.get("for_role_level");

            if (roleLevel == 1) {
                $jq("#riskRegion").show();
                $jq("#riskCompany").hide();
                inBar = echarts.init(document.getElementById("inBar"));
                biBar = echarts.init(document.getElementById("biBar"));
                //初始化图表（区域管理员）
                this.initRegionEchart();
                //图表自适应
                this.initPage(inBar, biBar);
            } else {
                $jq("#riskRegion").hide();
                $jq("#riskCompany").show();
                inLine = echarts.init(document.getElementById("inLine"));
                biLine = echarts.init(document.getElementById("biLine"));
                //初始化时间
                this.initDate();
                $jq("#inDate").val(formatDateTime7() + ' - ' + formatDateTime());
                $jq("#biDate").val(formatDateTime7() + ' - ' + formatDateTime());
                //初始化图表（公司管理员）
                this.initCompanyEchart();
                //图表自适应
                this.initPage(inLine, biLine);
            }
        }
        /**
         * 初始化时间
         */
        , initDate: function () {
            laydate.render({
                elem: '#inDate'
                , max: 0
                ,range: '~'
                , done: function (value) {
                    layui.homeIndex.initCompanyInEchart(value)
                }
            });

            laydate.render({
                elem: '#biDate'
                , max: 0
                , range: true
                , done: function (value) {

                }
            });
        }
        /**
         * 初始化图表（公司管理员）
         */
        , initCompanyEchart: function () {
            //获取当前时间戳
            var timestampNow = Date.parse(new Date());
            var timestampSenven = timestampNow - 7 * 24 * 3600 * 1000;
            var nowTime =  this.createTime(timestampNow);
            var senvenTimeBefore =  this.createTime(timestampSenven);

            var initTime = senvenTimeBefore + "~" + nowTime;

            layui.homeIndex.initCompanyInEchart(initTime);//初始化进件走势图表（公司管理员）
            layui.homeIndex.initCompanyBiEchart(initTime);//初始化账单走势图表（公司管理员）
        }
        /**
         * 初始化账单走势图表（公司管理员）
         */
        , initCompanyBiEchart: function (rangeDate) {

            var user_id = dataStore.get("for_edit_Pwd");
            var datas={
                "rangeTime":rangeDate,
                "userId":user_id
            };

            $jq.post("bill/getBillCountByRange", datas, function (data) {
                if (!ajaxCall(data)) {
                    return;
                }
                var xData = [];//横坐标
                var rechargeSeriesData = [];//充值
                var consumeSeriesData = [];//消费
                $jq.each(data.data, function (i, item) {
                    xData.push(item.bill_time);
                    rechargeSeriesData.push(item.recharge_count);
                    consumeSeriesData.push(item.consume_count);
                });
                // biLine.setOption({
                //     color: ['#8375FF', '#00F4B3'],
                //     title: {
                //         text: layui.language.get("bill_moves"),//账单走势
                //         left: '30',
                //         top: '5',
                //         textStyle: {
                //             color: '#333333',
                //             fontSize: '20',
                //             fontWeight: 'bolder'
                //         }
                //     },
                //     legend: {
                //         top: '15',
                //         itemWidth: 15,
                //         itemHeight: 15,
                //         right: '30%',
                //         data: [layui.language.get("recharge"), layui.language.get("consumption")]//充值   消费
                //     },
                //     tooltip: {
                //         trigger: 'axis',
                //         axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                //             type: 'line'        // 默认为直线，可选为：'line' | 'shadow'
                //         }
                //     },
                //     grid: {
                //         left: '3%',
                //         right: '4%',
                //         bottom: '3%',
                //         containLabel: true
                //     },
                //     xAxis: [
                //         {
                //             type: 'category',
                //             data: xData,
                //             axisTick: {
                //                 alignWithLabel: true,
                //                 show: false
                //             },
                //             axisLine: {
                //                 show: false
                //             }
                //         }
                //     ],
                //     yAxis: [
                //         {
                //             type: 'value',
                //             axisTick: {
                //                 show: false
                //             },
                //             axisLine: {
                //                 show: false
                //             },
                //             splitLine: {
                //                 show: true,
                //                 lineStyle: {
                //                     color: ['#eeeeee'],
                //                     opacity: 1
                //                 }
                //             }
                //         }
                //     ],
                //     series: [
                //         {
                //             name: layui.language.get("recharge"),//充值
                //             type: 'bar',
                //             barWidth: '10%',
                //             itemStyle: {
                //                 barBorderRadius: [4, 4, 0, 0],
                //             },
                //             data: rechargeSeriesData
                //         }, {
                //             name: layui.language.get("consumption"),//消费
                //             type: 'bar',
                //             barWidth: '10%',
                //             itemStyle: {
                //                 barBorderRadius: [4, 4, 0, 0],
                //             },
                //             data: consumeSeriesData
                //         }
                //     ]
                // });
                biLine.setOption({
                    color: ['#8375FF', '#00F4B3'],
                    title: {
                        text: layui.language.get("bill_moves"),//账单走势
                        left: '30',
                        top: '5',
                        textStyle: {
                            color: '#333333',
                            fontSize: '20',
                            fontWeight: 'bolder'
                        }
                    },
                    legend: {
                        top: '15',
                        itemWidth: 15,
                        itemHeight: 15,
                        right: '10%',
                        data: [layui.language.get("recharge"), layui.language.get("consumption")]//充值 消费
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'line'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: xData,
                            axisTick: {
                                alignWithLabel: true,
                                show: false
                            },
                            axisLine: {
                                show: false
                            }
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            axisTick: {
                                show: false
                            },
                            axisLine: {
                                show: false
                            },
                            splitLine: {
                                show: true,
                                lineStyle: {
                                    color: ['#eeeeee'],
                                    opacity: 1
                                }
                            }
                        }
                    ],
                    series: [
                        {
                            name: layui.language.get("recharge"),//充值
                            type: 'bar',
                            barWidth: '10%',
                            itemStyle: {
                                barBorderRadius: [4, 4, 0, 0],
                            },
                            data: rechargeSeriesData
                        }, {
                            name: layui.language.get("consumption"),//消费
                            type: 'bar',
                            barWidth: '10%',
                            itemStyle: {
                                barBorderRadius: [4, 4, 0, 0],
                            },
                            data: consumeSeriesData
                        }
                    ]
                });
            });
        }
        /**
         * 初始化进件走势图表（公司管理员）
         */
        , initCompanyInEchart: function (rangeDate) {

            var user_id = dataStore.get("for_edit_Pwd");
            var datas={
                "rangeTime":rangeDate,
                "user_id":user_id
            };


            $jq.post("order/getOrderCountByRange" , datas, function (data) {
                if (!ajaxCall(data)) {
                    return;
                }
                var xData = [];//横坐标
                var seriesData = [];//值
                $jq.each(data.data, function (i, item) {
                    xData.push(item.order_time);
                    seriesData.push(item.order_count);
                });
                inLine.setOption({
                    color: ['#8375FF'],
                    title: {
                        text: layui.language.get("entry_trend"),//进件走势
                        left: '30',
                        top: '5',
                        textStyle: {
                            color: '#333333',
                            fontSize: '20',
                            fontWeight: 'bolder'
                        }
                    },
                    tooltip: {
                        // width: 146px;
                        // height: 76px;
                        // opacity: 1;
                        // background: rgba(255,255,255,1);
                        // box-shadow: 0px 1px 5px 0px rgba(89,134,239,0.31);
                        trigger: 'axis',
                        backgroundColor: 'rgba(255,255,255,1)',
                        boxShadow: '20px 1px 7px 0px red',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'line'        // 默认为直线，可选为：'line' | 'shadow'
                        },
                        textStyle: {
                            color: 'rgba(51,51,51,1)'
                        }
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true,
                    },
                    xAxis: [{
                        type: 'category',
                        boundaryGap: false,
                        data: xData,
                        axisTick: {
                            alignWithLabel: true,
                            show: false
                        },
                        axisLine: {
                            show: false
                        }
                    }],
                    yAxis: [{
                        type: 'value',
                        axisTick: {
                            show: false
                        },
                        axisLine: {
                            show: false
                        },
                        splitLine: {
                            show: true,
                            lineStyle: {
                                color: ['#eeeeee'],
                                opacity: 1
                            }
                        }
                    }],
                    series: [{
                        name: layui.language.get("entry"),//进件
                        type: 'line',
                        smooth: true, //是否平滑曲线显示
                        symbolSize: 0,
                        lineStyle: {
                            normal: {
                                color: "#759CF7"   // 线条颜色
                            }
                        },
                        areaStyle: { //区域填充样式
                            normal: {
                                //线性渐变，前4个参数分别是x0,y0,x2,y2(范围0~1);相当于图形包围盒中的百分比。如果最后一个参数是‘true’，则该四个值是绝对像素位置。
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                                    {offset: 0, color: 'RGB(117,156,247)'},
                                    {offset: 1, color: '#fff'}
                                ], false)
                                // shadowColor: 'rgba(53,142,215, 0.9)', //阴影颜色
                            }
                        },
                        data: seriesData
                    }]
                });
            });
        }
        /**
         * 初始化图表（区域管理员）
         */
        , initRegionEchart: function () {
            var dateType = 1;
            layui.homeIndex.initRegionInEchart(dateType);//初始化进件走势图表（区域管理员）
            layui.homeIndex.initRegionBiEchart(dateType);//初始化账单走势图表（区域管理员）
        }
        /**
         * 初始化账单走势图表（区域管理员）
         */
        , initRegionBiEchart: function (dateType) {
            $jq.post("bill/getBillCountByType/"+ dateType, "", function (data) {
                if (!ajaxCall(data)) {
                    return;
                }
                var xData = [];//横坐标
                var rechargeSeriesData = [];//充值
                var consumeSeriesData = [];//消费
                $jq.each(data.data, function (i, item) {
                    xData.push(item.company_name);
                    rechargeSeriesData.push(item.recharge_amount);
                    consumeSeriesData.push(item.consume_amount);
                });
                biBar.setOption({
                    color: ['#8375FF', '#00F4B3'],
                    title: {
                        text: layui.language.get("bill_moves"),//账单走势
                        left: '30',
                        top: '5',
                        textStyle: {
                            color: '#333333',
                            fontSize: '20',
                            fontWeight: 'bolder'
                        }
                    },
                    legend: {
                        top: '15',
                        itemWidth: 15,
                        itemHeight: 15,
                        right: '10%',
                        data: [layui.language.get("recharge"), layui.language.get("consumption")]//充值 消费
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'line'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: xData,
                            axisTick: {
                                alignWithLabel: true,
                                show: false
                            },
                            axisLine: {
                                show: false
                            }
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            axisTick: {
                                show: false
                            },
                            axisLine: {
                                show: false
                            },
                            splitLine: {
                                show: true,
                                lineStyle: {
                                    color: ['#eeeeee'],
                                    opacity: 1
                                }
                            }
                        }
                    ],
                    series: [
                        {
                            name: layui.language.get("recharge"),//充值
                            type: 'bar',
                            barWidth: '10%',
                            itemStyle: {
                                barBorderRadius: [4, 4, 0, 0],
                            },
                            data: rechargeSeriesData
                        }, {
                            name: layui.language.get("consumption"),//消费
                            type: 'bar',
                            barWidth: '10%',
                            itemStyle: {
                                barBorderRadius: [4, 4, 0, 0],
                            },
                            data: consumeSeriesData
                        }
                    ]
                });
            });
        }
        /**
         * 初始化进件走势图表（区域管理员）
         */
        , initRegionInEchart: function (dateType) {
            $jq.post("order/getOrderCountByType/" + dateType, "", function (data) {
                if (!ajaxCall(data)) {
                    return;
                }
                var xData = [];//横坐标
                var seriesData = [];//值
                $jq.each(data.data, function (i, item) {
                    xData.push(item.company_name);
                    seriesData.push(item.order_count);
                })
                inBar.setOption({
                    color: ['#3877F7'],
                    title: {
                        text: layui.language.get("entry_trend"),//进件走势
                        left: '30',
                        top: '5',
                        textStyle: {
                            color: '#333333',
                            fontSize: '20',
                            fontWeight: 'bolder'
                        }
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'line'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: xData,
                            axisTick: {
                                alignWithLabel: true,
                                show: false
                            },
                            axisLine: {
                                show: false
                            }
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            axisTick: {
                                show: false
                            },
                            axisLine: {
                                show: false
                            },
                            splitLine: {
                                show: true,
                                lineStyle: {
                                    color: ['#eeeeee'],
                                    opacity: 1
                                }
                            }
                        }
                    ],
                    series: [
                        {
                            name: layui.language.get("entry"),//进件
                            type: 'bar',
                            barWidth: '10%',
                            itemStyle: {
                                barBorderRadius: [4, 4, 0, 0],
                            },
                            data: seriesData
                        }
                    ]
                });
            });
        }
        /**
         * 图表自适应
         */
        , initPage: function (inBar, biBar) {
            window.addEventListener('resize', function () {
                inBar.resize();
                biBar.resize();
            });
        }

        /**
         * 近7天的进件走势图表
         */
        ,orderTyepe1: function (obj) {
            $jq("#order_chart .risk-echart-btn").removeClass('chart-button-color');
            $jq(obj).addClass('chart-button-color');
            this.initRegionInEchart(1);
        }

        /**
         * 近14天的进件走势图表
         */
        ,orderTyepe2: function (obj) {
            $jq("#order_chart .risk-echart-btn").removeClass('chart-button-color');
            $jq(obj).addClass('chart-button-color');
            this.initRegionInEchart(2);
        }

        /**
         * 近30天的进件走势图表
         */
        ,orderTyepe3: function (obj) {
            $jq("#order_chart .risk-echart-btn").removeClass('chart-button-color');
            $jq(obj).addClass('chart-button-color');
            this.initRegionInEchart(3);
        }

        /**
         * 近90天的进件走势图表
         */
        ,orderTyepe4: function (obj) {
            $jq("#order_chart .risk-echart-btn").removeClass('chart-button-color');
            $jq(obj).addClass('chart-button-color');
            this.initRegionInEchart(4);
        }

        /**
         * 最近7天的账单走势
         */
        , billType1: function (obj) {
            $jq("#bill_chart .risk-echart-btn").removeClass('chart-button-color');
            $jq(obj).addClass('chart-button-color');
            this.initRegionBiEchart(1)
        }
        /**
         * 最近14天的账单走势
         */
        , billType2: function (obj) {
            $jq("#bill_chart .risk-echart-btn").removeClass('chart-button-color');
            $jq(obj).addClass('chart-button-color');
            this.initRegionBiEchart(2)
        }
        /**
         * 最近30天的账单走势
         */
        , billType3: function (obj) {
            $jq("#bill_chart .risk-echart-btn").removeClass('chart-button-color');
            $jq(obj).addClass('chart-button-color');
            this.initRegionBiEchart(3)
        }
        /**
         * 最近90天的账单走势
         */
        , billType4: function (obj) {
            $jq("#bill_chart .risk-echart-btn").removeClass('chart-button-color');
            $jq(obj).addClass('chart-button-color');
            this.initRegionBiEchart(4)
        }


        //时间戳转化为时间的
        , createTime: function (addTime) {
            var date = new Date(addTime);
            var y = date.getFullYear();
            var m = date.getMonth()+1;
            m = m<10?'0'+m:m;
            var d = date.getDate();
            d = d<10?("0"+d):d;
            // var h = date.getHours();
            // h = h<10?("0"+h):h;
            // var M = date.getMinutes();
            // M = M<10?("0"+M):M;
            var str = y+"-"+m+"-"+d;
            return str;
        }
    }

    exports('homeIndex', obj);

})