package cn.wlh.service;

import cn.wlh.model.SystemRoleMenu;
import com.baomidou.mybatisplus.service.IService;

public interface SystemRoleMenuService extends IService<SystemRoleMenu> {

    /**
     * 角色授权
     * @param roleId
     * @param menuIds
     * @return
     */
    Object editRoleMenus(Integer roleId, String menuIds);

}
