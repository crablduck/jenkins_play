package cn.wlh.service;

import cn.wlh.model.SystemRole;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

public interface SystemRoleService extends IService<SystemRole> {

    /**
     * 获取当前用户的角色
     */
    Object getCurrRole();

    /**
     *  获取所有角色数据
     */
    Object getRoleList(Page<SystemRole> page, SystemRole systemRole);

    /**
     *  编辑角色状态
     * @param roleId
     * @param state
     * @return
     */
    Object updateState(Integer roleId, String state);

    /**
     * 获取授权信息
     * @param systemRole
     * @return
     */
    Object authRoles(SystemRole systemRole);

    /**
     * 根据id删除角色数据
     * @param roleId
     * @return
     */
    Object delRole(Integer roleId);

    /**
     * 根据用户级别获取对于的角色
     * @param roleLevel
     * @return
     */
    Object getRolesByLevel(Integer roleLevel);
}
