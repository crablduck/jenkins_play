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
            layui.language.render("parentUser");
            layui.language.render("resetPwd");
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
                $jq("#addUser").show();
                $jq("#role").show();
                //初始化下拉框
                layui.userManage.initSelect();
            }else{
                $jq("#addUser").hide();
                $jq("#role").hide();
            }
        }
        /**
         * 初始化下拉框
         */
        ,initSelect : function () {
            $jq.post("role/getRolesByLevel",{"roleLevel":2},function (data) {
                var roleHtml = '<option value="">' + layui.language.get("role") + '</option>';//角色
                $jq.each(data.data,function (i,item) {
                    roleHtml += '<option value="' + item.roleId + '">' + item.roleName + '</option>';
                });
                $jq("#roleId").html(roleHtml);
                form.render('select');
            });
        }
        /**
         * 初始化表格
         */
        ,initTable : function () {
            table.render({
                elem: "#userManageTable"
                ,url: "user/getUserList"
                ,height: 500
                ,request: {
                    pageName: "current"
                    ,limitName: "size"
                }
                ,where: {
                    userAccount: $jq("#account_company_mobile").val()
                    ,userPhone: $jq("#account_company_mobile").val()
                    ,username: $jq("#account_company_mobile").val()
                    ,roleId: $jq("#roleId").val()
                    ,state: $jq("#state").val()
                }
                ,cols: [[
                    {field: 'xu', title: 'ID', type: 'numbers', width: 80}//ID
                    ,{field: 'userAccount', title: layui.language.get("user_account")}//用户账号
                    ,{field: 'companyName', title: layui.language.get("company_name")}//公司全称
                    ,{field: 'userPhone', title: layui.language.get("phone_number")}//手机号
                    ,{field: 'subAccountNum', title: layui.language.get("account")}//子账户个数
                    ,{field: 'roleName', title: layui.language.get("role")}//角色
                    ,{field: 'state', title: layui.language.get("status"), templet: function (d) {//状态
                        if(d.state == 0){
                            return layui.language.get("enable");//启用
                        }else if(d.state == 1){
                            return layui.language.get("disable");//禁用
                        }
                    }}
                    ,{field: 'createrName', title: layui.language.get("creater")}//创建人
                    ,{field: 'createTime', title: layui.language.get("create_time"), templet: function (d){//创建时间
                        var createTime = d.createTime;
                        var dian = createTime.lastIndexOf(".");
                        return createTime.substring(0,dian);
                    }}
                    ,{fixed: 'right', title: layui.language.get("operation"), align: 'center', width:380, templet: function (d) {//操作
                        var roleLevel = dataStore.get("for_role_level");
                        var operateHtml = '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="edit">' + layui.language.get("edit") + '</button>';//编辑
                        if(roleLevel == 1){
                            operateHtml += '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="resetPwd">' + layui.language.get("reset_password") + '</button>';//重置密码
                            if(d.state == 0){
                                operateHtml += '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="disable">' + layui.language.get("disable") + '</button>';//禁用
                            }else if(d.state == 1){
                                operateHtml += '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="enable">' + layui.language.get("enable") + '</button>';//启用
                            }
                        }
                        operateHtml += '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="subList">' + layui.language.get("sub_user_list") + '</button>';//子账户列表
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
        ,addUser : function () {
            dataStore.set("for_user_manage_type",0);
            var addUser = {"userAccount":"","companyName":"","userPhone":"","password":"","userId":"","descr":"","state":"0","type":"0","title":layui.language.get("add_user")};//添加用户
            $jq.post("role/getRolesByLevel",{"roleLevel":2},function (data) {
                layui.userManage.addOrUpdateUser(data.data,addUser);
            });
        }
        /**
         * 添加或编辑弹出框
         */
        ,addOrUpdateUser : function (roles,editUser) {
            var html = addOrUpdateUser.innerHTML;
            html = html.replace("addOrUpdateUserForm","addOrUpdateUserForm2")
            var htmlStr = laytpl(html).render(editUser);
            layer.open({
                type:1,
                area:'600px',
                title:editUser.title,
                content:htmlStr
            });
            layui.language.render("addOrUpdateUserForm2");
            var roleHtml = '';
            $jq.each(roles,function (i,item) {
                roleHtml += '<option value="' + item.roleId + '">' + item.roleName + '</option>';
            });
            $jq("#roleIdTwo").html(roleHtml);
            if(editUser.roleId){
                $jq("#roleIdTwo").val(editUser.roleId);
            }
            if(editUser.type == 1){
                $jq("#userAccount").attr("disabled","");
                if(editUser.state == 0 ){
                    $jq("#enable").attr("checked","");
                    $jq("#disable").removeAttr("checked");
                }else{
                    $jq("#disable").attr("checked","");
                    $jq("#enable").removeAttr("checked");
                }
            }else{
                $jq("#userAccount").removeAttr("disabled");
            }
            form.render();
        }
        /**
         * 监听事件
         */
        ,tool : function () {
            //表格监听事件
            table.on("tool(userManageEvent)",function(obj){
                var dataOne = obj.data;//获取一行的数据重置密码
                //
                if(obj.event == "resetPwd"){
                    var html = resetPwd.innerHTML;
                    layer.open({
                        type: 1,
                        area: "300px",
                        title:layui.language.get("reset_password"),//重置密码
                        content: html,
                        btn: [layui.language.get("certainly"),layui.language.get("back")],//确定   返回
                        yes: function () {
                           $jq.post("user/resetPwd",{"userId":dataOne.userId},function (data) {
                               if(!ajaxCall(data)){
                                   return;
                               }
                               layer.closeAll();
                           })
                        }
                    });
                }
                //编辑
                if(obj.event == "edit"){
                    dataStore.set("for_user_manage_type",1);
                    $jq.post("role/getRolesByLevel",{"roleLevel":2},function (data) {
                        dataOne.type = "1";
                        dataOne.title = layui.language.get("edit_user");//编辑用户
                        dataOne.companyName = dataOne.username;
                        if(dataOne.descr == null){
                            dataOne.descr = "";
                        }
                        layui.userManage.addOrUpdateUser(data.data,dataOne);
                    });
                }
                //启用
                if(obj.event == "enable"){
                    dataOne.state = 0;
                    layer.open({
                        title: [layui.language.get("information")],//信息
                        content: layui.language.get("confirm_enable"),//确认启用吗?
                        btn: [layui.language.get("certainly"),layui.language.get("cancel")],//确定   取消
                        yes: function(){
                            $jq.post("user/updateState",{"userId":dataOne.userId,"state":dataOne.state},function(data){
                                if(!ajaxCall(data)){
                                    return;
                                }
                                if(data.data){
                                    layer.closeAll();
                                    layui.userManage.initView();
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
                            $jq.post("user/updateState",{"userId":dataOne.userId,"state":dataOne.state},function(data){
                                if(!ajaxCall(data)){
                                    return;
                                }
                                if(data.data){
                                    layer.closeAll();
                                    layui.userManage.initView();
                                }
                            })
                        }
                    })
                }
                //子账户列表
                if(obj.event == "subList"){
                    dataStore.set("for_parent_user_id",dataOne.userId);
                    dataStore.set("for_parent_company_id",dataOne.companyId);
                    dataStore.set("for_parent_user_account",dataOne.userAccount);
                    $jq("#parentUser").hide();
                    $jq("#subUser").show();
                    $jq.get("../bamboo/views/subUserManage.html","",function(data){
                        $jq("#subUser").html(data);
                    })
                }
            });

            //监听表单验证
            form.verify({
                userAccount : function (value) {
                    if(value.length <= 0){
                        return layui.language.get("user_account_not_empty");//用户账号不能为空
                    }
                    if(dataStore.get("for_user_manage_type") == 0){
                        var userInfo = null;
                        $jq.ajax({
                            url: "user/checkUserAccount",
                            type: "post",
                            async: false,  //设置为同步
                            data: {
                                userAccount:value
                            },
                            dataType: "json",
                            success: function (data) {
                                if(!ajaxCall(data)){
                                    return;
                                }
                                userInfo = data.data;
                            }
                        });
                        if(userInfo != null){
                            return layui.language.get("account_exist");//该账户已经存在！
                        }
                    }
                }
                ,companyName : function (value) {
                    if(value.length <= 0){
                        return layui.language.get("company_name_not_empty");//公司名称不能为空
                    }
                }
                ,userPhone : function (value) {
                    if(value.length <= 0){
                        return layui.language.get("phone_number_not_empty");//手机号不能为空
                    }
                }
                ,password : function (value) {
                    if(value.length <= 0){
                        return layui.language.get("password_not_empty");//密码不能为空
                    }
                }
                ,confirmPassword : function (value) {
                    if($jq("#password").val() != value){
                        return layui.language.get("two_password_is_different");//两次密码输入不一致
                    }
                }
            });

            //表单监听提交事件（编辑或添加）
            form.on("submit(addOrUpdateUserForm)",function(data){
                $jq.post("user/addOrUpdateUser",data.field,function(data){
                    if(!ajaxCall(data)){
                        return;
                    }
                    layer.msg(layui.language.get("save_success"),{icon:1,time:1000},function(){//保存成功
                        layer.closeAll();
                        layui.userManage.initView();
                    });
                });
                return false;
            });
        }
    }

    exports('userManage', obj);

})