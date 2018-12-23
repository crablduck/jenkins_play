package cn.wlh.controller.user;

import cn.wlh.model.SystemMenu;
import cn.wlh.service.SystemMenuService;
import cn.wlh.util.SessionUtil;
import cn.wlh.vo.SimpleMsgVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("menu")
public class SystemMenuController {

    @Resource
    SystemMenuService systemMenuServiceImpl;

    @Resource
    SessionUtil sessionUtil;

    /**
     * 获取所有菜单列表
     * @return
     */
    @RequestMapping("getMenuList")
    public Object getMenuList() {
        try{
            return SimpleMsgVO.getOk(systemMenuServiceImpl.selectList(null));
        }catch(Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

    /**
     * 获取所有目录列表
     */
    @RequestMapping("getContentList")
    public Object getContentList() {
        Wrapper<SystemMenu> wrapper = new EntityWrapper<>();
        wrapper.eq("state", 0);
        wrapper.eq("parent_id",0);
        wrapper.orderBy("order_num",true);
        return systemMenuServiceImpl.selectList(wrapper);
    }

    /**
     * 添加或编辑菜单
     */
    @RequestMapping("addOrUpdateMenu")
    public Object addOrUpdateMenu(SystemMenu systemMenu) {
        try{
            return SimpleMsgVO.getOk(systemMenuServiceImpl.addOrUpdateMenu(systemMenu));
        }catch (Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

    /**
     * 通过id获取对应菜单数据
     */
    @RequestMapping("getMenuById/{menuId}")
    public Object getMenuById(@PathVariable("menuId") Integer menuId) {
        return systemMenuServiceImpl.selectById(menuId);
    }

    /**
     * 通过id获取对应菜单数据
     */
    @RequestMapping("delMenuById/{menuId}")
    public Object delMenuById(@PathVariable("menuId") Integer menuId) {
        return systemMenuServiceImpl.deleteById(menuId);
    }

    /**
     * 获取当前用户的菜单数据
     * @return
     */
    @RequestMapping("getCurrMenus")
    public Object getCurrMenus() {
        try{
            return systemMenuServiceImpl.getCurrMenus();
        }catch(Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

}
