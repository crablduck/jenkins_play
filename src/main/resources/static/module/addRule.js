layui.define(["jquery","form","table","laytpl"],function (exports) {
    var $jq = layui.jquery,
        form = layui.form,
        laytpl = layui.laytpl,
        table = layui.table;
        lan = layui.language;


    var obj = {
        /**
         * 页面初始化
         */
        initView : function(){

            var strategyId = dataStore.get("for_strategy_id");
            lan.render("addRule")
            form.render();//表单渲染
            // //初始化表格
            this.initTable(strategyId);
        }



        ,initTable : function (strategyId) {
            table.render({
                elem: "#ruleTable"
                //本来下面三个属性解决跨域问题， 但是因为异步访问导致访问问题
                ,time: 1000
                ,url: 'rule/getRuleList'//数据请求路径
                ,page: true
                ,loading: true
                ,where: {
                    ruleName: $jq("#rule_name").val()
                    ,status: $jq("#rule_status").val()
                    ,strategyId : strategyId
                }
                ,cols: [[
                    {field: 'ruleId', title: lan.get("rule_order")}
                    ,{field: 'ruleName', title:  lan.get("rule_name")}
                    ,{field:  'ruleType', title: lan.get("rule_type")}
                    ,{field:  'description', title: lan.get("description")}
                    ,{fixed: 'right', title: lan.get("status"), align: 'center', width:100, templet: function (d) {
                            if ( d.status == 1){
                                return lan.get("enable")
                            }else {
                                return lan.get("disable")
                            }
                        }}
                    ,{fixed: 'right', title: layui.language.get("operation"), align: 'center', width:350 , templet: function (d) {

                            var operateHtml = '<button  class="layui-btn layui-btn-sm layui-btn-normal" lay-event="edit">'+ lan.get("edit") +'</button>' +
                                '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="del">'+ lan.get("del")+'</button>' +
                                '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="view">'+ lan.get("rule_view")  +'</button>';

                            if(d.status == 0){
                                operateHtml += '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="disable">' + layui.language.get("disable") + '</button>';//禁用
                            }else if(d.status == 1){
                                operateHtml += '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="enable">' + layui.language.get("enable") + '</button>';//启用
                            }
                            return operateHtml;

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

        /**
         * 添加规则---》》》添加弹出框
         */
        ,viewAddRule : function () {
            layui.addRule.fieldName();
            var strategyId = dataStore.get("for_strategy_id");
            var editRule= {"ruleId":"","strategyId": strategyId ,"description":"","ruleName":" ","type":"0","title": lan.get("add_rule")};
            layui.addRule.addOrUpdateRule( editRule);
        }

        /**
         * 添加或编辑规则
         */
        ,addOrUpdateRule : function (editRule) {

            var html = addOrUpdateRule.innerHTML;
            html = html.replace("addOrUpdateRuleForm","addOrUpdateRuleForm2");
            var htmlStr = laytpl(html).render(editRule);
            layer.open({
                type:1,
                area:'900px',
                title:editRule.title,
                content:htmlStr
            });



            lan.render("addOrUpdateRuleForm2");

            //编辑时
            if(editRule.type == 1) {

                if (editRule.ruleId != null) {
                    $jq("#ruleId").attr("value", editRule.ruleId)
                }

                //修改名字的值
                if (editRule.ruleName != null) {
                    $jq("#ruleName").attr("value", editRule.ruleName)
                }
                
                //修改规则值
                if (editRule.ruleValue != null) {
                    $jq("#ruleValue").attr("value", editRule.ruleValue);
                }

                //修改规则值
                if (editRule.hitScore != null) {
                    $jq("#hit_score").attr("value", editRule.hitScore);
                }

                //修改描述的值
                if (editRule.description != null) {
                    $jq("#description").text(editRule.description )
                }

                //状态类型
                if (editRule.status == 0) {//若为目录时
                    //TODO::存在兼容性问题
                    $jq("#addOrUpdateStrateForm input[name=status][value=0]").attr("checked", "");
                    $jq("#addOrUpdateStrateForm input[name=status][value=1]").removeAttr("checked");
                }else{
                    $jq("#addOrUpdateStrateForm input[name=status][value=0]").removeAttr("checked");
                    $jq("#addOrUpdateStrateForm input[name=status][value=1]").attr("checked","");
                }

            }

            var roleLevel = dataStore.get("for_role_level");
            if(roleLevel != 1){
                $jq("#hit_score_div").hide();
            }

            form.render();
        }

        /**
         * 添加拼接字段
         */
        ,fieldName: function () {
            $jq.post("field/getFieldAllList", "", function (fieldRespon) {
                var fields = fieldRespon.data;
                var htmlStr = "<option selected disabled>字段名字</option> +\n";
                for (var i = 0; i < fields.length; i++){
                    htmlStr += ("<option value=\""+fields[i].fieldName+"\">"+fields[i].displayName+"</option>+\n")
                }
                $jq("#field_name").html(htmlStr);
                form.render("select");
            })
        }

        /**
         * 编辑拼接字段
         */
        ,editFieldName: function (fieldValue) {
            $jq.post("field/getFieldAllList", "", function (fieldRespon) {
                var fields = fieldRespon.data;
                var htmlStr = "<option disabled>字段名字</option> +\n";
                for (var i = 0; i < fields.length; i++){
                    if (fieldValue == fields[i].fieldName) {

                        htmlStr += ("<option value=\"" + fields[i].fieldName + "\" selected>" + fields[i].displayName + "</option>+\n");
                    } else {
                        htmlStr += ("<option value=\""+fields[i].fieldName+"\">"+fields[i].displayName+"</option>+\n");
                    }
                }
                $jq("#field_name").html(htmlStr);
                form.render("select");
            })
        }

        /**
         * 编辑拼接关系
         */
        ,editFieldRela: function (fieldRela, fieldName) {
            $jq.post("field/getCompareRela?fieldName="+fieldName, "", function (fieldRespon) {
                var fields = fieldRespon.data;
                var htmlStr = "";
                for (var i = 0; i < fields.length; i++){
                    if (fieldRela == fields[i].compareRela) {

                        htmlStr += ("<option value=\"" + fields[i].compareRela + "\" selected>" + fields[i].compareRela + "</option>+\n");
                    } else {
                        htmlStr += ("<option value=\""+fields[i].compareRela+"\">"+fields[i].compareRela+"</option>+\n");
                    }
                }
                $jq("#compare_rela").html(htmlStr);
                form.render("select");
            })
        }

        /**
         * 返回上一个页面
         */
        , backPage : function () {
            $jq("#parentRule").show();
            $jq("#bread_strategy").show();
            $jq("#childrenRule").hide();
            $jq("#bread_rule").hide();
        }

        /**
         * 监听事件
         */
        ,tool : function () {
            var userId = dataStore.get("for_edit_Pwd");
            //表单监听提交事件
            form.on("submit(addOrUpdateRuleForm)",function(data){
                console.log(data.field);
                $jq.post("rule/addOrUpdateRule/"+userId,data.field,function(data){
                    if(!ajaxCall(data)){
                        return;
                    }
                    layer.msg("操作完成",{icon:1,time:1000},function(){
                        layer.closeAll();
                        layui.addRule.initTable();
                    });
                });
                return false;
            });

            form.on("select(fieldName)",function (data) {

                var fieldName = $jq("#field_name").val()
                $jq.get("field/getCompareRela?fieldName="+fieldName, function (fieldRespon) {
                    debugger;
                    var fields = fieldRespon.data;
                    var htmlStr = "";
                    for (var i = 0; i < fields.length; i++){
                        htmlStr += ("<option class='field' value=\""+fields[i].compareRela+"\">"+fields[i].compareRela+"</option>+\n")
                    }
                    $jq("#compare_rela").html(htmlStr);
                    form.render("select");
                })
            })

            table.on("tool(ruleEvent)",function(obj){
                var dataOne = obj.data;//获取一行的数据

                //编辑
                if(obj.event == 'edit'){
                    layui.addRule.editFieldName(dataOne.ruleType);
                    layui.addRule.editFieldRela(dataOne.compareRelaValue,dataOne.ruleType );
                    dataOne.type = "1";
                    dataOne.title = lan.get("edit_rule");//编辑策略集
                    layui.addRule.addOrUpdateRule(dataOne);
                }
                //删除
                if(obj.event == 'del'){
                    layer.open({
                        title: [layui.language.get("information")],//信息
                        content: layui.language.get("confirm_del"),//确认删除吗？
                        btn: [layui.language.get("certainly"),layui.language.get("cancel")],//确定   取消
                        yes: function(){
                            $jq.post("rule/deleteRule/" + dataOne.ruleId,"",function(data){
                                if(!ajaxCall(data)){
                                    return;
                                }
                                layer.closeAll();
                                layui.addRule.initTable();
                            })
                        }
                    })
                }
                //查看规则
                if(obj.event == 'view'){
                    $jq.post("rule/getRuleById/"+dataOne.ruleId,"",function(viewRule){
                        viewRule["title"] = lan.get("rule_view");
                        var html = viewRuleScript.innerHTML;
                        var htmlStr = laytpl(html).render(viewRule.data);
                        layer.open({
                            type:1,
                            area:'700px',
                            title:[viewRule.title,'text-align:center;font-size:18px;'],
                            content:htmlStr
                        });
                        lan.render("viewRuleDiv")
                    })
                }
                //启用
                if(obj.event == 'enable'){
                    dataOne.status = 0;
                    layer.open({
                        title: [layui.language.get("information")],//信息
                        content: layui.language.get("confirm_enable"),//确认启用吗?
                        btn: [layui.language.get("certainly"),layui.language.get("cancel")],//确定   取消
                        yes: function(){
                            $jq.post("rule/updateRuleStatus/"+dataOne.ruleId,"",function(data){
                                if(!ajaxCall(data)){
                                    return;
                                }
                                layer.closeAll();
                                layui.addRule.initTable();
                            })
                        }
                    })

                }
                //禁用
                if(obj.event == 'disable'){
                    dataOne.status = 1;
                    layer.open({
                        title: [layui.language.get("information")],//信息
                        content: layui.language.get("confirm_disable"),//确认禁用吗?
                        btn: [layui.language.get("certainly"),layui.language.get("cancel")],//确定   取消
                        yes: function(){
                            $jq.post("rule/updateRuleStatus/"+dataOne.ruleId, "",function(data){
                                if(!ajaxCall(data)){
                                    return;
                                }
                                layer.closeAll();
                                layui.addRule.initTable();
                            })
                        }
                    })

                }

            });
        }
    }



    exports('addRule', obj);

})