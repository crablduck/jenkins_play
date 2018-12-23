layui.define(["jquery","form","table","laytpl"],function (exports) {
    var $jq = layui.jquery,
        form = layui.form,
        laytpl = layui.laytpl;

    var obj = {
        /**
         * 页面初始化
         */
        initView : function() {
            layui.language.render("menuManage");
            layui.language.render("view");
            this.initTable();//菜单栏表格

            form.render();//表单渲染
        }
        /**
         * 菜单栏表格
         */
        ,initTable : function () {
            $jq.post("menu/getMenuList","",function(data){
                if(!ajaxCall(data)){
                    return;
                }
                $jq.each(data.data,function(i,item){
                    if(item.parentId == 0){
                        item.menuName = layui.language.get(item.menuIcon);
                    }else{
                        item.menuName = layui.language.get(item.menuUrl.substring(7,item.menuUrl.length - 5));
                    }
                });
                var allData = data.data;
                layui.use(['laytpl', 'grid', 'treeGrid'], function () {
                    var laytpl = layui.laytpl,
                        treegrid = layui.treeGrid;
                    treegrid.config.render = function (viewid, data) {
                        var view = document.getElementById(viewid).innerHTML;
                        return laytpl(view).render(data) || '';
                    };

                    var treeForm = treegrid.createNew({
                        id: 'menuId',
                        elem: 'permTable',
                        view: 'view',
                        data: {rows: allData},
                        parentid: 'parentId',
                        singleSelect: false
                    });
                    treeForm.build();
                })
            })
        }
        /**
         * 添加菜单---》》》添加弹出框
         */
        ,viewAddMenu : function () {
            var addMenu = {"menuId":"","parentId":"","menuName":"","menuUrl":"","menuType":"0","menuIcon":"","orderNum":"","state":"","type":"0","title":layui.language.get("add_menu")};//添加菜单
            $jq.post("menu/getContentList","",function(data){
                layui.menuManage.addOrUpdateMenu(data,addMenu);
            })
        }
        /**
         * 添加或编辑菜单
         */
        ,addOrUpdateMenu : function (contents,editMenu) {
            var html = addOrUpdateMenu.innerHTML;
            html = html.replace("addOrUpdateMenuForm","addOrUpdateMenuForm2");
            var htmlStr = laytpl(html).render(editMenu);
            layer.open({
                type:1,
                area:'600px',
                title:editMenu.title,
                content:htmlStr
            });
            layui.language.render("addOrUpdateMenuForm2");
            //编辑时
            if(editMenu.type == 1){
                if(editMenu.menuType == 0){
                    $jq("#addOrUpdateMenuForm input[name=menuType][value=0]").attr("checked","");
                    $jq("#addOrUpdateMenuForm input[name=menuType][value=1]").removeAttr("checked");
                }else {
                    $jq("#addOrUpdateMenuForm input[name=menuType][value=1]").attr("checked","");
                    $jq("#addOrUpdateMenuForm input[name=menuType][value=0]").removeAttr("checked");
                }
                $jq("#addOrUpdateMenuForm input[name=menuType]").attr("disabled","");
                //状态类型
                if(editMenu.state == 0){//若为目录时
                    $jq("#addOrUpdateMenuForm input[name=state][value=0]").attr("checked","");
                    $jq("#addOrUpdateMenuForm input[name=state][value=1]").removeAttr("checked");
                }else{
                    $jq("#addOrUpdateMenuForm input[name=state][value=0]").removeAttr("checked");
                    $jq("#addOrUpdateMenuForm input[name=state][value=1]").attr("checked","");
                }
            }else{
                $jq("#addOrUpdateMenuForm input[name=menuType]").removeAttr("disabled");
            }
            layui.menuManage.display(editMenu.menuType);
            //初始化所属目录
            var contentHtml = '<option value="">' + layui.language.get("please_select") + '</option>';//请选择
            $jq.each(contents,function(i,item){
                contentHtml += '<option value="' + item.menuId + '">' + item.menuName + '</option>';
            });
            $jq("#ownContent").html(contentHtml);
            if(editMenu.parentId != 0 && editMenu.menuId){
                $jq("#ownContent").val(editMenu.parentId);
            }
            //显示对应的输入框

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
         * 编辑菜单---》》》编辑弹出框
         */
        ,editMenu : function (menuId) {
            var contents = null;
            var editMenu = null;
            $jq.post("menu/getMenuById/" + menuId,"",function(data){
                editMenu = data;
                editMenu["type"] = 1;
                editMenu["title"] = layui.language.get("edit_menu");//编辑菜单
                $jq.post("menu/getContentList","",function(data){
                    contents = data;
                    layui.menuManage.addOrUpdateMenu(contents,editMenu);
                })
            })
        }
        /**
         * 删除菜单
         */
        ,delMenu : function (menuId) {
            layer.open({
                title: [layui.language.get("information")],//信息
                content: layui.language.get("confirm_del"),//确认删除吗？
                btn: [layui.language.get("certainly"),layui.language.get("cancel")],//确定   取消
                yes: function(index){
                    $jq.post("menu/delMenuById/" + menuId,"",function(data){
                        if(data){
                            layer.msg("操作完成",{icon:1,time:1000},function(){
                                layer.closeAll();
                                layui.menuManage.initView();
                            });
                        }
                    })
                }
            })
        }
        /**
         * 监听事件
         */
        ,tool : function () {
            //菜单类型选中事件
            form.on("radio(menuType)",function(data){
               layui.menuManage.display(data.value);
            });

            //表单监听提交事件
            form.on("submit(addOrUpdateMenuForm)",function(data){
                if(data.field.menuType == 0){
                    data.field.menuName = data.field.contentName;
                }
                if(data.field.menuType == 1){
                    data.field.parentId = data.field.ownContent;
                }
                $jq.post("menu/addOrUpdateMenu",data.field,function(data){
                    if(!ajaxCall(data)){
                        return;
                    }
                    layer.closeAll();
                    layui.menuManage.initView();
                    layer.msg(layui.language.get("save_success"),{icon:1,time:1000})//保存成功
                });
                return false;
            });
        }
    }

    exports('menuManage', obj);

})