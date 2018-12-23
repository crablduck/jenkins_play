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

            lan.render("strategy_manage");
            //初始化表格
            this.initTable();
            form.render();//表单渲染
        }
        /**
         * 初始化表格
         */
        ,initTable : function () {
            var userId = dataStore.get("for_edit_Pwd");
            table.render({
                elem: "#strategyManageTable"
                ,url: 'rule/getStrategyManageList' //数据请求路径
                ,height: 500
                ,request: {
                    pageName: 'current',
                    limitName: 'size'
                }
                ,where: {
                    strategy: $jq("#strategy_name").val()
                    ,status: $jq("#status").val()
                    ,userId: userId
                }
                ,cols: [[
                    {field: 'xu', title: 'ID', type: 'numbers', width: 80}
                    ,{field: 'strategy', title: layui.language.get('strategy')}
                    ,{field: 'strategyNum', title: layui.language.get('strategy_num')}
                    ,{field: 'ruleNum', title: layui.language.get('rule_num')}
                    ,{field: 'description', title: layui.language.get('discription')}
                    ,{fixed: 'right', title: layui.language.get('status'), align: 'center', width:100, templet: function (d) {
                            if ( d.status == 0){
                                return layui.language.get("enable")
                            }else {
                                return layui.language.get("disable")
                            }
                        }}
                    ,{fixed: 'right', title: layui.language.get("operation"), align: 'center', width:350, templet: function (d) {
                        var operateHtml = '<button  class="layui-btn layui-btn-sm layui-btn-normal" lay-event="edit">'+layui.language.get("edit") +'</button>' +
                            '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="del">'+ layui.language.get("del") +'</button>' +
                            '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="strategy">'+ layui.language.get("strategy") +'</button>';
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
            })
        }
        /**
         * 添加规则---》》》弹出框
         */
        ,viewAddStrategy : function () {
            lan.render("addOrUpdateStrategy")
            var editStrategy= {"strategyManageId":"","description":"","strategy":"","type":"0","title":lan.get("add_strategyManage_list")};
            layui.strategyManage.addOrUpdateStrategyManage(editStrategy);
        }
        /**
         * 添加或编辑规则的渲染页面
         */
        ,addOrUpdateStrategyManage : function (editStrategy) {
            var html = addOrUpdateStrategy.innerHTML;
            var htmlStr = laytpl(html).render(editStrategy);
            layer.open({
                type:1,
                area:'600px',
                title: editStrategy.title,
                content:htmlStr
            });
            //编辑时
            if(editStrategy.type == 1) {
                if(editStrategy.state == 0 ){
                 $jq("#enable").attr("checked","");
                 $jq("#disable").removeAttr("checked");
                }else{
                 $jq("#disable").attr("checked","");
                 $jq("#enable").removeAttr("checked");
                }
            }
            lan.render("addOrUpdateRuleForm");
            form.render();
        }
        /**
         * 监听事件
         */
        ,tool : function () {
            //表格监听事件
            table.on("tool(strategyManageEvent)",function(obj){
                var dataOne = obj.data;//获取一行的数据
                //编辑
                if(obj.event == 'edit'){
                    lan.render("addOrUpdateStrategy")
                    dataOne.type = "1";
                    dataOne.title = lan.get("edit_strategyManage");//编辑策略集
                    layui.strategyManage.addOrUpdateStrategyManage(dataOne);
                }
                //删除
                if(obj.event == 'del'){
                    layer.open({
                        title: [layui.language.get("information")],//信息
                        content: layui.language.get("confirm_del"),//确认删除吗？
                        btn: [layui.language.get("certainly"),layui.language.get("cancel")],//确定   取消
                        yes: function(){
                            $jq.post("rule/deleteStrategyManage/" + dataOne.strategyManageId,"",function(data){
                                if(!ajaxCall(data)){
                                    return;
                                }
                                layer.closeAll();
                                layui.strategyManage.initView();
                            })
                        }
                    })
                }
                //策略
                if(obj.event == 'strategy'){
                    dataStore.set("for_strategy_id",dataOne.strategyManageId);
                    $jq("#parent").hide();
                    $jq("#children").show();
                    $jq.get("views/addStrate.html",function (data) {
                        $jq("#children").html(data);
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
                            $jq.post("rule/updateStrategyMStatus",dataOne,function(data){
                                if(!ajaxCall(data)){
                                    return;
                                }
                                layer.closeAll();
                                layui.strategyManage.initTable();
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
                            $jq.post("rule/updateStrategyMStatus",dataOne,function(data){
                                if(!ajaxCall(data)){
                                    return;
                                }
                                layer.closeAll();
                                layui.strategyManage.initTable();
                            })
                        }
                    })
                }

            });

            //表单监听提交事件
            form.on("submit(addOrUpdateStrategyForm)",function(data){
                    var userId = dataStore.get("for_edit_Pwd");
                    console.log(data.field);
                    $jq.post("rule/addOrUpdateStrategyManage/"+userId,data.field,function(data){
                        if(!ajaxCall(data)){
                            return;
                        }
                        layer.msg("操作完成",{icon:1,time:1000},function(){
                            layer.closeAll();
                            layui.strategyManage.initView();
                        });
                    });
                    return false;
                });

        }

    }

    exports('strategyManage', obj);

})