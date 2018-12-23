package cn.wlh.service.impl;

import cn.wlh.mapper.SystemMenuMapper;
import cn.wlh.model.SystemMenu;
import cn.wlh.model.SystemRoleMenu;
import cn.wlh.model.SystemUser;
import cn.wlh.service.SystemMenuService;
import cn.wlh.service.SystemRoleMenuService;
import cn.wlh.util.SessionUtil;
import cn.wlh.vo.SimpleMsgVO;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SystemMenuServiceImpl extends ServiceImpl<SystemMenuMapper,SystemMenu> implements SystemMenuService {

    @Resource
    SystemMenuMapper systemMenuMapper;

    @Resource
    SessionUtil sessionUtil;

    @Resource
    SystemRoleMenuService systemRoleMenuServiceImpl;

    /**
     * 获取当前用户的菜单数据
     * @return
     */
    @Override
    public Object getCurrMenus() {
        SystemUser systemUser = sessionUtil.getUserInfo();
        JSONArray menuList = null;
        if(systemUser != null){
            //得到菜单数据
            List<SystemMenu> list = systemMenuMapper.getMenusByUserId(systemUser.getUserId());
            menuList = new JSONArray();
            for(SystemMenu systemMenu : list){
                if(systemMenu.getParentId() == 0){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name",systemMenu.getMenuName());
                    jsonObject.put("icon",systemMenu.getMenuIcon());
                    JSONArray childrenList = new JSONArray();
                    for(SystemMenu systemMenu1 : list){
                        if(systemMenu1.getParentId() == systemMenu.getMenuId()){
                            JSONObject json = new JSONObject();
                            json.put("name",systemMenu1.getMenuName());
                            json.put("url",systemMenu1.getMenuUrl());
                            childrenList.add(json);
                        }
                    }
                    jsonObject.put("children",childrenList);
                    menuList.add(jsonObject);
                }
            }
        }
        return SimpleMsgVO.getOk(menuList);
    }

    /**
     * 添加或编辑菜单
     * @param systemMenu
     * @return
     */
    @Override
    public Object addOrUpdateMenu(SystemMenu systemMenu) {
        if(systemMenu.getMenuType() == 0 && systemMenu.getParentId() == null){//添加目录
            systemMenu.setParentId(0);
            systemMenuMapper.insertAndSelectKey(systemMenu);
            SystemRoleMenu systemRoleMenu = new SystemRoleMenu();
            systemRoleMenu.setRoleId(1);
            systemRoleMenu.setMenuId(systemMenu.getMenuId());
            return systemRoleMenuServiceImpl.insert(systemRoleMenu);
        }else if(systemMenu.getMenuType() == 1 && systemMenu.getParentId() == null){//添加菜单
            systemMenuMapper.insertAndSelectKey(systemMenu);
            SystemRoleMenu systemRoleMenu = new SystemRoleMenu();
            systemRoleMenu.setRoleId(1);
            systemRoleMenu.setMenuId(systemMenu.getMenuId());
            return systemRoleMenuServiceImpl.insert(systemRoleMenu);
        }else{//编辑菜单
            return this.updateById(systemMenu);
        }
    }
}
