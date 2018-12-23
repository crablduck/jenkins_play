package cn.wlh.service.impl;

import cn.wlh.mapper.*;
import cn.wlh.model.*;
import cn.wlh.service.*;
import cn.wlh.util.Md5Util;
import cn.wlh.util.SessionUtil;
import cn.wlh.vo.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper,SystemUser> implements SystemUserService {

    @Resource
    SessionUtil sessionUtil;

    @Resource
    SystemRoleMapper systemRoleMapper;

    @Resource
    SystemUserRoleMapper systemUserRoleMapper;

    @Resource
    SystemCompanyService systemCompanyService;

    @Resource
    SystemCompanyMapper systemCompanyMapper;

    @Resource
    SystemUserService systemUserService;

    @Resource
    SystemUserMapper systemUserMapper;

    @Resource
    SystemRoleMenuMapper systemRoleMenuMapper;

    @Resource
    SystemUserMenuService systemUserMenuServiceImpl;

    @Resource
    SystemMenuMapper systemMenuMapper;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private IBillService billService;

    /**
     * 检测该用户信息
     * @param systemUser
     * @return
     */
    @Override
    public Object checkUser(SystemUser systemUser) {
        Wrapper<SystemUser> wrapper = new EntityWrapper<>();
        wrapper.eq("user_account",systemUser.getUserAccount());
//        wrapper.eq("password", Md5Util.md5(systemUser.getPassword()));
        SystemUser user = this.selectOne(wrapper);
        if(null != user){
            if(!systemUser.getUserAccount().equalsIgnoreCase(user.getUserAccount())){
                return SimpleMsgVO.get402Fail();
            }
            if(!Md5Util.md5(systemUser.getPassword()).equalsIgnoreCase(user.getPassword())){
                return SimpleMsgVO.get400Fail();
            }
            if(user.getState() == 0){
                sessionUtil.putUserInfo(user);
                return SimpleMsgVO.getOk();
            }
            return SimpleMsgVO.get800Fail();
        }else{
            return SimpleMsgVO.get402Fail();
        }
    }

    /**
     * 获取用户相关信息
     * @return
     */
    @Override
    public Object selectUserInfoByUserId() {
        UserInfoVO userInfoVO = new UserInfoVO();
        SystemUser systemUser = sessionUtil.getUserInfo();
        if(null != systemUser){
            BeanUtils.copyProperties(systemUser,userInfoVO);
            userInfoVO.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(systemUser.getCreateTime()));
            SystemUser user = this.selectById(systemUser.getCreaterId());
            if(null != user){
                userInfoVO.setCreaterAccount(user.getUserAccount());
                userInfoVO.setCreaterName(user.getUsername());
            }
            List<String> roleNames = systemRoleMapper.getRolesByUserId(systemUser.getUserId());
            String names = "";
            for (String str : roleNames){
                names += str;
            }
            if(names.lastIndexOf(",") == names.length()){
                names = names.substring(0,names.lastIndexOf(",") - 1);
            }
            userInfoVO.setRoleNames(names);
        }
        return SimpleMsgVO.getOk(userInfoVO);
    }

    /**
     * 添加或编辑公司级用户
     * @return
     */
    @Override
    public Object addOrUpdateUser(UserInfoReq userInfoReq) {
        //判断是添加还是编辑
        //添加  添加公司信息  添加用户  添加用户角色关联表  添加用户菜单
        SystemUser sessionUtilUserInfo = sessionUtil.getUserInfo();
        if(null != sessionUtilUserInfo){
            if(null == userInfoReq.getUserId()){//添加

                String companyName = userInfoReq.getCompanyName();
                String companySecret = UUID.randomUUID().toString();
                String appNumber = "App_" + companyName;

                //添加公司信息
                SystemCompany systemCompany = new SystemCompany();
                systemCompany.setCompanyName(companyName);
                systemCompany.setCompanySecret(companySecret);
                systemCompanyMapper.insertAndSelectKey(systemCompany);

                //添加公司的基础 应用
                Commodity commodity = new Commodity();
                commodity.setCommodityNumber(appNumber);
                commodity.setCommodityName(companyName);
                commodity.setCommoditySecret(companySecret);
                commodity.setCompanyId(systemCompany.getCompanyId());
                commodityService.insertAndSelectKey(commodity);

                systemCompany.setBaseAppId(commodity.getId());
                boolean b = systemCompanyService.addBaseAppId(systemCompany);

                //添加一个账单
                Bill bill = new Bill();
                bill.setCompanyName(companyName);
                bill.setAccount(userInfoReq.getUserAccount());
                bill.setPhoneNumber(userInfoReq.getUserPhone());
                bill.setCompany_id(systemCompany.getCompanyId());
                bill.setBalance(0f);

                java.util.Date date = new java.util.Date();
                Timestamp timeStamp = new Timestamp(date.getTime());
                bill.setAddTime(timeStamp);
                billService.insert(bill);

                //添加用户
                SystemUser systemUser = new SystemUser();
                systemUser.setUserAccount(userInfoReq.getUserAccount());
                systemUser.setUsername(userInfoReq.getCompanyName());
                systemUser.setPassword(Md5Util.md5(userInfoReq.getPassword()));
                systemUser.setUserPhone(userInfoReq.getUserPhone());
                systemUser.setCreaterId(sessionUtilUserInfo.getUserId());
                systemUser.setCompanyId(systemCompany.getCompanyId());
                systemUser.setState(userInfoReq.getState());
                systemUser.setDescr(userInfoReq.getDescr());
                systemUser.setCommodityId(commodity.getId());
                systemUserMapper.insertAndSelectKey(systemUser);
                //添加用户角色关联表
                Integer roleLevel = systemRoleMapper.selectById(userInfoReq.getRoleId()).getRoleLevel();
                SystemUserRole systemUserRole = new SystemUserRole();
                systemUserRole.setRoleId(userInfoReq.getRoleId());
                systemUserRole.setUserId(systemUser.getUserId());
                systemUserRole.setRoleLevel(roleLevel);
                systemUserRoleMapper.insert(systemUserRole);
                //更新当前用户子账户个数
                systemUserMapper.updateSubSumByUserId(sessionUtilUserInfo.getUserId());
                //获取角色对应的菜单id
                Wrapper<SystemRoleMenu> systemRoleMenuWrapper = new EntityWrapper<>();
                systemRoleMenuWrapper.eq("role_id",userInfoReq.getRoleId());
                List<SystemRoleMenu> systemRoleMenus = systemRoleMenuMapper.selectList(systemRoleMenuWrapper);
                //添加用户菜单数据
                List<SystemUserMenu> systemUserMenus = new ArrayList<>();
                for(SystemRoleMenu systemRoleMenu : systemRoleMenus){
                    SystemUserMenu systemUserMenu = new SystemUserMenu();
                    systemUserMenu.setUserId(systemUser.getUserId());
                    systemUserMenu.setMenuId(systemRoleMenu.getMenuId());
                    systemUserMenu.setRoleId(userInfoReq.getRoleId());
                    systemUserMenu.setState(systemRoleMenu.getState());
                    systemUserMenus.add(systemUserMenu);
                }
                if(systemUserMenus != null && systemUserMenus.size() > 0){
                    systemUserMenuServiceImpl.insertBatch(systemUserMenus);
                }
                return  SimpleMsgVO.getOk();
            }else{//编辑 编辑公司信息  编辑用户 编辑用户菜单
                //编辑公司信息
                SystemCompany systemCompany = new SystemCompany();
                systemCompany.setCompanyName(userInfoReq.getCompanyName());
                systemCompany.setCompanyId(userInfoReq.getCompanyId());
                systemCompanyService.updateById(systemCompany);
                //编辑用户
                SystemUser systemUser = new SystemUser();
                systemUser.setUsername(userInfoReq.getCompanyName());
                systemUser.setUserPhone(userInfoReq.getUserPhone());
                systemUser.setUserId(userInfoReq.getUserId());
                systemUser.setState(userInfoReq.getState());
                systemUser.setDescr(userInfoReq.getDescr());
                systemUserService.updateById(systemUser);
                //编辑用户菜单（1、先删除，再添加）
//                Wrapper<SystemUserMenu> wrapper = new EntityWrapper<>();
//                wrapper.eq("user_id",userInfoReq.getUserId());
//                systemUserMenuServiceImpl.delete(wrapper);
//                //获取角色对应的菜单id
//                Wrapper<SystemRoleMenu> systemRoleMenuWrapper = new EntityWrapper<>();
//                systemRoleMenuWrapper.eq("role_id",userInfoReq.getRoleId());
//                List<SystemRoleMenu> systemRoleMenus = systemRoleMenuMapper.selectList(systemRoleMenuWrapper);
//                //添加用户菜单数据
//                List<SystemUserMenu> systemUserMenus = new ArrayList<>();
//                for(SystemRoleMenu systemRoleMenu : systemRoleMenus){
//                    SystemUserMenu systemUserMenu = new SystemUserMenu();
//                    systemUserMenu.setUserId(userInfoReq.getUserId());
//                    systemUserMenu.setMenuId(systemRoleMenu.getMenuId());
//                    systemUserMenu.setState(systemRoleMenu.getState());
//                    systemUserMenus.add(systemUserMenu);
//                }
//                systemUserMenuServiceImpl.insertBatch(systemUserMenus);
            }
        }
        return SimpleMsgVO.getOk();
    }

    /**
     * 添加或编辑子账户
     * @return
     */
    @Override
    public Object addOrUpdateSubUser(UserInfoReq userInfoReq) {
        //判断是添加还是编辑
        //添加    添加用户    添加用户角色关联（角色默认为3）    添加用户菜单关联
        if(null == userInfoReq.getUserId()){//添加
            //添加用户
            userInfoReq.setPassword(Md5Util.md5(userInfoReq.getPassword()));
            SystemUser systemUser = new SystemUser();
            BeanUtils.copyProperties(userInfoReq,systemUser);
            systemUserMapper.insertAndSelectKey(systemUser);

            //添加用户角色关联表
            Integer roleLevel = systemRoleMapper.selectById(userInfoReq.getRoleId()).getRoleLevel();
            SystemUserRole systemUserRole = new SystemUserRole();
            systemUserRole.setRoleId(userInfoReq.getRoleId());
            systemUserRole.setRoleLevel(roleLevel);
            systemUserRole.setUserId(systemUser.getUserId());
            systemUserRoleMapper.insert(systemUserRole);
            //更新父用户的子账户个数
            systemUserMapper.updateSubSumByUserId(userInfoReq.getCreaterId());
            //获取角色对应的菜单id
            Wrapper<SystemRoleMenu> systemRoleMenuWrapper = new EntityWrapper<>();
            systemRoleMenuWrapper.eq("role_id",userInfoReq.getRoleId());
            List<SystemRoleMenu> systemRoleMenus = systemRoleMenuMapper.selectList(systemRoleMenuWrapper);
            //添加用户菜单数据
            List<SystemUserMenu> systemUserMenus = new ArrayList<>();
            for(SystemRoleMenu systemRoleMenu : systemRoleMenus){
                SystemUserMenu systemUserMenu = new SystemUserMenu();
                systemUserMenu.setUserId(systemUser.getUserId());
                systemUserMenu.setMenuId(systemRoleMenu.getMenuId());
                systemUserMenu.setRoleId(userInfoReq.getRoleId());
                systemUserMenus.add(systemUserMenu);
            }
            if(systemUserMenus != null && systemUserMenus.size() > 0){
                return systemUserMenuServiceImpl.insertBatch(systemUserMenus);
            }
        }else{//编辑  编辑用户
            //编辑用户
            SystemUser systemUser = new SystemUser();
            BeanUtils.copyProperties(userInfoReq,systemUser);
            return systemUserService.updateById(systemUser);
        }
        return null;
    }

    /**
     * 获取公司级用户下所有子账户
     * @return
     */
    @Override
    public Object getSubUserList(Pagination page, UserInfoReq userInfoReq) {
        List<UserInfoRes> userInfoRes = systemUserMapper.selectSubUserPages(page,userInfoReq);
        return TableMsgVO.getOk(page.getTotal(),userInfoRes);
    }

    /**
     * 子账户授权
     * @param userId
     * @param createrId
     * @return
     */
    @Override
    public Object subAuth(Integer userId, Integer createrId) {
        //获取父用户的权限    获取子账户的权限
        List<SystemMenu> parentMenus = systemMenuMapper.getMenusByUserId(createrId);
        List<SystemMenu> childMenus = systemMenuMapper.getMenusByUserId(userId);
        JSONArray menuList = new JSONArray();
        for(SystemMenu systemMenu : parentMenus){
            if(systemMenu.getParentId() == 0){
                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("title",systemMenu.getMenuName());
                jsonObject.put("title",systemMenu.getMenuIcon());
                jsonObject.put("value",systemMenu.getMenuId());
                if(systemMenu.getMenuId() == 2 || systemMenu.getMenuName().equalsIgnoreCase("管理中心")){
                    jsonObject.put("disabled",true);
                }
                JSONArray childrenList = new JSONArray();
                for(SystemMenu systemMenu1 : parentMenus){
                    if(systemMenu1.getParentId() == systemMenu.getMenuId()){
                        JSONObject json = new JSONObject();
//                        json.put("title",systemMenu1.getMenuName());
                        json.put("title",systemMenu1.getMenuUrl().substring(7,systemMenu1.getMenuUrl().length() - 5));
                        json.put("value",systemMenu1.getMenuId());
                        if(systemMenu.getMenuId() == 2 || systemMenu.getMenuName().equalsIgnoreCase("管理中心")){
                            json.put("disabled",true);
                        }
                        for(SystemMenu systemMenu2 : childMenus){
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
        return menuList;
    }

    /**
     * 编辑子账户权限
     * @param userId
     * @param roleId
     * @param menuIds
     * @return
     */
    @Override
    public Object editSubUserMenus(Integer userId, Integer roleId, String menuIds) {
        //1、删除原有的用户权限   2、添加新的权限
        Wrapper<SystemUserMenu> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        boolean isDel = systemUserMenuServiceImpl.delete(wrapper);
        boolean isEdit = false;
        String[] ids = menuIds.split(",");
        if(isDel){
            List<SystemUserMenu> list = new ArrayList<>();
            for(String id : ids){
                SystemUserMenu systemUserMenu = new SystemUserMenu();
                systemUserMenu.setRoleId(roleId);
                systemUserMenu.setUserId(userId);
                systemUserMenu.setMenuId(Integer.parseInt(id));
                list.add(systemUserMenu);
            }
            isEdit = systemUserMenuServiceImpl.insertBatch(list);
        }
        return isEdit;
    }

    /**
     * 修改密码
     * @param userId
     * @param password
     * @return
     */
    @Override
    public Object editPwd(Integer userId, String password) {
        SystemUser systemUser = new SystemUser();
        systemUser.setUserId(userId);
        systemUser.setPassword(Md5Util.md5(password));
        return this.updateById(systemUser);
    }

    @Override
    public SystemUser getUserByUserId(Integer userId) {
        return this.selectById(userId);
    }

    /**
     * 检查用户账号是否存在
     * @param userAccount
     * @return
     */
    @Override
    public Object checkUserAccount(String userAccount) {
        Wrapper<SystemUser> wrapper = new EntityWrapper<>();
        wrapper.eq("user_account",userAccount);
        SystemUser systemUser = this.selectOne(wrapper);
        return SimpleMsgVO.getOk(systemUser);
    }

    /**
     * 获取用户公司级用户
     * @param userInfoReq
     * @return
     */
    @Override
    public Object getUserList(Pagination page, UserInfoReq userInfoReq) {
        SystemUser systemUser = sessionUtil.getUserInfo();
        List<UserInfoRes> userInfoRes = null;
        if(null != systemUser){
            userInfoReq.setUserId(systemUser.getUserId());
            if(systemUser.getCreaterId() != 0){//表示当前用户为公司级
                userInfoReq.setCreaterId(systemUser.getCreaterId());
                userInfoRes = systemUserMapper.selectCompanyUserPages(page,userInfoReq);
            }else{
                userInfoRes = systemUserMapper.selectUserPages(page,userInfoReq);
            }
        }
        return TableMsgVO.getOk(page.getTotal(),userInfoRes);
    }

    /**
     * 重置密码
     * @param userId
     * @return
     */
    @Override
    public Object resetPwd(Integer userId) {
        SystemUser systemUser = new SystemUser();
        systemUser.setUserId(userId);
        systemUser.setPassword(Md5Util.md5("123456"));
        this.updateById(systemUser);
        return SimpleMsgVO.getOk();
    }

    /**
     * 编辑用户状态
     * @param systemUser
     * @return
     */
    @Override
    public Object updateState(SystemUser systemUser) {
        return SimpleMsgVO.getOk(this.updateById(systemUser));
    }


}
