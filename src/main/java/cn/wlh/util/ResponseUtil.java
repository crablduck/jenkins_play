package cn.wlh.util;

import cn.wlh.vo.Response;

public class ResponseUtil {

    public static final Integer SERVERFAIL= 500;
    public static final Integer SUCCESS = 200;
    public static final Integer PARAMFAIL = 400;

    public static Response failRep() {
        Response response = new Response();
        response.setData(null);
        response.setMessage("服务器出错");
        response.setStatus(SERVERFAIL);
        return response;
    }

    public static Response successRep(Object object) {
        Response response = new Response();
        response.setData(object);
        response.setMessage("成功");
        response.setStatus(SUCCESS);
        return response;
    }

    public static Response paramFail() {
        Response response = new Response();
        response.setData(null);
        response.setMessage("请求参数错误");
        response.setStatus(PARAMFAIL);
        return response;
    }
}
