layui.define(["jquery","form","table","laytpl"],function (exports) {
    var $jq = layui.jquery,
        form = layui.form,
        laytpl = layui.laytpl,
        table = layui.table;
        lan = layui.language;
    var strategiesId;

    var obj = {
        /**
         * 页面初始化
         */
        initView : function(){
            strategiesId = dataStore.get("for_strategy_id");
            lan.render("parentRule")
            form.render();//表单渲染
            // //初始化表格
            this.initTable();
        }

        ,initTable : function () {
            table.render({
                elem: "#strategyTable"
                ,url: 'rule/getStrategy'//数据请求路径
                ,height: 500
                ,request: {
                    pageName: 'current',
                    limitName: 'size'
                }
                ,where: {
                    strategy: $jq("#strate").val()
                    ,status: $jq("#strategy_status").val()
                    ,strategiesId : strategiesId
                }
                ,cols: [[
                    {field: 'xu', title: 'ID', type: 'numbers', width: 80}
                    ,{field: 'strategyId', title: lan.get("strategy_order")}
                    ,{field: 'strategy', title: lan.get("single_strategy")}
                    ,{field:  'ruleNum', title: lan.get("rule_num")}
                    ,{field: 'description', title: lan.get("description")}
                    ,{fixed: 'right', title: lan.get("status"), align: 'center', width:100, templet: function (d) {
                            if ( d.status == 0){
                                return lan.get("enable")
                            }else {
                                return lan.get("disable")
                            }
                        }}
                    ,{fixed: 'right', title: lan.get("operate_success"), align: 'center', width:350, templet: function (d) {
                        var operateHtml = '<button  class="layui-btn layui-btn-sm layui-btn-normal" lay-event="edit">'+ lan.get("edit") +'</button>' +
                                '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="del">'+ lan.get("del")+'</button>' +
                                '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="rule">'+ lan.get("rule") +'</button>';
                        if(d.status == 0){
                            operateHtml += '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="disable">' + layui.language.get("disable") + '</button>';//禁用
                        }else if(d.status == 1){
                            operateHtml += '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="enable">' + layui.language.get("enable") + '</button>';//启用
                        }
                        return operateHtml;
                        }}
                ]]
                ,page: {
                    layout: ['count', 'prev', 'page', 'next', 'limit', 'skip']
                    ,limits:[10,20,50,100]
                }
            });

        }
        /**
         * 添加策略
         */
        ,viewAddStrate : function () {

            var editStrate= {"strategyId":"", "strategiesId":strategiesId ,"description":"","strategy":"","type":"0","title":lan.get("add_strategy_list")};
            layui.addStrate.addOrUpdateStrategy(editStrate);
        }
        /**
         * 添加编辑规则模板的弹窗
         */
        ,addOrUpdateStrategy : function (editStrate) {
            var html = addOrUpdateStrate.innerHTML;
            var htmlStr = laytpl(html).render(editStrate);
            layer.open({
                type:1,
                area:'600px',
                title:editStrate.title,
                content:htmlStr
            });
            lan.render("addStrateDiv");
            //编辑时
            if(editStrate.type == 1) {
                if(editStrate.state == 0 ){
                    $jq("#enable").attr("checked","");
                    $jq("#disable").removeAttr("checked");
                }else{
                    $jq("#disable").attr("checked","");
                    $jq("#enable").removeAttr("checked");
                }
            }
            form.render();
        }

        /**
         * 返回上一个页面
         */
        , backPage : function () {
            $jq("#parent").show();
            $jq("#children").hide();
        }

        /**
         * 监听事件
         */
        ,tool : function () {
            //表格监听事件
            table.on("tool(strategyEvent)",function(obj){
                var dataOne = obj.data;//获取一行的数据
                //编辑
                if(obj.event == 'edit'){
                    dataOne.type = "1";
                    dataOne.title = lan.get("strategy_edit");//编辑策略集
                    layui.addStrate.addOrUpdateStrategy(dataOne);
                }
                //删除
                if(obj.event == 'del'){
                    layer.open({
                        title: [layui.language.get("information")],//信息
                        content: layui.language.get("confirm_del"),//确认删除吗？
                        btn: [layui.language.get("certainly"),layui.language.get("cancel")],//确定   取消
                        yes: function(){
                            $jq.post("rule/deleteStrategy/" + dataOne.strategyId,"",function(data){
                                if(!ajaxCall(data)){
                                    return;
                                }
                                layer.closeAll();
                                layui.addStrate.initView();
                            })
                        }
                    })
                }
                //规则
                if(obj.event == 'rule'){
                    dataStore.set("for_strategy_id",dataOne.strategyId);
                    $jq("#parentRule").hide();
                    $jq("#bread_strategy").hide();
                    $jq("#childrenRule").show();
                    $jq.get("views/addRule.html",function (data) {
                        $jq("#childrenRule").html(data);
                    })
                }
                //启用
                if(obj.event == 'enable'){
                    dataOne.status = 0;
                    delete dataOne.addTime;
                    delete dataOne.modifyTime;
                    layer.open({
                        title: [layui.language.get("information")],//信息
                        content: layui.language.get("confirm_enable"),//确认启用吗?
                        btn: [layui.language.get("certainly"),layui.language.get("cancel")],//确定   取消
                        yes: function(){
                            $jq.post("rule/updateStrategyStatus",dataOne,function(data){
                                if(!ajaxCall(data)){
                                    return;
                                }
                                layer.closeAll();
                                layui.addStrate.initView();
                            })
                        }
                    })
                }
                //禁用
                if(obj.event == 'disable'){
                    dataOne.status = 1;
                    delete dataOne.addTime;
                    delete dataOne.modifyTime;
                    layer.open({
                        title: [layui.language.get("information")],//信息
                        content: layui.language.get("confirm_disable"),//确认禁用吗?
                        btn: [layui.language.get("certainly"),layui.language.get("cancel")],//确定   取消
                        yes: function(){
                            $jq.post("rule/updateStrategyStatus",dataOne,function(data){
                                if(!ajaxCall(data)){
                                    return;
                                }
                                layer.closeAll();
                                layui.addStrate.initView();
                            })
                        }
                    })
                }

            });

            //表单监听提交事件
            form.on("submit(addOrUpdateStrateForm)",function(data){
                console.log(data.field);
                $jq.post("rule/addOrUpdateStrategy",data.field,function(data){
                    if(!ajaxCall(data)){
                        return;
                    }
                    layer.msg("操作完成",{icon:1,time:1000},function(){
                        layer.closeAll();
                        layui.addStrate.initView();
                    });
                });
                return false;
            });

        }


    }



    exports('addStrate', obj);

})