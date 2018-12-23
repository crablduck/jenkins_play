package cn.wlh.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色菜单表
 */
@Data
@TableName("system_role_menu")
public class SystemRoleMenu implements Serializable {

    /**
     * id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 角色id
     */
    @TableField("role_id")
    private Integer roleId;

    /**
     * 菜单id
     */
    @TableField("menu_id")
    private Integer menuId;

    /**
     * 状态（0：启用，1：禁用）
     */
    private Integer state;

}
