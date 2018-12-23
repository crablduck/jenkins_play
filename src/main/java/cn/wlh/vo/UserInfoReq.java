package cn.wlh.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoReq implements Serializable {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 父用户id
     */
    private Integer createrId;

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
     * 密码
     */
    private String password;

    /**
     * 公司id
     */
    private Integer companyId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 商品id
     */
    private Integer commodityId;

    /**
     * 状态（0：启用，1：禁用）
     */
    private Integer state;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 描述
     */
    private String descr;

}
