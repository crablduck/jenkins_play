package cn.wlh.service;

import cn.wlh.model.SystemUser;
import cn.wlh.vo.UserInfoReq;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.baomidou.mybatisplus.service.IService;

public interface SystemUserService extends IService<SystemUser> {

    /**
     * 检测该用户信息
     * @param systemUser
     * @return
     */
    Object checkUser(SystemUser systemUser);

    /**
     * 根据用户id获取相关信息
     */
    Object selectUserInfoByUserId();

    /**
     * 添加或编辑公司级用户
     * @return
     */
    Object addOrUpdateUser(UserInfoReq userInfoReq);

    /**
     * 检查用户账号是否存在
     * @param userAccount
     * @return
     */
    Object checkUserAccount(String userAccount);

    /**
     * 获取用户公司级用户
     * @param userInfoReq
     * @return
     */
    Object getUserList(Pagination page, UserInfoReq userInfoReq);

    /**
     * 重置密码
     * @param userId
     * @return
     */
    Object resetPwd(Integer userId);

    /**
     * 编辑用户状态
     * @param systemUser
     * @return
     */
    Object updateState(SystemUser systemUser);

    /**
     * 添加或编辑子账户
     * @return
     */
    Object addOrUpdateSubUser(UserInfoReq userInfoReq);

    /**
     * 获取公司级用户下所有子账户
     * @return
     */
    Object getSubUserList(Pagination page, UserInfoReq userInfoReq);

    /**
     * 子账户授权
     * @param userId
     * @param createrId
     * @return
     */
    Object subAuth(Integer userId, Integer createrId);

    /**
     * 编辑子账户权限
     * @param userId
     * @param roleId
     * @param menuIds
     * @return
     */
    Object editSubUserMenus(Integer userId, Integer roleId, String menuIds);

    /**
     * 修改密码
     * @param userId
     * @param password
     * @return
     */
    Object editPwd(Integer userId, String password);

    SystemUser getUserByUserId(Integer userId);
}
