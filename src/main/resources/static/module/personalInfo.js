layui.define(["jquery","form","table","laytpl"],function (exports) {
    var $jq = layui.jquery,
        laytpl = layui.laytpl;



    var obj = {
        /**
         * 页面初始化
         */
        initView : function(){
            layui.language.render("mainIndex");
            layui.use("language",function(language){
                $jq.post("user/getUserInfo","",function (data) {
                    if(data.code == 200){
                        var html = personalInfo.innerHTML;
                        var htmlStr = laytpl(html).render(data.data);
                        $jq("#userInfo").html(htmlStr);
                        language.render("userInfo");
                    }
                })
            });
        }

    }


    exports('personalInfo', obj);

})