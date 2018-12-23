package cn.wlh.mapper;

import cn.wlh.model.SystemRole;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemRoleMapper extends BaseMapper<SystemRole> {

    //编辑角色状态
    @Update("UPDATE system_role SET state = #{state} WHERE role_id = #{roleId}")
    boolean updateState(@Param("roleId") Integer roleId, @Param("state") String state);

    @Select("SELECT sr.role_name FROM system_role sr " +
            "LEFT JOIN system_user_role sur ON sr.role_id = sur.role_id " +
            "LEFT JOIN system_user su ON sur.user_id = su.user_id " +
            "WHERE su.user_id = #{userId}")
    List<String> getRolesByUserId(Integer userId);
}
