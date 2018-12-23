package cn.wlh.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoRes implements Serializable {

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
     * 密码
     */
    private String password;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 商品id
     */
    private Integer commodityId;

    /**
     * 商品名称
     */
    private String commodityName;

    /**
     * 创建人id
     */
    private Integer createrId;

    /**
     * 创建人姓名
     */
    private String createrName;

    /**
     * 创建人账户
     */
    private String createrAccount;

    /**
     * 公司id
     */
    private Integer companyId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 子账户个数
     */
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
     * 角色id
     */
    private Integer roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 创建时间
     */
    private String createTime;



}
