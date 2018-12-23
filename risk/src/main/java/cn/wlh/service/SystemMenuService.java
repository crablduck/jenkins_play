package cn.wlh.service;

import cn.wlh.model.SystemMenu;
import com.baomidou.mybatisplus.service.IService;

public interface SystemMenuService extends IService<SystemMenu> {

    /**
     * 获取当前用户的菜单数据
     * @return
     */
    Object getCurrMenus();

    /**
     * 添加或编辑菜单
     * @param systemMenu
     * @return
     */
    Object addOrUpdateMenu(SystemMenu systemMenu);
}
