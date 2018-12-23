package cn.wlh.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 公司表
 */
@Data
@TableName("system_company")
public class SystemCompany implements Serializable {

    /**
     * 公司id
     */
    @TableId(value = "company_id",type = IdType.AUTO)
    private Integer companyId;

    /**
     * 公司名称
     */
    @TableField("company_name")
    private String companyName;

    /**
     * 公司秘钥
     */
    @TableField("company_secret")
    private String companySecret;

    /**
     * 基础应用的id
     */
    @TableField("base_appid")
    private Integer baseAppId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Timestamp createTime;

}
