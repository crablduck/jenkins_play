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
            layui.language.render("childUser");
            layui.language.render("resetPwdSub");
            layui.language.render("subAuth");
            //初始化下拉框
            this.initSelect();
            //初始化角色
            this.initRole();
            //初始化表格
            this.initTable();
            form.render();//表单渲染
        }
        /**
         * 初始化下拉框
         */
        ,initSelect : function () {
            debugger;
            var companyId = dataStore.get("for_parent_company_id");
            var commodityHtml = '<option value="">' + layui.language.get("app") + '</option>';//所属应用
            $jq.post("app/getCommodityById",{"companyId":companyId},function (data) {
                if(!ajaxCall(data)){
                    return;
                }
                $jq.each(data.data,function (i,item) {
                    commodityHtml += '<option value="' + item.id + '">' + item.commodityName + '</option>';
                })
                $jq("#commodityId").html(commodityHtml);
                form.render("select");
            })
        }
        /**
         * 初始化角色
         */
        ,initRole : function () {
            var roleLevel = dataStore.get("for_role_level");
            if(roleLevel == 1){
                $jq("#addSubUser").hide();
            }else{
                $jq("#addSubUser").show();
            }
        }
        /**
         * 初始化表格
         */
        ,initTable : function () {
            table.render({
                elem: "#subUserManageTable"
                ,url: "user/getSubUserList"
                ,height: 500
                ,request: {
                    pageName: "current"
                    ,limitName: "size"
                }
                ,where: {
                    createrId:dataStore.get("for_parent_user_id")
                    ,userAccount: $jq("#account_name_mobile").val()
                    ,userPhone: $jq("#account_name_mobile").val()
                    ,username: $jq("#account_name_mobile").val()
                    ,commodityId: $jq("#commodityId").val()
                    ,state: $jq("#subState").val()
                }
                ,cols: [[
                    {field: 'xu', title: 'ID', type: 'numbers', width: 80}
                    ,{field: 'createrAccount', title: layui.language.get("parent_account")}//父级账号
                    ,{field: 'userAccount', title: layui.language.get("user_account")}//用户账号
                    ,{field: 'username', title: layui.language.get("user_name")}//用户姓名
                    ,{field: 'userPhone', title: layui.language.get("phone_number")}//手机号
                    ,{field: 'roleName', title: layui.language.get("role")}//角色
                    ,{field: 'commodityName', title: layui.language.get("app")}//所属应用
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
                    ,{fixed: 'right', title: layui.language.get("operation"), align: 'center', width:300, templet: function (d) {//操作
                        var roleLevel = dataStore.get("for_role_level");
                        var operateHtml = "";
                        if(roleLevel != 1){
                            operateHtml  += '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="edit">' + layui.language.get("edit") + '</button>';//编辑
                        }
                        operateHtml += '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="resetPwd">' + layui.language.get("reset_password") + '</button>';//重置密码
                        if(d.state == 0){
                            operateHtml += '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="disable">' + layui.language.get("disable") + '</button>';//禁用
                        }else if(d.state == 1){
                            operateHtml += '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="enable">' + layui.language.get("enable") + '</button>';//启用
                        }
                        operateHtml += '<button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="subAuth">' + layui.language.get("sub_user_auth") + '</button>';//子账户授权
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
        ,addSubUser : function () {
            dataStore.set("for_user_manage_type",0);
            var parentUserAccount = dataStore.get("for_parent_user_account");
            var companyId = dataStore.get("for_parent_company_id");
            var createrId = dataStore.get("for_parent_user_id");
            var addSubUser = {"parentUserAccount":parentUserAccount,"createrId":createrId,"userAccount":"","username":"","companyId":companyId,"userPhone":"","password":"","userId":"","descr":"","state":"0","type":"0","title":layui.language.get("add_sub_user")};//添加子用户
            $jq.post("role/getRolesByLevel",{"roleLevel":3},function (data) {
                var roles = data.data;
                $jq.post("app/getCommodityById",{"companyId":companyId},function (data) {
                    layui.subUserManage.addOrUpdateSubUser(roles,data.data,addSubUser);
                });
            });
        }
        /**
         * 添加或编辑弹出框
         */
        ,addOrUpdateSubUser : function (roles,commoditys,editSubUser) {
            var html = addOrUpdateSubUser.innerHTML;
            html = html.replace("addOrUpdateSubUserForm","addOrUpdateSubUserForm2");
            var htmlStr = laytpl(html).render(editSubUser);
            layer.open({
                type:1,
                area:'600px',
                title:editSubUser.title,
                content:htmlStr
            });
            layui.language.render("addOrUpdateSubUserForm2");
            var roleHtml = '';
            $jq.each(roles,function (i,item) {
                roleHtml += '<option value="' + item.roleId + '">' + item.roleName + '</option>';
            });
            $jq("#roleIdThree").html(roleHtml);
            if(editSubUser.roleId){
                $jq("#roleIdThree").val(editSubUser.roleId);
            }
            var commodityHtml = '<option value="">' + layui.language.get("app") + '</option>';//所属应用
            $jq.each(commoditys,function (i,item) {
                commodityHtml += '<option value="' + item.id + '">' + item.commodityName + '</option>';
            });
            $jq("#commodityIdTwo").html(commodityHtml);
            if(editSubUser.commodityId){
                $jq("#commodityIdTwo").val(editSubUser.commodityId);
            }
            $jq("#parentUserAccount").html(editSubUser.parentUserAccount);
            if(editSubUser.type == 1){
                $jq("#userAccount").attr("disabled","");
                if(editSubUser.state == 0 ){
                    $jq("#enableSub").attr("checked","");
                    $jq("#disableSub").removeAttr("checked");
                }else{
                    $jq("#disableSub").attr("checked","");
                    $jq("#enableSub").removeAttr("checked");
                }
            }else{
                $jq("#userAccount").removeAttr("disabled");
            }
            form.render();
        }
        /**
         * 子账户授权
         */
        ,initMenuTree : function (dataOne) {
            debugger;
            var html = subAuth.innerHTML;
            var htmlStr = laytpl(html).render(dataOne);
            layer.open({
                type:1,
                area:['600px','400px'],
                title:layui.language.get("sub_user_auth"),//子账户授权
                content:htmlStr
            });
            var json = "";
            $jq.ajax({
                url: "user/subAuth",
                type: "post",
                async: false,  //设置为同步
                data: {"userId":dataOne.userId,"createrId":dataOne.createrId},
                dataType: "json",
                success: function (data) {
                    if(!ajaxCall(data)){
                        return;
                    }
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
         * 监听事件
         */
        ,tool : function () {
            //表格监听事件
            table.on("tool(subUserManageEvent)",function(obj){
                var dataOne = obj.data;//获取一行的数据
                //重置密码
                if(obj.event == "resetPwd"){
                    var html = resetPwdSub.innerHTML;
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
                    var parentUserAccount = dataStore.get("for_parent_user_account");
                    var companyId = dataStore.get("for_parent_company_id");
                    $jq.post("role/getRolesByLevel",{"roleLevel":3},function (data) {
                        var roles = data.data;
                        $jq.post("app/getCommodityById",{"companyId":companyId},function (data) {
                            dataOne.type = "1";
                            dataOne.title = layui.language.get("edit_sub_user");//编辑子用户
                            dataOne.companyId = companyId;
                            dataOne.parentUserAccount = parentUserAccount;
                            layui.subUserManage.addOrUpdateSubUser(roles,data.data,dataOne);
                        });
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
                                    layui.subUserManage.initView();
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
                                    layui.subUserManage.initView();
                                }
                            })
                        }
                    })
                }
                //子账户授权
                if(obj.event == "subAuth"){
                    layui.subUserManage.initMenuTree(dataOne);
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
                ,username : function (value) {
                    if(value.length <= 0){
                        return layui.language.get("username_not_empty");//用户姓名不能为空
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
                ,commodityId : function (value) {
                    if(value.length <= 0){
                        return layui.language.get("app_not_empty");//所属应用不能为空
                    }
                }
                ,confirmPassword : function (value) {
                    if($jq("#passwordSub").val() != value){
                        return layui.language.get("two_password_is_different");//两次密码输入不一致
                    }
                }
            });

            //表单监听提交事件（编辑或添加）
            form.on("submit(addOrUpdateSubUserForm)",function(data){
                debugger;
                console.log(data.field);
                $jq.post("user/addOrUpdateSubUser",data.field,function(data){
                    if(!ajaxCall(data)){
                        return;
                    }
                    layer.closeAll();
                    layui.subUserManage.initView();
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
                $jq.post("user/editSubUserMenus",{"roleId":data.field.roleId,"userId":data.field.userId,"menuIds":Array.from(new Set(menuIds)).join(",")},function (data) {
                    if(!ajaxCall(data)){
                        return;
                    }
                    layer.closeAll();
                    layui.subUserManage.initView();
                    layer.msg(layui.language.get("save_success"),{icon:1,time:1000})//保存成功
                });
                return false;
            })
        }
    }

    exports('subUserManage', obj);

})