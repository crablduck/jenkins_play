layui.define(["jquery","form","table"],function (exports) {
    var $jq = layui.jquery,
        form = layui.form,
        table = layui.table;
        lan = layui.language;


    var obj = {
        /**
         * 页面初始化
         */
        initView : function(){

            form.render();//表单渲染
            lan.render("fieldManage")
            this.selectOpt();
            //初始化表格
            this.initTable();
        }
        /**
         * 初始化表格
         */
        ,initTable : function () {


            table.render({
                elem: "#fieldManageTable"
                //本来下面三个属性解决跨域问题， 但是因为异步访问导致访问问题
                ,async: false
                 ,xhrFields: {
                        withCredentials: true
                    }
                ,crossDomain: true
                ,time: 1000
                ,url: 'field/getFieldList'  //数据请求路径
                ,page: true
                ,loading: true
                ,where: {
                    fieldName: $jq("#field_name").val()
                    ,fieldType: $jq("#field_type").val()
                }
                ,cols: [[
                    {field: 'fieldId', title: layui.language.get("field_id")}
                    ,{field: 'fieldName', title: layui.language.get("field_name")}
                    ,{field: 'displayName', title: layui.language.get("display_name")}
                    ,{field: 'fieldType', title: layui.language.get("field_type")}
                    ,{field: 'description', title: layui.language.get("discription")}
                ]]
                ,request: {
                        pageName: 'current',
                        limitName: 'size'
                }
                ,limit: 10
                ,limits: [10, 20, 30, 40, 50]
            })
        }




        ,selectOpt : function () {
            debugger

            var strOpt = '<option value="" disabled="disabled" selected>'+ lan.get("choose_field")+'</option>';
                strOpt += '<option value="数值型" >'+lan.get("num_type")+'</option> \n <option value="字符串" >'+lan.get("string")+'</option>';
            $jq("#field_type").append(strOpt);
            form.render();

        }

        /**
         * 监听事件
         */
        ,tool : function () {


        }

    }


    exports('fieldManage', obj);

})