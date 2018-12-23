package cn.wlh.service;

import cn.wlh.model.SystemUserRole;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

public interface SystemUserRoleService extends IService<SystemUserRole> {

    /**
     * 通过角色id获取所有用户id
     * @param roleId
     * @return
     */
    List<Integer> selectUserIdsByRoleId(Integer roleId);
}
