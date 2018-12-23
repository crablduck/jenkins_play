package cn.wlh.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 角色表
 */
@Data
@TableName("system_role")
public class SystemRole implements Serializable {

    /**
     * 角色id
     */
    @TableId(value = "role_id",type = IdType.AUTO)
    private Integer roleId;

    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;

    /**
     * 角色级别
     */
    @TableField("role_level")
    private Integer roleLevel;

    /**
     * 描述
     */
    private String descr;

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
