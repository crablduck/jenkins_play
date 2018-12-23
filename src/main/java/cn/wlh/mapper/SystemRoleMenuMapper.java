package cn.wlh.mapper;

import cn.wlh.model.SystemRoleMenu;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemRoleMenuMapper extends BaseMapper<SystemRoleMenu> {

//    //获取角色对应的菜单id
//    @Select("SELECT menu_id FROM system_role_menu WHERE role_id = #{}")
//    List<String> getmenuIdsByRoleId(Integer roleId);
}
