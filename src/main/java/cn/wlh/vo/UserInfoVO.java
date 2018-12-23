package cn.wlh.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoVO implements Serializable {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户账户
     */
    private String userAccount;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 商户id
     */
    private Integer commodityId;

    /**
     * 创建人id
     */
    private Integer createrId;

    /**
     * 状态（0：启用，1：禁用）
     */
    private Integer state;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 创建人账户
     */
    private String createrAccount;

    /**
     * 创建人姓名
     */
    private String createrName;

    /**
     * 角色
     */
    private String roleNames;

}
