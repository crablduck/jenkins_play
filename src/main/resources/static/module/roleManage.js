layui.define(["jquery","form","table","laytpl"],function (exports) {
    var $jq = layui.jquery,
        form = layui.form,
        table = layui.table,
        laytpl = layui.laytpl,
        menuTree = null;

    var obj = {
        /**
         * 页面初始化
         */
        initView : function(){
            layui.language.render("mainIndex");
            layui.language.render("roleAuth");
            //初始下拉框
            this.initSelect();

            //初始化表格
            this.initTable();
            form.render();//表单渲染
        }
        /**
         * 初始下拉框
         */
        ,initSelect : function () {
            $jq.post("role/getRoleList","",function(data){
                var roleHtml = '<option value="">' + layui.language.get("role") + '</option>';//角色
                if(data.data){
                    $jq.each(data.data,function (i,item) {
                        roleHtml += '<option value="' + item.roleId + '">' + item.roleName + '</option>';
                    })
                }
                $jq("#roleId").html(roleHtml);
                form.render('select');
            });
        }
        /**
         * 初始化表格
         */
        ,initTable : function () {
            table.render({
                elem: "#roleManageTable"
                ,url: "role/getRoleList"
                ,height: 500
                ,request: {
                    pageName: "current"
                    ,limitName: "size"
                }
                ,where: {
                    roleId: $jq("#roleId").val()
                    // ,state: $jq("#state").val()
                }
                ,cols: [[
                    {field: 'xu', title: 'ID', type: 'numbers', width: 80}
                    ,{field: 'roleName', title: layui.language.get("role_name")}//角色名称
                    ,{field: 'roleLevel', title: layui.language.get("role_level"), templet: function (d) {//角色级别
                        if(d.roleLevel == 1){
                            return layui.language.get("one_level");//一级
                        }else if(d.roleLevel == 2){
                            return layui.language.get("two_level");//二级
                        }else{
                            return layui.language.get("three_level");//三级
                        }
                    }}
                    ,{field: 'descr', title: layui.language.get("description")}//描述
                    // ,{field: 'state', title: '状态', templet: function (d) {
                    //     if(d.state == 0){
                    //         return "启用";
                    //     }else if(d.state == 1){
                    //         return "禁用";
                    //     }
                    // }}
                    ,{fixed: 'right', title: layui.language.get("operation"), align: 'center', width:250, templet: function (d) {//操作
                        var operateHtml = "";
                        operateHtml += '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="edit">' + layui.language.get("edit") + '</button>';//编辑
                        if(d.roleLevel != 1){
                            operateHtml += '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="auth">' + layui.language.get("auth") + '</button>'//授权
                                            + '<button class="layui-btn layui-btn-sm layui-btn-danger" lay-event="del">' + layui.language.get("del") + '</button>';//删除
                        }
                        // if(d.state == 0){
                        //     operateHtml += '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="disable">禁用</button>';
                        // }else if(d.state == 1){
                        //     operateHtml += '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="enable">启用</button>';
                        // }
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
         * 角色授权
         */
        ,initMenuTree : function(oneData){
            var html = roleAuth.innerHTML;
            delete oneData.createTime;
            var htmlStr = laytpl(html).render(oneData);
            layer.open({
                type:1,
                area:['600px','400px'],
                title:layui.language.get("role_auth"),//角色授权
                content:htmlStr
            });
            var json = "";
            $jq.ajax({
                url: "role/authRoles",
                type: "post",
                async: false,  //设置为同步
                data: oneData,
                dataType: "json",
                success: function (data) {
                    $jq.each(data.data,function (i,item) {
                        item.title = layui.language.get(item.title);
                        $jq.each(item.data,function(j,subItem){
                            subItem.title = layui.language.get(subItem.title);
                        });
                    });
                    json = data.data;
                }
            });
            menuTree = new layuiXtree({
                elem: 'menuTree'   //(必填) 放置xtree的容器，样式参照 .xtree_contianer
                , form: form     //(必填) layui 的 from
                , data: json     //(必填) json数据
                , icon: {        //三种图标样式，更改几个都可以，用的是layui的图标
                    open: "&#xe625"       //节点打开的图标
                    , close: "&#xe623;"    //节点关闭的图标
                    , end: ""      //末尾节点的图标
                }
            });
        }
        /**
         * 添加字典项---》》》添加弹出框
         */
        ,addRole : function () {
            var addRole = {"roleId":"","roleName":"","descr":"","type":"0","title":layui.language.get("add_role")};//添加角色
            layui.roleManage.addOrUpdateRole(addRole);
        }
        /**
         * 添加或编辑弹出框
         */
        ,addOrUpdateRole : function (editRole) {
            var html = addOrUpdateRole.innerHTML;
            html = html.replace("addOrUpdateRoleForm","addOrUpdateRoleForm2");
            var htmlStr = laytpl(html).render(editRole);
            layer.open({
                type:1,
                area:'600px',
                title:editRole.title,
                content:htmlStr
            });
            layui.language.render("addOrUpdateRoleForm2");
            if(editRole.roleLevel){
                $jq("#roleLevel").val(editRole.roleLevel);
            }
            // if(editRole.type == 1){
            //     if(editRole.state == 0 ){
            //         $jq("#enable").attr("checked","");
            //         $jq("#disable").removeAttr("checked");
            //     }else{
            //         $jq("#disable").attr("checked","");
            //         $jq("#enable").removeAttr("checked");
            //     }
            // }
            form.render();
        }
        /**
         * 监听事件
         */
        ,tool : function () {
            //表格监听事件
            table.on("tool(roleManageEvent)",function(obj){
                var data = obj.data;//获取一行的数据
                //授权
                if(obj.event == "auth"){
                    layui.roleManage.initMenuTree(data);
                }
                //编辑
                if(obj.event == "edit"){
                    data.type = "1";
                    data.title = layui.language.get("edit_role");//编辑角色
                    layui.roleManage.addOrUpdateRole(data);
                }
                // //启用
                // if(obj.event == "enable"){
                //     data.state = 0;
                //     layer.open({
                //         title: ["信息"],
                //         content: "确认启用吗?",
                //         btn: ["确定","取消"],
                //         yes: function(){
                //             $jq.post("role/updateState/" + data.roleId,{"state":data.state},function(data){
                //                 if(!ajaxCall(data)){
                //                     return;
                //                 }
                //                 if(data.data){
                //                     layer.closeAll();
                //                     layui.roleManage.initView();
                //                 }
                //             })
                //         }
                //     })
                // }
                // //禁用
                // if(obj.event == "disable"){
                //     data.state = 1;
                //     layer.open({
                //         title: ["信息"],
                //         content: "确认禁用吗?",
                //         btn: ["确定","取消"],
                //         yes: function(){
                //             $jq.post("role/updateState/" + data.roleId,{"state":data.state},function(data){
                //                 if(!ajaxCall(data)){
                //                     return;
                //                 }
                //                 if(data.data){
                //                     layer.closeAll();
                //                     layui.roleManage.initView();
                //                 }
                //             })
                //         }
                //     })
                // }
                //删除
                if(obj.event == "del"){
                    layer.open({
                        title: [layui.language.get("information")],//信息
                        content: layui.language.get("confirm_del"),//确认删除吗？
                        btn: [layui.language.get("certainly"),layui.language.get("cancel")],//确定   取消
                        yes: function(){
                            $jq.post("role/delRole/" + data.roleId,"",function(data){
                                if(!ajaxCall(data)){
                                    return;
                                }
                                layer.closeAll();
                                layui.roleManage.initView();
                            })
                        }
                    })
                }
            });

            //表单验证
            form.verify({
                roleName : function (value) {
                    if(value.length <= 0){
                        return layui.language.get("role_name_not_empty");//角色名称不能为空
                    }
                }
            });

            //表单监听提交事件（编辑或添加）
            form.on("submit(addOrUpdateRoleForm)",function(data){
                $jq.post("role/addOrUpdateRole",data.field,function(data){
                    if(!ajaxCall(data)){
                        return;
                    }
                    layer.closeAll();
                    layui.roleManage.initView();
                    layer.msg(layui.language.get("save_success"),{icon:1,time:1000})//保存成功
                });
                return false;
            });
            //表单监听提交事件（授权）
            form.on("submit(nowSubmit)",function (data) {
                var oCks = menuTree.GetChecked(); //这是方法
                var menuIds = [];
                for (var i = 0; i < oCks.length; i++) {
                    var b = menuTree.GetParent(oCks[i].value);
                    if(b != null){
                        menuIds.push(b.value);
                    }
                    menuIds.push(oCks[i].value);
                }
                $jq.post("role/editRoleMenus",{"roleId":data.field.roleId,"menuIds":Array.from(new Set(menuIds)).join(",")},function (data) {
                    if(!ajaxCall(data)){
                        return;
                    }
                    layer.closeAll();
                    layui.roleManage.initView();
                    layer.msg(layui.language.get("save_success"),{icon:1,time:1000})//保存成功

                });
                return false;
            })
        }
    }

    exports('roleManage', obj);

})