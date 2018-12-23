package cn.wlh.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户表
 */
@Data
@TableName("system_user")
public class SystemUser implements Serializable {

    /**
     * 用户id
     */
    @TableId(value = "user_id",type = IdType.AUTO)
    private Integer userId;

    /**
     * 用户账户
     */
    @TableField("user_account")
    private String userAccount;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户手机号
     */
    @TableField("user_phone")
    private String userPhone;

    /**
     * 商户id
     */
    @TableField("commodity_id")
    private Integer commodityId;

    /**
     * 创建人id
     */
    @TableField("creater_id")
    private Integer createrId;

    /**
     * 公司id
     */
    @TableField("company_id")
    private Integer companyId;

    /**
     * 子账户个数
     */
    @TableField("sub_account_num")
    private Integer subAccountNum;

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
