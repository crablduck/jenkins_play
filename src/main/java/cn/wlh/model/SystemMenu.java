package cn.wlh.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 菜单表
 */
@Data
@TableName("system_menu")
public class SystemMenu implements Serializable {

    /**
     * 菜单id
     */
    @TableId(value = "menu_id",type = IdType.AUTO)
    private Integer menuId;

    /**
     * 父级id
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 菜单名称
     */
    @TableField("menu_name")
    private String menuName;

    /**
     * 菜单URL
     */
    @TableField("menu_url")
    private String menuUrl;

    /**
     * 菜单类型（0：目录，1：菜单，2：按钮）
     */
    @TableField("menu_type")
    private Integer menuType;

    /**
     * 菜单图标
     */
    @TableField("menu_icon")
    private String menuIcon;

    /**
     * 排序
     */
    @TableField("order_num")
    private Integer orderNum;

    /**
     * 状态（0：启用，1：禁用）
     */
    private Integer state;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Timestamp createTime;



}
