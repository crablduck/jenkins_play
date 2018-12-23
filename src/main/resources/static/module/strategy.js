layui.define(["jquery","form","table","laytpl"],function (exports) {
    var $jq = layui.jquery,
        form = layui.form,
        laytpl = layui.laytpl,
        table = layui.table;



    var obj = {
        /**
         * 页面初始化
         */
        initView : function(){
            form.render();//表单渲染
            //初始化表格
            this.initTable();
        }
        /**
         * 初始化表格
         */
        ,initTable : function () {


            table.render({
                elem: "#strategyManageTable"
                //本来下面三个属性解决跨域问题， 但是因为异步访问导致访问问题
                ,async: false
                 ,xhrFields: {
                        withCredentials: true
                    }
                ,crossDomain: true
                ,time: 1000
                ,url: 'http://localhost:8000/rule/getStrategyManages'  //数据请求路径
                ,page: true
                ,loading: true
                ,where: {
                    strategy: $jq("#strategy_name").val()
                    ,status: $jq("#status").val()
                }
                ,cols: [[
                    {field: 'id', title: '顺序'}
                    ,{field: 'strategy', title: '策略'}
                    ,{field: 'strategyNum', title: '策略数目'}
                    ,{field: 'ruleNum', title: '规则数目'}
                    ,{field: 'description', title: '描述'}
                    ,{fixed: 'right', title: '状态', align: 'center', width:100, templet: function (d) {
                             if ( d.status == 0){
                               return '启用'
                             }else {
                                 return '禁用'
                             }
                        }}
                    ,{fixed: 'right', title: '操作', align: 'center', width:250, templet: function (d) {
                            var status =  null;
                            if ( d.status == 0){
                                status =  '启用'
                            }else {
                                status = '禁用'
                            }
                        return '<button  class="layui-btn layui-btn-sm layui-btn-normal" onclick="layui.strategyManage.editStrategy('+d.id+')">编辑</button>' +
                            '<button class="layui-btn layui-btn-danger layui-btn-sm" onclick="layui.strategyManage.delStrategy('+d.id+')">删除</button>' +
                            '<button class="layui-btn layui-btn-sm" onclick="layui.strategy.viewRule('+d.id+')">策略</button>' +
                            '<button class="layui-btn layui-btn-sm layui-btn-warm" onclick="layui.strategyManage.useStrategyManage('+d.id+ ','+ d.status+')">'+status+'</button>'

                    }}
                ]]
                ,request: {
                        pageName: 'page',
                        limitName: 'size'
                }
                ,limit: 10
                ,limits: [10, 20, 30, 40, 50]
            })
        }
        /**
         * 删除菜单
         */
        ,delStrategy : function (strategyId) {
            debugger
            layer.open({
                title: ["信息"],
                content: "确认删除吗?",
                btn: ["确定","取消"],
                yes: function(index){
                    $jq.post("http://localhost:8000/rule/deleteStrategyManage/" + strategyId,"",function(data){
                        if(data){
                            layer.msg("操作完成",{icon:1,time:1000},function(){
                                layer.closeAll();
                                layui.strategyManage.initView();
                            });
                        }删除
                    })
                }
            })
        }
        /**
         * 添加规则---》》》添加弹出框
         */
        ,viewAddStrategy : function () {

            var editStrategy= {"id":"","description":"","strategy":"","type":"0","title":"添加/编辑策略集"};
            $jq.post("http://localhost:8000/rule/getRuleList","",function(data){
                layui.strategyManage.addOrUpdateStrategyManage(data,editStrategy);
            })
        }
        /**
         * 添加或编辑规则
         */
        ,addOrUpdateStrategyManage : function (contents, editStrategy) {

            var html = addOrUpdateStrategy.innerHTML;
            var htmlStr = laytpl(html).render(editStrategy);
            layer.open({
                type:1,
                area:'700px',
                title:[editStrategy.title,'text-align:center;font-size:18px;'],
                content:htmlStr
            });
            //编辑时
             if(editStrategy.type == 1) {

                 if (editStrategy.data.id != null) {
                     $jq("#strategyId").attr("value", editStrategy.data.id)
                 }

                 //修改名字的值
                 if (editStrategy.data.strategy != null) {
                     $jq("#strategy").attr("value", editStrategy.data.strategy)
                 }

                 //修改描述的值
                 if (editStrategy.data.description != null) {
                     $jq("#description").text(editStrategy.data.description )
                 }
                 
                 //状态类型
                 if (editStrategy.data.status == 0) {//若为目录时
                     //TODO::存在兼容性问题
                     $jq("#addOrUpdateRuleForm input[name=status][value=0]").attr("checked", "");
                     $jq("#addOrUpdateRuleForm input[name=status][value=1]").removeAttr("checked");
                 }else{
                     $jq("#addOrUpdateRuleForm input[name=status][value=0]").removeAttr("checked");
                     $jq("#addOrUpdateRuleForm input[name=status][value=1]").attr("checked","");
                 }

                 // //显示对应的输入框
             }
            form.render();
        }
        /**
         * 显示对应的输入框
         */
        ,display : function (menuType) {
            if(menuType == 1){
                $jq(".menu-form").show();
                $jq(".content-form").hide();
            }else{
                $jq(".menu-form").hide();
                $jq(".content-form").show();
            }
        }
        /**
         * 编辑规则---》》》编辑弹出框
         */
        ,editStrategy: function (strategyId) {
            var editStrategy= null;

            console.log('this is rule Id :'+strategyId)

            $jq.post("http://localhost:8000/rule/getStrategyById/"+strategyId,"",function(data){
                editStrategy = data;

                console.log(data)
                editStrategy["type"] = 1;
                editStrategy["title"] = "编辑策略集合";
                layui.strategyManage.addOrUpdateStrategyManage(data, editStrategy);
            })

        }
        ,viewSt: function (ruleId) {

            $jq.post("http://localhost:8000/rule/getRuleById/"+ruleId,"",function(viewRule){
                viewRule["title"] = "查看规则";
                debugger
                console.log(viewRule)
                var html = viewTheRule.innerHTML;
                var htmlStr = laytpl(html).render(viewRule);
                layer.open({
                    type:1,
                    area:'700px',
                    title:[viewRule.title,'text-align:center;font-size:18px;'],
                    content:htmlStr
                });
            })
        }
         ,useStrategyManage: function (strategyManageId, status) {

            console.log("hello")

            if(status == 0){
                status = 1;
            }else{
                status = 0;
            }
            $jq.post("http://localhost:8000/rule/updateStrategyMStatus/"+strategyManageId, "",function(data){
                if(!ajaxCall(data)){
                    return;
                }
                layer.msg("操作完成",{icon:1,time:1000},function(){
                    layer.closeAll();
                    layui.strategyManage.initView();
                });
            })
        }


        /**
         * 监听事件
         */
        ,tool : function () {
            //表单监听提交事件
            form.on("submit(addOrUpdateStrategyForm)",function(data){
                    console.log(data.field);
                    $jq.post("http://localhost:8000/rule/addOrUpdateStrategyManage",data.field,function(data){
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