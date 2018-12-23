package cn.wlh.controller.user;

import cn.wlh.model.SystemUser;
import cn.wlh.service.SystemUserService;
import cn.wlh.util.Md5Util;
import cn.wlh.util.SessionUtil;
import cn.wlh.vo.SimpleMsgVO;
import cn.wlh.vo.UserInfoReq;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("user")
public class SystemUserController {

    @Resource
    SystemUserService systemUserServiceImpl;

    @Resource
    SessionUtil sessionUtil;

    /**addOrUpdateUser
     * 获取当前用户姓名
     */
    @RequestMapping("getUsername")
    public Object getUsername(){
        JSONObject json = new JSONObject();
        json.put("username", sessionUtil.getUserInfo().getUsername());
        json.put("userId", sessionUtil.getUserInfo().getUserId());
        return SimpleMsgVO.getOk(json);
    }

    /**
     * 得到父级账号信息
     */
    @RequestMapping("getParentAccount")
    public Object getParentAccount(){
        SystemUser systemUser = systemUserServiceImpl.selectById(sessionUtil.getUserInfo().getCreaterId());
        return SimpleMsgVO.getOk(systemUser);
    }

    /**
     * 检查密码是否正确
     * @param userId
     * @param password
     * @return
     */
    @RequestMapping("checkPwd")
    public Object checkPwd(Integer userId,String password){
        Wrapper<SystemUser> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        wrapper.eq("password", Md5Util.md5(password));
        int count = systemUserServiceImpl.selectCount(wrapper);
        if(count > 0){
            return SimpleMsgVO.getOk();
        }
        return SimpleMsgVO.get401Fail("原密码错误，请重新输入！");
    }

    /**
     * 修改密码
     * @param userId
     * @param password
     * @return
     */
    @RequestMapping("editPwd")
    public Object editPwd(Integer userId,String password){
        try{
            return SimpleMsgVO.getOk(systemUserServiceImpl.editPwd(userId,password));
        }catch (Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

    /**
     * 获取用户相关信息
     * @return
     */
    @RequestMapping("getUserInfo")
    public Object getUserInfo(){
        try{
            return systemUserServiceImpl.selectUserInfoByUserId();
        }catch (Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }


    /**
     * 获取用户公司级用户
     * @return
     */
    @RequestMapping("getUserList")
    public Object getUserList(Pagination page, UserInfoReq userInfoReq){
        try{
            return systemUserServiceImpl.getUserList(page, userInfoReq);
        }catch (Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

    /**
     * 获取公司级用户下所有子账户
     * @return
     */
    @RequestMapping("getSubUserList")
    public Object getSubUserList(Pagination page, UserInfoReq userInfoReq){
        try{
            return systemUserServiceImpl.getSubUserList(page, userInfoReq);
        }catch (Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }


    /**
     * 添加或编辑公司级用户
     * @return
     */
    @RequestMapping("addOrUpdateUser")
    public Object addOrUpdateUser(UserInfoReq userInfoReq){
        try{
            return systemUserServiceImpl.addOrUpdateUser(userInfoReq);
        }catch (Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

    /**
     * 添加或编辑子账户
     * @return
     */
    @RequestMapping("addOrUpdateSubUser")
    public Object addOrUpdateSubUser(UserInfoReq userInfoReq){
        try{
            return SimpleMsgVO.getOk(systemUserServiceImpl.addOrUpdateSubUser(userInfoReq));
        }catch (Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

    /**
     * 检查用户账号是否存在
     * @param userAccount
     * @return
     */
    @RequestMapping("checkUserAccount")
    public Object checkUserAccount(String userAccount){
        try{
            return systemUserServiceImpl.checkUserAccount(userAccount);
        }catch (Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

    /**
     * 重置密码
     * @param userId
     * @return
     */
    @RequestMapping("resetPwd")
    public Object resetPwd(Integer userId){
        try{
            return systemUserServiceImpl.resetPwd(userId);
        }catch (Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

    /**
     * 编辑用户状态
     * @param systemUser
     * @return
     */
    @RequestMapping("updateState")
    public Object updateState(SystemUser systemUser){
        try{
            return systemUserServiceImpl.updateState(systemUser);
        }catch (Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

    /**
     * 子账户授权
     * @param userId
     * @param createrId
     * @return
     */
    @RequestMapping("subAuth")
    public Object subAuth(Integer userId,Integer createrId){
        try{
            return SimpleMsgVO.getOk(systemUserServiceImpl.subAuth(userId,createrId));
        }catch(Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

    /**
     * 编辑子账户权限
     * @param userId
     * @param roleId
     * @param menuIds
     * @return
     */
    @RequestMapping("editSubUserMenus")
    public Object editSubUserMenus(Integer userId,Integer roleId,String menuIds){
        try{
            return SimpleMsgVO.getOk(systemUserServiceImpl.editSubUserMenus(userId,roleId,menuIds));
        }catch (Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }


}
