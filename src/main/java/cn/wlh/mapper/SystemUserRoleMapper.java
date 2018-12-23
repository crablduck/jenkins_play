package cn.wlh.mapper;

import cn.wlh.model.SystemUserRole;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemUserRoleMapper extends BaseMapper<SystemUserRole> {

    //通过角色id获取所有用户id
    @Select("SELECT user_id FROM system_user_role WHERE role_id = #{roleId}")
    List<Integer> selectUserIdsByRoleId(Integer roleId);
}
