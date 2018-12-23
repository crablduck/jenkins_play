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
            layui.language.render("dictManage");
            //初始化表格
            this.initTable();

            form.render();//表单渲染
        }
        /**
         * 初始化表格
         */
        ,initTable : function () {
            table.render({
                elem: "#dictManageTable"
                ,url: "dict/getDictList"
                ,height: 500
                ,request: {
                    pageName: "current"
                    ,limitName: "size"
                }
                ,where: {
                    code: $jq("#code_name").val()
                    ,name: $jq("#code_name").val()
                    ,state: $jq("#state").val()
                }
                ,cols: [[
                    {field: 'xu', title: 'ID', type: 'numbers', width: 80}
                    ,{field: 'code', title: layui.language.get("dict_code")}//字典编码
                    ,{field: 'name', title: layui.language.get("dict_name")}//字典名称
                    ,{field: 'value', title: layui.language.get("value")}//值
                    ,{field: 'state', title: layui.language.get("status"), templet: function (d) {//状态
                        if(d.state == 0){
                            return layui.language.get("enable");//启用
                        }else if(d.state == 1){
                            return layui.language.get("disable");//禁用
                        }
                    }}
                    ,{fixed: 'right', title: layui.language.get("operation"), align: 'center', width:250, templet: function (d) {//操作
                        var operateHtml = '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="edit">' + layui.language.get("edit") + '</button>';//编辑
                        if(d.state == 0){
                            operateHtml += '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="disable">' + layui.language.get("disable") + '</button>';//禁用
                        }else if(d.state == 1){
                            operateHtml += '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="enable">' + layui.language.get("enable") + '</button>';//启用
                        }
                        operateHtml += '<button class="layui-btn layui-btn-sm layui-btn-danger" lay-event="del">' + layui.language.get("del") + '</button>';//删除
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
         * 添加字典项---》》》添加弹出框
         */
        ,addDict : function () {
            var addDict = {"id":"","name":"","value":"","code":"","state":"","desc":"","type":"0","title":layui.language.get("add_dict")};//添加字典
            layui.dictManage.addOrUpdateDict(addDict);
        }
        /**
         * 添加或编辑弹出框
         */
        ,addOrUpdateDict : function (editDict) {
            var html = addOrUpdateDict.innerHTML;
            html = html.replace("addOrUpdateDictForm","addOrUpdateDictForm2");
            var htmlStr = laytpl(html).render(editDict);
            layer.open({
                type:1,
                area:'600px',
                title:editDict.title,
                content:htmlStr
            });
            layui.language.render("addOrUpdateDictForm2");
            if(editDict.type == 1){
                if(editDict.state == 0 ){
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
         * 监听事件
         */
        ,tool : function () {
            //表格监听事件
            table.on("tool(dictManageEvent)",function(obj){
                var data = obj.data;//获取一行的数据
                //编辑
                if(obj.event == "edit"){
                    data.type = "1";
                    data.title = layui.language.get("edit_dict");//编辑字典
                    layui.dictManage.addOrUpdateDict(data);
                }
                //启用
                if(obj.event == "enable"){
                    data.state = 0;
                    layer.open({
                        title: [layui.language.get("information")],//信息
                        content: layui.language.get("confirm_enable"),//确认启用吗?
                        btn: [layui.language.get("certainly"),layui.language.get("cancel")],//确定   取消
                        yes: function(){
                            $jq.post("dict/updateState/" + data.id,{"state":data.state},function(data){
                                if(!ajaxCall(data)){
                                    return;
                                }
                                if(data.data){
                                    layer.closeAll();
                                    layui.dictManage.initView();
                                }
                            })
                        }
                    })
                }
                //禁用
                if(obj.event == "disable"){
                    data.state = 1;
                    layer.open({
                        title: [layui.language.get("information")],//信息
                        content: layui.language.get("confirm_disable"),//确认禁用吗?
                        btn: [layui.language.get("certainly"),layui.language.get("cancel")],//确定   取消
                        yes: function(){
                            $jq.post("dict/updateState/" + data.id,{"state":data.state},function(data){
                                if(!ajaxCall(data)){
                                    return;
                                }
                                if(data.data){
                                    layer.closeAll();
                                    layui.dictManage.initView();
                                }
                            })
                        }
                    })
                }
                //删除
                if(obj.event == "del"){
                    layer.open({
                        title: [layui.language.get("information")],//信息
                        content: layui.language.get("confirm_del"),//确认删除吗？
                        btn: [layui.language.get("certainly"),layui.language.get("cancel")],//确定   取消
                        yes: function(){
                            $jq.post("dict/delDict/" + data.id,"",function(data){
                                if(!ajaxCall(data)){
                                    return;
                                }
                                layer.closeAll();
                                layui.dictManage.initView();
                            })
                        }
                    })
                }
            });


            //表单监听提交事件
            form.on("submit(addOrUpdateDictForm)",function(data){
                $jq.post("dict/addOrUpdateDict",data.field,function(data){
                    if(!ajaxCall(data)){
                        return;
                    }
                    layer.closeAll();
                    layui.dictManage.initView();
                    layer.msg(layui.language.get("save_success"),{icon:1,time:1000})//保存成功
                });
                return false;
            });
        }
    }

    exports('dictManage', obj);

})