//package cn.wlh.interceptor;
//
//import cn.wlh.util.SessionUtil;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Component
//public class RiskInterceptor implements HandlerInterceptor {
//
//    @Resource
//    SessionUtil sessionUtil;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
//        System.out.print("进入preHandle。。。。。。。。。。");
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
//
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
//
//    }
//}
