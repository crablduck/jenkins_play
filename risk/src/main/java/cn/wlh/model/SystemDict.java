package cn.wlh.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 字典表
 */
@Data
@TableName("system_dict")
public class SystemDict implements Serializable {

    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 值
     */
    private String value;

    /**
     * 编码
     */
    private String code;

    /**
     * 状态（0：启用，1：禁用）
     */
    private Integer state;

    /**
     * 描述
     */
    private String descr;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Timestamp createTime;

}
