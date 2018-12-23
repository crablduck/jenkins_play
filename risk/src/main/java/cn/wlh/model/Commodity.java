package cn.wlh.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 商品表
 */
@Data
@TableName("commodity")
public class Commodity implements Serializable {

    /**
     * 商品id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 商品编号
     */
    @TableField("commodity_number")
    private String commodityNumber;

    /**
     * 商品名称
     */
    @TableField("commodity_name")
    private String commodityName;

    /**
     * 商品秘钥
     */
    @TableField("commodity_secret")
    private String commoditySecret;

    /**
     * 公司id
     */
    @TableField("company_id")
    private Integer companyId;

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
