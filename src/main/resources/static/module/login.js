layui.define(["jquery","form","language"],function (exports) {
    var $jq = layui.jquery,
        form = layui.form,
        language = layui.language;

    var obj = {
        /**
         * 初始化页面
         */
        initView : function () {
            this.initLan();//初始化语言并存入缓存
            //获取验证码
            layui.use(['language'],function(language){
                $jq('#user-login').html(language.get('user_login'));
                $jq('#login-username').attr('placeholder',language.get('please_enter_username'));
                $jq('#login-password').attr('placeholder',language.get('please_enter_password'));
                $jq('#verification').attr('placeholder',language.get('please_enter_verification'));
                $jq('#keep-login').attr('title',language.get('keep_me_logged_in'));
                $jq('#userLogin').text(language.get('log_in'));
                form.render();
            });
            drawPic();
            // 给用户名和密码输入框赋值
            this.assignValue();
            //给登录按钮绑定enter事件
            $jq(document).keydown(function (e) {
                if (e.keyCode === 13) {
                    $jq("#userLogin").trigger("click");
                }
            });
        }
        /**
         * 初始化语言并存入缓存中
         */
        ,initLan : function(){
            if(!dataStore.get('current_lan')){//缓存语言为空时，则默认为当前浏览器的语言
                var lan = navigator.language.toLowerCase();
                if(lan.indexOf('cn') != -1){
                    dataStore.set('current_lan','cn');
                }else if(lan.indexOf('en') != -1){
                    dataStore.set('current_lan','en');
                }
            }
        }
        /**
         *给用户名和密码输入框赋值
         */
        ,assignValue : function () {
            var username = layui.login.getCookieValue('username');
            var password  = layui.login.getCookieValue('password');
            if(username !== null){
                $jq("input[name='username']").val(username);
            }
            if(password !== null){
                $jq("input[name='password']").val(password);
            }
        }
        /**
         * 获取Cookie中对应的值
         */
        ,getCookieValue : function (name) {
            //读cookie属性，这将返回文档的所有cookie
            var allcookies = document.cookie;
            //查找名为name的cookie的开始位置
            name += "=";
            var pos = allcookies.indexOf(name);
            //如果找到了具有该名字的cookie，那么提取并使用它的值
            if (pos != -1){                       //如果pos值为-1则说明搜索"version="失败
                var start = pos + name.length;         //cookie值开始的位置
                var end = allcookies.indexOf(";",start);    //从cookie值开始的位置起搜索第一个";"的位置,即cookie值结尾的位置
                if (end == -1) end = allcookies.length;    //如果end值为-1说明cookie列表里只有一个cookie
                var value = allcookies.substring(start,end); //提取cookie的值
                return (value);              //对它解码
            }
            else return "";                //搜索失败，返回空字符串
        }
        /**
         * 监听事件
         */
        ,tool : function () {
            //监听表单校验事件
            form.verify({
                username : function (value) {
                    if(value.length <=0 ){
                        return layui.language.get("username_not_empty");//用户名不能为空
                    }
                }
                ,password : function (value) {
                    if(value.length <=0 ){
                        return layui.language.get("password_not_empty");//密码不能为空
                    }
                }
                ,verification : function (value) {
                    if(value.length <=0 ){
                        return layui.language.get("verification_not_empty");//验证码不能为空
                    }
                    var picCode = $jq("#picCode").val();
                    if(value.toLowerCase() != picCode.toLowerCase()){
                        return layui.language.get("verification_id_error");//验证码错误
                    }
                }
            });

            //监听表单提交事件
            form.on("submit(userLogin)",function (data) {
                var field = data.field;
                var username = field.username;
                var password = field.password;
                $jq.post("login/doLogin",{"userAccount":username,"password":password},function(data){
                    if(data.code == "200"){
                        if(field.remember){
                            layui.login.addCookie("username",username,24,"/");
                            layui.login.addCookie("password",password,24,"/");
                        }
                        layer.msg(layui.language.get("login_success"), {//登陆成功
                            icon: 1,
                            time: 500    //0.5秒关闭（如果不配置，默认是3秒）
                        }, function(){
                            window.location.href='index.html';
                        });
                    }else if(data.code == 800 || data.code == 402 || data.code == 400){
                        //800:账号已被锁，请联系管理员,402:账号不存在,400:密码错误
                        layer.msg(layui.language.get(data.code), {icon: 2});
                        drawPic();
                    }else {
                        layer.msg(data.message, {icon: 2,time:1000});
                        drawPic();
                    }
                });
                return false;
            })
        }
        /**
         *  将数据存入cookie中
         */
        ,addCookie : function(name,value,days,path){   /**添加设置cookie**/
            var expires = new Date();
            expires.setTime(expires.getTime() + days * 3600000 * 24);
            //path=/，表示cookie能在整个网站下使用，path=/temp，表示cookie只能在temp目录下使用
            path = path == "" ? "" : ";path=" + path;
            //GMT(Greenwich Mean Time)是格林尼治平时，现在的标准时间，协调世界时是UTC
            //参数days只能是数字型
            var _expires = (typeof days) == "string" ? "" : ";expires=" + expires.toUTCString();
            document.cookie = name + "=" + value + _expires + path;
        }
    }

    exports('login', obj);

})