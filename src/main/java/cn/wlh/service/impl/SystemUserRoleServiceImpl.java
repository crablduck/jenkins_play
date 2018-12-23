package cn.wlh.service.impl;

import cn.wlh.mapper.SystemUserRoleMapper;
import cn.wlh.model.SystemUserRole;
import cn.wlh.service.SystemUserRoleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SystemUserRoleServiceImpl extends ServiceImpl<SystemUserRoleMapper,SystemUserRole> implements SystemUserRoleService {

    @Resource
    SystemUserRoleMapper systemUserRoleMapper;
    /**
     * 通过角色id获取所有用户id
     * @param roleId
     * @return
     */
    @Override
    public List<Integer> selectUserIdsByRoleId(Integer roleId) {
        return systemUserRoleMapper.selectUserIdsByRoleId(roleId);
    }



}
