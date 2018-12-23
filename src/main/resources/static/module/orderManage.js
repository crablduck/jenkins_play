layui.define(["jquery","form","table","laytpl","laydate", "element"],function (exports) {
    var $jq = layui.jquery,
        form = layui.form,
        laytpl = layui.laytpl,
        table = layui.table;
        lan = layui.language
        laydate = layui.laydate;
        element = layui.element;


    var obj = {
        /**
         * 页面初始化
         */
        initView : function(){

            lan.render("orderManageLan")
            //初始化下拉框
            this.initRole();
            laydate.render({
                elem: '#orderDate'
                ,range: true
            });

            form.render();//表单渲染
            // //初始化表格
            this.initTable();
        }

        /**
         * 初始化角色
         */
        ,initRole : function () {
            var roleLevel = dataStore.get("for_role_level");
            if(roleLevel == 1){
                // $jq("#company_name").show();
                // $jq("#application_name").show();
                //初始化下拉框
                layui.orderManage.initSelectCompany();
                layui.orderManage.initSelectCommo();
            }else if (roleLevel == 2) {
                $jq("#companySele").hide();
                layui.orderManage.initSelectCommo();
            }else if (roleLevel == 3) {
                $jq("#companySele").hide();
                $jq("#appSele").hide();
                //初始化下拉框
            }
        }

        /**
         * 初始化下拉框
         */
        ,initSelectCompany : function () {
            $jq.post("company/getCompanyList","",function (data) {
                var companyHtml = '<option value="" selected disabled>'+ lan.get("company_name")+'</option>';//公司
                $jq.each(data.data,function (i,item) {
                    companyHtml += '<option value="' + item.companyId + '">' + item.companyName + '</option>';
                });
                $jq("#company_name").html(companyHtml);
                form.render('select');
            });
        }

        , initSelectCommo : function () {
            $jq.post("app/getCommodityList","",function (data) {
                var appHtml = '<option value="" selected disabled>'+ lan.get("app_name")+'</option>';//应用
                $jq.each(data.data,function (i,item) {
                    appHtml += '<option value="' + item.commodityNumber + '">' + item.commodityName + '</option>';
                });
                $jq("#application_name").html(appHtml);
                form.render('select');
            });
        }

        /**
         * 初始化表格
         */
        ,initTable : function () {

            var roleLevel = dataStore.get("for_role_level");
            var userId= dataStore.get("for_edit_Pwd");
            var orderDate = $jq("#orderDate").val();
            table.render({
                elem: "#orderManageTable"
                //本来下面三个属性解决跨域问题， 但是因为异步访问导致访问问题

                ,time: 1000
                ,url: 'order/getOrderList/'+roleLevel//数据请求路径
                ,page: true
                ,loading: true
                ,toolbar: '#toolbarOrder'
                ,where: {
                    acount: $jq("#account_name").val()
                    ,company_name: $jq("#company_name").val()
                    ,rangeTime: $jq("#orderDate").val()
                    ,user_id: userId
                }
                ,cols: [[
                    ,{type: 'checkbox', fixed: 'left'}
                    ,{field: 'order_id', title:  lan.get("order_id")}
                    ,{field: 'order_no', title:  lan.get("order_no")}
                    ,{field: 'user_name', title:  lan.get("user_name"), width:100}
                    ,{field:  'company_name', title: lan.get("company_name")}
                    ,{field:  'app_name', title: lan.get("application_name")}
                    ,{field:  'user_real_name', title:  lan.get("user_real_name")}
                    ,{field:  'phone', title:  lan.get("phone")}
                    ,{field:  'money', title: lan.get("money")}
                    ,{field:  'limit_date', title:  lan.get("limit_date")}
                    ,{field:  'gps_location', title:  lan.get("gps_location")}
                    ,{field:  'add_time', title:  lan.get("add_time"),
                        templet :function (row){
                            return layui.orderManage.createTime(row.add_time);
                        }}
                    ,{fixed: 'right', title:  lan.get("order_status"), align: 'center', width:100, templet: function (d) {
                            if ( d.order_status == 1){
                                return lan.get("pass")
                            }else {
                                return lan.get("refuse")
                            }
                        }}
                    ,{fixed: 'right', title: lan.get("operation"), align: 'center', width:250, templet: function (d) {
                            return '<button  class="layui-btn layui-btn-sm layui-btn-normal" onclick="layui.orderManage.orderDetail('+d.order_id+')">'+lan.get("order_detail")+'</button>'

                        }}
                ]]
                ,request: {
                    pageName: 'current',
                    limitName: 'size'
                }
                ,limit: 10
                ,limits: [10, 20, 30, 40, 50]
            });

        }

        //时间戳转化为时间的
        , createTime: function (addTime) {
            var date = new Date(addTime);
            var y = date.getFullYear();
            var m = date.getMonth()+1;
            m = m<10?'0'+m:m;
            var d = date.getDate();
            d = d<10?("0"+d):d;
            var h = date.getHours();
            h = h<10?("0"+h):h;
            var M = date.getMinutes();
            M = M<10?("0"+M):M;
            var str = y+"-"+m+"-"+d+" "+h+":"+M;
            return str;
        }

        ,orderDetail : function (orderId) {

            $jq.post("order/getOrderById/"+orderId,"",function(data){
                var editOrder = data;

                console.log(data)
                editOrder["title"] = lan.get("order_detail");
                var html = orderDetail.innerHTML;
                var htmlStr = laytpl(html).render(editOrder);
                layer.open({
                    type: 1,
                    area: '700px',
                    title: [editOrder.title, 'text-align:center;font-size:18px;'],
                    content: htmlStr
                });

                var des = editOrder.data.description_array;
                debugger
                if (des != null) {
                    var deses = des.split(",");
                    var data = [];
                    for (var i = 0; i < deses.length; i++){
                        var json = {};
                        json.desc = deses[i];
                        data.push(json);
                    }

                    table.render({
                        elem: "#order_result"
                        ,data: data                    ,autoHeight: true
                        ,cols: [[
                            {field: 'desc', title:  lan.get("result_description")}
                        ]]
                    })
                }

                //这个是图谱链用的
                var roleLevel = dataStore.get("for_role_level");
                if(roleLevel == 1){
                    var des = editOrder.data.base_description_array;
                    if (des != null) {
                        var deses = des.split(",");
                        var data = [];
                        for (var i = 0; i < deses.length; i++){
                            var json = {};
                            json.desc = deses[i];
                            data.push(json);
                        }

                        table.render({
                            elem: "#order_base_result"
                            ,data: data
                            ,autoHeight: true
                            ,cols: [[
                                {field: 'desc', title:  lan.get("result_base_description")}
                            ]]
                        })
                    }

                    var result_base_description = "<div>图谱链校验通过</div>"
                    $jq("order_base_result").html(result_base_description);
                }else if (roleLevel == 2) {
                    debugger
                    $jq("#baseResult").hide();
                }else if (roleLevel == 3) {
                    $jq("#baseResult").hide();
                }

                lan.render("order_detail")


                // var des = editOrder.data.description_array;
                // var deses = des.split(",");
                //
                //
                // var tableOrder = "<table class=\"layui-table\">";
                // for (var i = 0; i < deses.length; i++){
                //     tableOrder += "<td>"+deses[i]+"</td>";
                // }
                // tableOrder += '</table >';
                // $jq("order_result").html(tableOrder);

            })

        }

        ,showoutDetail: function (contents, editBill) {

        }

        ,toJson : function(arr) {
            var jsonStr = "{";
            for (var i = 0; i < arr.length; i++){
                if (i == arr.length-1){
                    jsonStr += "des:"+arr[i]
                } else {
                    jsonStr += "des:"+arr[i]+","
                }
            }
            jsonStr += "}"
        }

        /**
         * 监听事件
         */
        ,tool : function () {
            //表单监听提交事件
            form.on("submit(addOrUpdateBillForm)",function(data){
                console.log(data.field);
                $jq.post("http://localhost:8005/bill/addRecharge",data.field,function(data){
                    if(!ajaxCall(data)){
                        return;
                    }
                    layer.msg(lan.get("operate_success"),{icon:1,time:1000},function(){
                        layer.closeAll();
                        layui.billManage.initView();
                    });
                });
                return false;
            });

            //表格监听事件
            table.on("tool(orderManageEvent)",function(obj){
                var data = obj.data;//获取一行的数据
                //编辑
                if(obj.event == "orderDetail"){
                    $jq.post("order/getOrderById/"+data.orderId,"",function(order){
                        var editOrder = order;

                        console.log(data)
                        editOrder["title"] = lan.get("order_detail");
                        var html = orderDetail.innerHTML;
                        var htmlStr = laytpl(html).render(editOrder);
                        layer.open({
                            type: 1,
                            area: '700px',
                            title: [editOrder.title, 'text-align:center;font-size:18px;'],
                            content: htmlStr
                        });


                    })
                }

            });
        }
    }



    exports('orderManage', obj);

})