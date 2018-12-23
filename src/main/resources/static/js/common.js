var dataStore  = {
    get:function(key) {
        return localStorage.getItem(key);
        var obj = localStorage.getItem(key);
        if(null==obj) return null;
        return obj[key];
    },
    set:function(key,v) {
        localStorage.setItem(key,v);
    },
    del: function(k) {
        localStorage.removeItem(k);
    }
}
function ajaxCall(data) {
    if(!data) return;
    if(data.code == 200){
        return true;
    }
    if(data.code == '500' || data.code == '403'){
        layer.open({
            title: layui.language.get("prompt"),           //'提示'
            content: data.message,
            btn : [layui.language.get("certainly")]		//按钮
         });
        return false;
    }
    return true;

    // if(!data) return;
    // var d = null;
    // if( data.code) {
    //     d = data;
    // }else {
    //     try {
    //         d = JSON.parse(data);
    //     }catch(e){
    //         d = {};
    //     }
    // }
    // if(d.code=='200' ) {
    //     return true;
    // }
    // if(d.code=='401' || d.code=='402' || d.code=='403') {
    //     layer.open({
    //         title: layui.language.get('prompt'),           //'提示'
    //         content: d.message,
    //         btn : [ layui.language.get('certainly')]		//按钮
    //     });
    //     return false;
    // }
    // if(d.code=='500' || d.code=='201') {
    //     layer.open({
    //         title: layui.language.get('prompt'),           //'提示'
    //         content: d.message||layui.language.get('error_tips'),
    //         btn : [ layui.language.get('certainly')]		//按钮
    //     });
    //     return false;
    // }
    // if(d.code=='800') {
    //     location.href='login.html';
    //     return false;
    // }
    // return true;
}
/**
 * 当前时间
 * @param inputTime
 * @returns
 */
function formatDateTime() {
    var date = new Date();
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    return y + '-' + m + '-' + d;
}
/**
 * 七天前的时间
 * @param inputTime
 * @returns
 */
function formatDateTime7() {
    var date = new Date();
    date.setDate(date.getDate() - 6);
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    return y + '-' + m + '-' + d;
};
