package cn.wlh.service.impl;

import cn.wlh.mapper.SystemMenuMapper;
import cn.wlh.mapper.SystemRoleMapper;
import cn.wlh.model.SystemMenu;
import cn.wlh.model.SystemRole;
import cn.wlh.model.SystemUser;
import cn.wlh.model.SystemUserRole;
import cn.wlh.service.SystemRoleMenuService;
import cn.wlh.service.SystemRoleService;
import cn.wlh.service.SystemUserRoleService;
import cn.wlh.util.SessionUtil;
import cn.wlh.vo.SimpleMsgVO;
import cn.wlh.vo.TableMsgVO;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SystemRoleServiceImpl extends ServiceImpl<SystemRoleMapper,SystemRole> implements SystemRoleService {

    @Resource
    SystemRoleMapper systemRoleMapper;

    @Resource
    SessionUtil sessionUtil;

    @Resource
    SystemMenuMapper systemMenuMapper;

    @Resource
    SystemRoleMenuService systemRoleMenuServiceImpl;

    @Resource
    SystemUserRoleService systemUserRoleServiceImpl;

    /**
     * 获取当前用户的角色
     */
    @Override
    public Object getCurrRole() {
        SystemUser systemUser = sessionUtil.getUserInfo();
        SystemUserRole systemUserRole = null;
        if(null != systemUser){
            Wrapper<SystemUserRole> wrapper = new EntityWrapper<>();
            wrapper.eq("user_id",systemUser.getUserId());
            systemUserRole = systemUserRoleServiceImpl.selectOne(wrapper);
        }
        return SimpleMsgVO.getOk(systemUserRole);
    }

    /**
     *  获取所有角色数据
     */
    @Override
    public Object getRoleList(Page<SystemRole> page, SystemRole systemRole) {
        Integer roleId = systemRole.getRoleId();
        Integer state = systemRole.getState();
        Wrapper<SystemRole> wrapper = new EntityWrapper<>();
        if(null != roleId){
            wrapper.eq("role_id",roleId);
        }
        if(null != state){
            wrapper.eq("state",state);
        }
        page = this.selectPage(page,wrapper);
        return TableMsgVO.getOk(page.getTotal(),page.getRecords());
    }

    /**
     * 编辑角色状态
     * @param roleId
     * @param state
     * @return
     */
    @Override
    public Object updateState(Integer roleId, String state) {
        return SimpleMsgVO.getOk(systemRoleMapper.updateState(roleId,state));
    }

    /**
     * 获取授权信息
     * @param systemRole
     * @return
     */
    @Override
    public Object authRoles(SystemRole systemRole) {
        SystemUser systemUser = sessionUtil.getUserInfo();
        JSONArray menuList = null;
        if(null != systemUser){
            //获取当前用户的菜单数据
            List<SystemMenu> list = systemMenuMapper.getMenusByUserId(systemUser.getUserId());
            //获取角色的菜单数据
            List<SystemMenu> authRoles = systemMenuMapper.authRoles(systemRole.getRoleId());
            menuList = new JSONArray();
            for(SystemMenu systemMenu : list){
                if(systemMenu.getParentId() == 0){
                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("title",systemMenu.getMenuName());
                    jsonObject.put("title",systemMenu.getMenuIcon());
                    jsonObject.put("value",systemMenu.getMenuId());
                    JSONArray childrenList = new JSONArray();
                    for(SystemMenu systemMenu1 : list){
                        if(systemMenu1.getParentId() == systemMenu.getMenuId()){
                            JSONObject json = new JSONObject();
//                            json.put("title",systemMenu1.getMenuName());
                            json.put("title",systemMenu1.getMenuUrl().substring(7,systemMenu1.getMenuUrl().length() - 5));
                            json.put("value",systemMenu1.getMenuId());
                            for(SystemMenu systemMenu2 : authRoles){
                                if(systemMenu1.getMenuId() == systemMenu2.getMenuId()){
                                    json.put("checked",true);
                                }
                            }
                            json.put("data",new JSONArray());
                            childrenList.add(json);
                        }
                    }
                    jsonObject.put("data",childrenList);
                    menuList.add(jsonObject);
                }
            }
        }
        return menuList;
    }

    /**
     * 根据id删除角色数据
     * @param roleId
     * @return
     */
    @Override
    public Object delRole(Integer roleId) {
        //判断有用户是否使用该角色
        Wrapper<SystemUserRole> wrapper = new EntityWrapper<>();
        wrapper.eq("role_id",roleId);
        List<SystemUserRole> list = systemUserRoleServiceImpl.selectList(wrapper);
        if(list != null && list.size() > 0){
            return SimpleMsgVO.get403Fail();
        }
        boolean isDel = this.deleteById(roleId);
        return SimpleMsgVO.getOk(isDel);
    }

    /**
     * 根据用户级别获取对于的角色
     * @param roleLevel
     * @return
     */
    @Override
    public Object getRolesByLevel(Integer roleLevel) {
        Wrapper<SystemRole> wrapper = new EntityWrapper<>();
        wrapper.eq("role_level",roleLevel);
        return this.selectList(wrapper);
    }

}
