package cn.wlh.service.impl;

import cn.wlh.mapper.SystemRoleMenuMapper;
import cn.wlh.model.SystemRoleMenu;
import cn.wlh.model.SystemUser;
import cn.wlh.model.SystemUserMenu;
import cn.wlh.service.SystemRoleMenuService;
import cn.wlh.service.SystemUserMenuService;
import cn.wlh.service.SystemUserRoleService;
import cn.wlh.service.SystemUserService;
import cn.wlh.vo.SimpleMsgVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SystemRoleMenuServiceImpl extends ServiceImpl<SystemRoleMenuMapper,SystemRoleMenu> implements SystemRoleMenuService {

    @Resource
    SystemUserRoleService systemUserRoleServiceImpl;

    @Resource
    SystemUserMenuService systemUserMenuServiceImpl;

    @Resource
    SystemUserService systemUserServiceImpl;


    /**
     * 角色授权
     * @param roleId
     * @param menuIds
     * @return
     */
    @Override
    public Object editRoleMenus(Integer roleId, String menuIds) {
        //删除角色菜单关联表的数据
        Wrapper<SystemRoleMenu> systemRoleMenuWrapper = new EntityWrapper<>();
        systemRoleMenuWrapper.eq("role_id",roleId);
        this.delete(systemRoleMenuWrapper);
        List<String> ids = Arrays.asList(menuIds.split(","));
        List<SystemRoleMenu> roleMenus = new ArrayList<>();
        for(String str : ids){
            SystemRoleMenu systemRoleMenu = new SystemRoleMenu();
            systemRoleMenu.setMenuId(Integer.parseInt(str));
            systemRoleMenu.setRoleId(roleId);
            roleMenus.add(systemRoleMenu);
        }
        //添加新的授权信息
        if(roleMenus != null && roleMenus.size() > 0){
            this.insertBatch(roleMenus);
        }

        if(roleId == 2){//公司级
            //得到拥有该角色的用户id
            List<Integer> userIds = systemUserRoleServiceImpl.selectUserIdsByRoleId(roleId);

            //删除用户id对应的用户菜单关联表的数据
            List<SystemUserMenu> systemUserMenus = new ArrayList<>();
            List<Integer> childUserIds = new ArrayList<>();
            for (Integer userId : userIds){
                Wrapper<SystemUserMenu> systemUserMenuWrapper = new EntityWrapper<>();
                systemUserMenuWrapper.eq("user_id",userId);
                systemUserMenuServiceImpl.delete(systemUserMenuWrapper);
                Wrapper<SystemUser> systemUserWrapper = new EntityWrapper<>();
                systemUserWrapper.eq("creater_id",userId);
                List<SystemUser> systemUsers = systemUserServiceImpl.selectList(systemUserWrapper);
                for(SystemUser systemUser : systemUsers){
                    childUserIds.add(systemUser.getUserId());
                }
                for(String id : ids){
                    SystemUserMenu systemUserMenu = new SystemUserMenu();
                    systemUserMenu.setUserId(userId);
                    systemUserMenu.setMenuId(Integer.parseInt(id));
                    systemUserMenu.setState(0);
                    systemUserMenus.add(systemUserMenu);
                }
            }
            //插入用户id对应的新菜单
            if(null != systemUserMenus && systemUserMenus.size() > 0){
                systemUserMenuServiceImpl.insertBatch(systemUserMenus);
            }

            //查询子账户所拥有的菜单是否在父级菜单里，，没有则删除
            List<Integer> userMenuIds = new ArrayList<>();
            for(Integer cUserId : childUserIds){
                Wrapper<SystemUserMenu> wrapper = new EntityWrapper<>();
                wrapper.eq("user_id",cUserId);
                List<SystemUserMenu> userMenus = systemUserMenuServiceImpl.selectList(wrapper);
                System.out.println(ids);
                for(SystemUserMenu systemUserMenu : userMenus){
                    System.out.println(systemUserMenu.getMenuId() + "------------------" + ids.contains(String.valueOf(systemUserMenu.getMenuId())));
                    if(!ids.contains(String.valueOf(systemUserMenu.getMenuId()))){
                        userMenuIds.add(systemUserMenu.getId());
                    }
                }
            }
            //批量删除
            if(null != userMenuIds && userMenuIds.size() > 0){
                systemUserMenuServiceImpl.deleteBatchIds(userMenuIds);
            }
        }
        return SimpleMsgVO.getOk();
    }
}
