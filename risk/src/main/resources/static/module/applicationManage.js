layui.define(["jquery","form","table","laytpl"],function (exports) {
    var $jq = layui.jquery,
        form = layui.form,
        table = layui.table,
        laytpl = layui.laytpl;

    var obj = {
        /**
         * 页面初始化
         */
        initView : function(){
            layui.language.render("mainIndex");
            //初始化角色
            this.initRole();

            //初始化表格
            this.initTable();
            form.render();//表单渲染

        }
        /**
         * 初始化角色
         */
        ,initRole : function () {
            var roleLevel = dataStore.get("for_role_level");
            if(roleLevel == 1){
                $jq("#addApp").hide();
            }else{
                $jq("#addApp").show();
            }
        }
        /**
         * 初始化表格
         */
        ,initTable : function () {
            table.render({
                elem: "#applicationManageTable"
                ,url: "app/getCommodityList"
                ,height: 500
                ,request: {
                    pageName: "current"
                    ,limitName: "size"
                }
                ,where: {
                    commodityNumber: $jq("#commodity-number-name").val()
                    ,commodityName: $jq("#commodity-number-name").val()
                    ,state: $jq("#state").val()
                }
                ,cols: [[
                    {field: 'xu', title: 'ID', type: 'numbers', width: 80}
                    ,{field: 'commodityNumber', title: layui.language.get("app_number")}//应用编号
                    ,{field: 'commodityName', title: layui.language.get("app_name")}//应用名称
                    ,{field: 'commoditySecret', title: layui.language.get("merchant_key")}//商户密匙
                    ,{field: 'descr', title: layui.language.get("note")}//备注
                    ,{field: 'state', title: layui.language.get("status"), templet: function (d) {//状态
                        if(d.state == 0){
                            return layui.language.get("enable");//启用
                        }else if(d.state == 1){
                            return layui.language.get("disable");//禁用
                        }
                    }}
                    ,{fixed: 'right', title: layui.language.get("operation"), align: 'center', width:300, templet: function (d) {//操作
                        var operateHtml = '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="edit">' + layui.language.get("edit") + '</button>';//编辑
                        if(d.state == 0){
                            operateHtml += '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="disable">' + layui.language.get("disable") + '</button>';//禁用
                        }else if(d.state == 1){
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
         * 添加用户---》》》添加弹出框
         */
        ,addApp : function () {
            var addApp = {"id":"","commodityNumber":"","commodityName":"","commoditySecret":"","descr":"","state":"0","type":"0","title":layui.language.get("add_app")};//添加应用
            layui.applicationManage.addOrUpdateApp(addApp);
        }
        /**
         * 添加或编辑弹出框
         */
        ,addOrUpdateApp : function (editApp) {
            var html = addOrUpdateApp.innerHTML;
            html = html.replace("addOrUpdateAppForm","addOrUpdateAppForm2");
            var htmlStr = laytpl(html).render(editApp);
            layer.open({
                type:1,
                area:'600px',
                title:editApp.title,
                content:htmlStr
            });
            if(editApp.type == 1){
                if(editApp.state == 0 ){
                    $jq("#enable").attr("checked","");
                    $jq("#disable").removeAttr("checked");
                }else{
                    $jq("#disable").attr("checked","");
                    $jq("#enable").removeAttr("checked");
                }
            }
            layui.language.render("addOrUpdateAppForm2");
            form.render();
        }
        /**
         * 监听事件
         */
        ,tool : function () {
            //表格监听事件
            table.on("tool(applicationManageEvent)",function(obj){
                var dataOne = obj.data;//获取一行的数据
                //编辑
                if(obj.event == "edit"){
                    dataOne.type = 1;
                    dataOne.title = layui.language.get("edit_app");//编辑应用
                    layui.applicationManage.addOrUpdateApp(dataOne);
                }
                //启用
                if(obj.event == "enable"){
                    dataOne.state = 0;
                    layer.open({
                        title: [layui.language.get("information")],//信息
                        content: layui.language.get("confirm_enable"),//确认启用吗?
                        btn: [layui.language.get("certainly"),layui.language.get("cancel")],//确定   取消
                        yes: function(){
                            $jq.post("app/updateState",{"id":dataOne.id,"state":dataOne.state},function(data){
                                if(!ajaxCall(data)){
                                    return;
                                }
                                if(data.data){
                                    layer.closeAll();
                                    layui.applicationManage.initView();
                                }
                            })
                        }
                    })
                }
                //禁用
                if(obj.event == "disable"){
                    dataOne.state = 1;
                    layer.open({
                        title: [layui.language.get("information")],//信息
                        content: layui.language.get("confirm_disable"),//确认禁用吗?
                        btn: [layui.language.get("certainly"),layui.language.get("cancel")],//确定   取消
                        yes: function(){
                            $jq.post("app/updateState",{"id":dataOne.id,"state":dataOne.state},function(data){
                                if(!ajaxCall(data)){
                                    return;
                                }
                                if(data.data){
                                    layer.closeAll();
                                    layui.applicationManage.initView();
                                }
                            })
                        }
                    })
                }
            });

            //表单验证
            form.verify({
                commodityNumber :  function (value) {
                    if(value.length <= 0 ){
                        return layui.language.get("app_number_not_empty");//应用编号不能为空
                    }
                }
                ,commodityName : function (value) {
                    if(value.length <= 0 ){
                        return layui.language.get("app_name_not_empty");//应用名称不能为空
                    }
                }
            });

            //表单监听提交事件（编辑或添加）
            form.on("submit(addOrUpdateAppForm)",function(data){
                $jq.post("app/addOrUpdateApp",data.field,function(data){
                    if(!ajaxCall(data)){
                        return;
                    }
                    layer.closeAll();
                    layui.applicationManage.initView();
                    layer.msg(layui.language.get("save_success"),{icon:1,time:1000})//保存成功
                });
                return false;
            });
        }
    }

    exports('applicationManage', obj);

})