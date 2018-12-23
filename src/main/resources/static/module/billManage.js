layui.define(["jquery","form","table","laytpl"],function (exports) {
    var $jq = layui.jquery,
        form = layui.form,
        laytpl = layui.laytpl,
        table = layui.table;
        lat = layui.laydate;
        lan = layui.language;

    var obj = {

        /**
         * 页面初始化
         */
        initView : function(){
            var strategyId = dataStore.get("for_strategy_id");
            //初始化下拉框
            this.initRole();
            lan.render("bill");
            form.render();//表单渲染
            // //初始化表格
            this.initTable(strategyId);

            // this.initDate();
        }

        /**
         * 初始化角色
         */
        ,initRole : function () {
            var roleLevel = dataStore.get("for_role_level");
            if(roleLevel == 1){
                layui.billManage.initSelect();
            }else{
                $jq("#company_name_div").hide();
                $jq("#account_name").hide();
                $jq("#query").hide();
                $jq("#recharge_button").hide();
            }
        }

        /**
         * 初始化账单管理页面的下拉框
         */
        ,initSelect : function () {
            $jq.post("company/getCompanyList","",function (data) {
                var companyHtml = '<option value="" selected disabled>'+lan.get("company_name")+'</option>';//公司
                $jq.each(data.data,function (i,item) {
                    companyHtml += '<option value="' + item.companyId + '">' + item.companyName + '</option>';
                });
                $jq("#company_name").html(companyHtml);
                form.render('select');
            });
        }

        /**
         * 初始化账单管理的表单
         * @param strategyId
         */
        ,initTable : function (strategyId) {
            var roleLevel = dataStore.get("for_role_level");
            var userId = dataStore.get('for_edit_Pwd');
            table.render({
                elem: "#billManageTable"
                //本来下面三个属性解决跨域问题， 但是因为异步访问导致访问问题
                ,time: 1000
                ,url: 'bill/getBillList/'+roleLevel//数据请求路径
                ,page: true
                ,loading: true
                ,where: {
                    account: $jq("#account_name").val()
                    ,company_id: $jq("#company_name").val()
                    ,userId: userId
                }
                ,cols: [[
                    {field: 'billId', title: lan.get("bill_order")}
                    ,{field: 'account', title: lan.get("bill_count")}
                    ,{field:  'companyName', title: lan.get("company_name")}
                    ,{field:  'phoneNumber', title: lan.get("phone")}
                    ,{field:  'balance', title: lan.get("now_amount")}
                    ,{fixed: 'right', title: lan.get("operation"), align: 'center', width:450, templet: function (d) {
                            return '<button id="recharge_button" class="layui-btn layui-btn-sm layui-btn-normal" lay-event="recharge">'+  lan.get("recharge") +'</button>' +
                                '<button class="layui-btn layui-btn-sm" lay-event="rechargeRecord">'+lan.get("recharge_record") +'</button>' +
                                '<button class="layui-btn layui-btn-sm layui-btn-warm" lay-event="consumDetail">'+ lan.get("expense_calendar")+'</button>'

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

        /**
         * 监听事件
         */
        ,tool : function () {
            //表单监听提交事件
            form.on("submit(addOrUpdateBillForm)",function(data){
                console.log(data.field);
                $jq.post("bill/addRecharge",data.field,function(data){
                    if(!ajaxCall(data)){
                        return;
                    }
                    layer.msg("操作完成",{icon:1,time:1000},function(){
                        layer.closeAll();
                        layui.billManage.initView();
                    });
                });
                return false;
            });

           // 表格监听事件
            table.on("tool(billManageEvent)",function(obj){
                var data = obj.data;//获取一行的数据

                // 充值
                if(obj.event == "recharge"){
                    data.title = layui.language.get("recharge");//编辑字典
                    $jq.get("bill/getBillById/"+data.billId,"",function(data){
                        var editBill = data;
                        editBill["title"] = lan.get("recharge");

                        var html = addOrUpdateBill.innerHTML;
                        var htmlStr = laytpl(html).render(editBill);

                        layer.open({
                            type: 1,
                            area: '700px',
                            title: [editBill.title, 'text-align:center;font-size:18px;'],
                            content: htmlStr
                        });
                        layui.language.render("rechargeDiv");
                    })
                }

                //入账明细
                if(obj.event == "rechargeRecord"){
                    $jq.post("bill/getBillById/"+data.billId,"",function(data){
                        var editBill = data;
                        editBill["title"] = lan.get("recharge_record");

                        var html = rechargeRecord.innerHTML;
                        var htmlStr = laytpl(html).render(editBill);

                        layer.open({
                            type: 1,
                            area: '960px',
                            title: [editBill.title, 'text-align:center;font-size:18px;'],
                            content: htmlStr,
                            success: function(){
                                lat.render({
                                    elem: '#rechargeDate'
                                    ,range: true
                                });
                            }
                        });
                        layui.language.render("recharge");
                        debugger
                        table.render({
                            elem: "#rechargeRecordTable"
                            //本来下面三个属性解决跨域问题， 但是因为异步访问导致访问问题
                            ,time: 1000
                            ,url: 'bill/getRechargeList/'+data.data.billId //数据请求路径
                            ,page: true
                            ,loading: true
                            ,where: {
                                acount: $jq("#account_name").val()
                                ,companyName: $jq("#company_name").val()
                            }
                            ,toolbar: '#toolbarDemo'
                            ,cols: [[
                                {field: 'rechargeId', sort: true, title: lan.get("bill_order")}
                                ,{field: 'rechargeAmount', sort: true, title: lan.get("recharge_money")}
                                ,{field:  'currentAmount', sort: true, title: lan.get("now_amount")}
                                ,{field:  'operatorName', sort: true, title: lan.get("operator")}
                                ,{field:  'addTime', sort: true, title:  lan.get("add_time"),
                                    templet :function (row){
                                        return layui.billManage.createTime(row.addTime);
                                    }}
                            ]]
                            ,request: {
                                pageName: 'current',
                                limitName: 'size'
                            }
                            ,limit: 10
                            ,limits: [10, 20, 30, 40, 50]
                        });
                    })
                }

                // 消费明细
                if(obj.event == "consumDetail"){
                    $jq.post("bill/getBillById/"+data.billId,"",function(data){
                        var editBill = data;
                        editBill["title"] = lan.get("expense_calendar");
                        var html = consumRecord.innerHTML;
                        var htmlStr = laytpl(html).render(editBill);
                        layer.open({
                            type: 1,
                            area: '1220px',
                            title: [editBill.title, 'text-align:center;font-size:18px;'],
                            content: htmlStr
                        });
                        layui.language.render("consumRecordDiv");
                        table.render({
                            elem: "#consumRecordTable"
                            //本来下面三个属性解决跨域问题， 但是因为异步访问导致访问问题
                            ,time: 1000
                            ,url: 'bill/getConsumeList/'+data.data.billId//数据请求路径
                            ,page: true
                            ,loading: true
                            ,where: {
                                acount: $jq("#account_name").val()
                                ,companyName: $jq("#company_name").val()
                            }
                            ,toolbar: '#toolbarDemo'
                            ,cols: [[
                                {field: 'consumeId', title: lan.get("consume_id")}
                                ,{field: 'applicationName', title: lan.get("application_name")}
                                ,{field:  'projectName', title: lan.get("project_name")}
                                ,{field:  'consumePrice', title: lan.get("price")}
                                ,{field:  'invokeTime', title: lan.get("invoke_time")}
                                ,{field:  'consumeAmount', title: lan.get("consume_money")}
                                ,{field:  'consumeTime', sort: true, title:  lan.get("consume_time"),
                                    templet :function (row){
                                        return layui.billManage.createTime(row.consumeTime);
                                    }}
                            ]]
                            ,request: {
                                pageName: 'current',
                                limitName: 'size'
                            }
                            ,limit: 10
                            ,limits: [10, 20, 30, 40, 50]
                        });

                    })
                }


            });
        }
    }



    exports('billManage', obj);

})