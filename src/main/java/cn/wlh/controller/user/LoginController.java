package cn.wlh.controller.user;

import cn.wlh.model.SystemUser;
import cn.wlh.service.SystemUserService;
import cn.wlh.util.SessionUtil;
import cn.wlh.vo.SimpleMsgVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 登录控制层
 */
@RestController
@RequestMapping("login")
public class LoginController {

    @Resource
    SystemUserService systemUserServiceImpl;

    @Resource
    SessionUtil sessionUtil;

    /**
     * 用户登录
     * @param systemUser
     * @return
     */
    @RequestMapping("doLogin")
    public Object doLogin(SystemUser systemUser){
        try{
            return systemUserServiceImpl.checkUser(systemUser);
        }catch (Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

    /**
     * 用户登出
     * @return
     */
    @RequestMapping("logout")
    public Object logout(){
        try{
            sessionUtil.logout();
        }catch (Exception e){
            SimpleMsgVO.get500Fail();
        }
        return SimpleMsgVO.getOk();
    }

}
