layui.define(["jquery","laytpl","form"],function (exports) {
    var $jq = layui.jquery,
        laytpl = layui.laytpl,
        form = layui.form;

    var obj = {
        /**
         * 页面初始化
         */
        initView : function(){
            layui.use("language",function(){
                layui.language.render("subLan");
                layui.language.render("operate");
                layui.language.render("navigation")
                layui.index.loadMenu();//加载菜单栏
                dataStore.set("menu-control","close");
                layui.index.initCurrLan();//显示当前语言
                layui.index.displayName();//显示用户姓名
                layui.index.getParentAccount();//得到父级账号
                layui.index.loadRole();//获取角色级别
            })
        }
        /**
         * 获取角色id
         */
        ,loadRole : function(){
            $jq.post("role/getCurrRole","",function(data){
                if(!ajaxCall(data)){
                    return;
                }
                dataStore.set("for_role_level",data.data.roleLevel);
            });
        }
        /**
         * 加载菜单栏
         */
        ,loadMenu : function () {
            // $jq.getJSON("../bamboo/json/menu.json","",function(data){
            $jq.post("menu/getCurrMenus","",function (data) {
                if(data.code == "200"){
                    if(!data.data){
                        window.location.href = "login.html";
                        return;
                    }
                    var d = data.data;
                    var menu = {};
                    menu.html = '<ul class="layui-nav layui-nav-tree"  lay-filter="test">';
                    layui.index.parentMenu(menu,d);//加载父菜单
                    menu.html += '</ul>';
                    $jq("#menu").html(menu.html);
                    layui.element.render('nav');
                    layui.index.menuClick();//菜单栏点击事件
                    layui.index.clickIndex(1);//F5刷新事件
                }
            })
        }
        /**
         * 加载父菜单
         */
        ,parentMenu : function (menu,data) {
            layui.use(['language'],function(language){
                for(var index in data){
                    menu.html += '<li class="layui-nav-item" id="'+ data[index].icon +'">'
                        + '<a class="" href="javascript:layui.index.menuChange(\'open\',\''+ data[index].icon +'\',\'1\');">'
                        // + '<i class="icon_menu '+ data[index].icon +'"></i>'
                        + '<img class="icon_menu" src="img/'+ data[index].icon +'.png">'
                        + layui.language.get(data[index].icon)
                        + '</a>';
                    layui.index.childrenMenu(menu,data[index].children);//加载子菜单
                    menu.html += '</li>';
                }
            });
        }
        /**
         * 加载子菜单
         */
        ,childrenMenu : function (menu,data) {
            layui.use("language",function(language){
                if(!data) return;
                menu.html += '<dl class="layui-nav-child">';
                for(var index in data){
                    menu.html += '<dd><a href="'+ data[index].url +'" data-href="'+ data[index].url +'">' + layui.language.get(data[index].url.substring(7,data[index].url.length - 5)) + '</a></dd>';
                }
                menu.html += '</dl>';
            });
        }
        /**
         * 菜单栏点击事件
         */
        ,menuClick : function () {
            $jq("#menu a[data-href]").on("click",function () {
                var href = $jq(this).attr("data-href");
                layui.index.loanMainHtml(href,"#main");//加载内容区域
            })
        }
        /**
         * F5刷新事件
         */
        ,clickIndex : function (type) {
            var currUrlArr = location.href.split("#");
            if(currUrlArr.length == 2){
                var currUrl = currUrlArr[1];
                $jq('#menu a[data-href="#' + currUrl + '"]').click();
                $jq('a[data-href="#' + currUrl + '"]').parents("li").addClass("layui-nav-itemed");
                layui.index.menuChange("open",$jq('a[data-href="#' + currUrl + '"]').parents("li").prop("id"),type);
                return;
            }
            $jq('#menu a[data-href="#views/homeIndex.html"]').click();
            $jq('#menu a[data-href="#views/homeIndex.html"]').parents("li").addClass("layui-nav-itemed");
            layui.index.menuChange("open","homeIndex",type);
        }
        /**
         * 加载内容区域
         */
        ,loanMainHtml : function (url,id) {
            var url = url.replace("#","");
            $jq.get(url,"random=" + Math.random(),function (data) {
                $jq(id).html(data);
            })
        }
        /**
         * 菜单栏选中并切换图片
         */
        ,menuChange : function(command,id,type){
            var flag = $jq("#" + id).prop("open")+"";
            var isOpen = flag=='open';
            if(type == 1){
                if(isOpen){
                    $jq("#" + id + " .icon_menu").prop("src","img/" + id + ".png?ran="+Math.random());
                    $jq("#" + id).prop("open","close");
                }else{
                    $jq("#" + id + " .icon_menu").prop("src","img/" + id + "-active.png?ran="+Math.random());
                    $jq("#" + id).prop("open","open");
                }
            }else {
                if(isOpen){
                    $jq("#" + id + " .icon_menu").prop("src","img/" + id + "-active.png?ran="+Math.random());
                }else{
                    $jq("#" + id + " .icon_menu").prop("src","img/" + id + ".png?ran="+Math.random());
                }
            }
            var dis = $jq(".menu-close").css("display");
            if(command && dis == "block"){
                return;
            }
            layui.index.menuControl(command);
        }
        /**
         * 菜单开关控制
         */
        ,menuControl : function (command) {
            if(!command){
                command = dataStore.get("menu-control");
            }
            if("open" == command){
                $jq(".layui-layout-admin .layui-side").css("width","240px");
                $jq(".layui-side-scroll").css("width","240px");
                $jq(".layui-nav-tree").css("width","240px");
                $jq(".layui-body").css("left","240px");
                $jq(".layui-side .layui-nav-more").show();
                $jq('.layui-side .layui-nav-itemed .layui-nav-child').show();
                $jq('.layui-side .layui-nav-itemed .layui-nav-child').css("display","");

                $jq(".menu-close").css("display","block");
                dataStore.set("menu-control","close");
            }else{
                $jq(".layui-layout-admin .layui-side").css("width","50px");
                $jq(".layui-side-scroll").css("width","60px");
                $jq(".layui-nav-tree").css("width","60px");
                $jq(".layui-body").css("left","60px");
                $jq(".layui-side .layui-nav-more").hide();
                $jq('.layui-side .layui-nav-itemed .layui-nav-child').hide();
                $jq(".menu-close").css("display","none");
                dataStore.set("menu-control","open");
            }
            var curUrlArr = location.href.split('#');
            $jq('#menu a[data-href="#'+curUrlArr[1]+'"]').click();

        }
        /**
         * 个人信息
         */
        ,clickPersonal : function () {
            $jq('#menu a[data-href="#views/personalInfo.html"]').click();
        }
        /**
         * 退出
         */
        ,logout : function(){
            layer.open({
                title: [layui.language.get("information")],//信息
                content: layui.language.get("confirm_exit"),//确认退出吗?
                btn: [layui.language.get("certainly"),layui.language.get("cancel")],//确定   取消
                yes: function(){
                    $jq.post("login/logout","",function(data){
                        if(data.code == "200"){
                            layer.closeAll();
                            window.location.href="login.html";
                        }
                    })
                }
            })
        }
        /**
         * 展示用户姓名
         */
        ,displayName : function () {
            $jq.post("user/getUsername","",function (data) {
                if(data.code == "200"){
                    $jq("#display-name").text(data.data.username);
                    dataStore.set('for_edit_Pwd',data.data.userId);
                }
            })
        }
        /**
         * 得到父级账号
         */
        ,getParentAccount : function () {
            $jq.post("user/getParentAccount","",function (data) {
                if(data.code == "200"){
                    var d = data.data;
                    if(d){
                        dataStore.set('for_parent_user_account',data.data.userAccount);
                    }
                }
            })
        }
        /**
         * 修改密码
         */
        ,editPwd : function () {
            layui.use("language",function(language){
                var userId = dataStore.get('for_edit_Pwd');
                var parentUserAccount = dataStore.get('for_parent_user_account');
                // if(!id){
                //     layer.alert('请先登录再尝试此操作！');
                //     return;
                // }
                var html = editPwd.innerHTML;
                html = html.replace("editPwdForm","editPwdForm2");
                var htmlStr = laytpl(html).render({'userId':userId,'parentUserAccount':parentUserAccount});
                layer.open({
                    type:1,
                    area:'600px',
                    title:[language.get("edit_password"),'text-align:center;font-size:18px;'],
                    content:htmlStr
                });
                language.render("editPwdForm2");
            })
        }
        /**
         * 显示当前语言
         */
        ,initCurrLan : function () {
            var currLan = dataStore.get('current_lan');
            layui.use(['language'],function(language) {
                if (currLan == 'cn') {
                    $jq('#currLan').text(language.get('index_cn'));
                } else if (currLan == 'en') {
                    $jq('#currLan').text(language.get('index_en'));
                }


                $jq('.sub-lan li').each(function(){
                    if($jq(this).text() == $jq('#currLan').text()){
                        $jq(this).children('span').css({'color':'#4679EF','background-color':'rgba(95,158,252,0.14)'});
                    }
                });
            });

        }
        /**
         * 监听事件
         */
        ,tool : function () {
            //监听表单验证
            form.verify({
                oldPwd : function(value){
                    if(value.length == 0){
                        return layui.language.get("original_password_not_empty");//原密码不能为空
                    }
                    var password = $jq('#oldPwd').val();
                    var userId = dataStore.get('for_edit_Pwd');
                    var code = "";
                    var msg = "";
                    $jq.ajax({
                        url: "user/checkPwd",
                        type: "post",
                        async: false,  //设置为同步
                        data: {
                            password:password,
                            userId:userId
                        },
                        dataType: "json",
                        success: function (data) {
                            code = data.code;
                            msg = data.message;
                        }
                    });
                    if(code!='200'){return msg;}

                },
                newPwd : function(value){
                    if(value.length == 0){
                        return layui.language.get("new_password_not_empty");//新密码不能为空
                    }
                },
                confirmPwd : function(value){
                    if($jq("#newPwd").val() != value){
                        return layui.language.get("two_password_is_different");//两次密码输入不一致
                    }
                }
            });
            //监听表单提交
            form.on('submit(editPwdForm)',function(data){
                //得到表单里面数据的新密码以及userId
                var userId = data.field.userId;
                var newPwd = data.field.newPwd;
                $jq.post('user/editPwd/',{'userId':userId,'password':newPwd},function(data){
                    if(!ajaxCall(data)){
                        return;
                    }
                    layer.msg(layui.language.get("save_success"),{icon:1,time:1000},function(){//保存成功
                        layer.closeAll();
                        layui.applicationManage.initView();
                    });
                });
                //至关重要，防止表单提交
                return false;
            });
        }
        ,switchLanguage : function (lan) {
            layui.use(['language'],function(language){
                language.changeLan(lan);
            });
        }
    }

    exports('index', obj);

})