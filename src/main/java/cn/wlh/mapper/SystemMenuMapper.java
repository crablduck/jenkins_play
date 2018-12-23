package cn.wlh.mapper;

import cn.wlh.model.SystemMenu;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemMenuMapper extends BaseMapper<SystemMenu> {


    //得到用户对应的菜单数据
    @Select("SELECT sm.menu_id AS menuId,sm.parent_id AS parentId,sm.menu_name AS menuName,sm.menu_url AS menuUrl,sm.menu_type AS menuType,sm.menu_icon AS menuIcon,sm.order_num AS orderNum " +
            "FROM system_menu sm " +
            "LEFT JOIN system_user_menu sum ON sm.menu_id = sum.menu_id " +
            "WHERE sum.user_id = #{userId} AND sm.state = 0 " +
            "GROUP BY sm.menu_id " +
            "ORDER BY sm.parent_id,sm.order_num")
    List<SystemMenu> getMenusByUserId(@Param("userId") Integer userId);

    //得到角色对应的菜单数据
    @Select("SELECT sm.menu_id AS menuId,sm.parent_id AS parentId,sm.menu_name AS menuName,sm.menu_url AS menuUrl,sm.menu_type AS menuType,sm.menu_icon AS menuIcon,sm.order_num AS orderNum FROM system_menu sm " +
            "LEFT JOIN system_role_menu srm ON sm.menu_id = srm.menu_id " +
            "LEFT JOIN system_role sr ON sr.role_id = srm.role_id " +
            "WHERE sr.role_id = #{roleId} AND sm.state = 0")
    List<SystemMenu> authRoles(@Param("roleId") Integer roleId);

    @Insert("INSERT INTO system_menu(parent_id,menu_name,menu_url,menu_type,menu_icon,order_num,state,create_time) VALUES(#{parentId},#{menuName},#{menuUrl},#{menuType},#{menuIcon},#{orderNum},#{state},NOW())")
    @Options(useGeneratedKeys = true,keyProperty = "menuId",keyColumn = "menu_id")
    int insertAndSelectKey(SystemMenu systemMenu);
}
