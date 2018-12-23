package cn.wlh.controller.user;

import cn.wlh.model.SystemRole;
import cn.wlh.service.SystemRoleMenuService;
import cn.wlh.service.SystemRoleService;
import cn.wlh.util.SessionUtil;
import cn.wlh.vo.SimpleMsgVO;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("role")
public class SystemRoleController {

    @Resource
    SystemRoleService systemRoleServiceImpl;

    @Resource
    SessionUtil sessionUtil;

    @Resource
    SystemRoleMenuService systemRoleMenuServiceImpl;

    /**
     * 获取当前用户的角色
     */
    @RequestMapping("getCurrRole")
    public Object getCurrRole(){
        try{
            return systemRoleServiceImpl.getCurrRole();
        }catch (Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

    /**
     *  获取所有角色数据
     */
    @RequestMapping("getRoleList")
    public Object getRoleList(Page<SystemRole> page, SystemRole systemRole){
        try{
            return systemRoleServiceImpl.getRoleList(page,systemRole);
        }catch(Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

    /**
     * 添加或编辑角色
     * @param systemRole
     * @return
     */
    @RequestMapping("addOrUpdateRole")
    public Object addOrUpdateRole(SystemRole systemRole){
        try{
            if(null == systemRole.getRoleId()){//添加角色
                systemRole.setState(0);
                systemRoleServiceImpl.insert(systemRole);
            }else{//编辑角色
                systemRoleServiceImpl.updateById(systemRole);
            }
        }catch (Exception e){
            SimpleMsgVO.get500Fail();
        }
        return SimpleMsgVO.getOk();
    }

    /**
     * 编辑角色状态
     * @param roleId
     * @param state
     * @return
     */
    @RequestMapping("updateState/{roleId}")
    public Object updateState(@PathVariable("roleId") Integer roleId, String state){
        try{
            return systemRoleServiceImpl.updateState(roleId,state);
        }catch(Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

    /**
     * 根据id删除角色数据
     * @param roleId
     * @return
     */
    @RequestMapping("delRole/{roleId}")
    public Object delRole(@PathVariable("roleId") Integer roleId){
        try{
            return systemRoleServiceImpl.delRole(roleId);
        }catch(Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }


    /**
     * 获取授权信息
     * @param systemRole
     * @return
     */
    @RequestMapping("authRoles")
    public Object authRoles(SystemRole systemRole){
        try{
            return SimpleMsgVO.getOk(systemRoleServiceImpl.authRoles(systemRole));
        }catch (Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

    /**
     * 角色授权(提交)
     * @param roleId
     * @param menuIds
     * @return
     */
    @RequestMapping("editRoleMenus")
    public Object editRoleMenus(Integer roleId,String menuIds){
        try{
            return systemRoleMenuServiceImpl.editRoleMenus(roleId,menuIds);
        }catch (Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

    /**
     * 根据用户级别获取对于的角色
     * @param roleLevel
     * @return
     */
    @RequestMapping("getRolesByLevel")
    public Object getRolesByLevel(Integer roleLevel){
        try{
            return SimpleMsgVO.getOk(systemRoleServiceImpl.getRolesByLevel(roleLevel));
        }catch (Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }



}
