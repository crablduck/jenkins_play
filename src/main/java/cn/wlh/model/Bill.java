package cn.wlh.model;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("bill_manage")
public class Bill {

    @TableId(value = "bill_id", type = IdType.AUTO)
    private Integer billId;

    @TableField("account")
    private String account;

    @TableField("company_name")
    private String companyName;

    @TableField("phone_number")
    private String phoneNumber;

    @TableField("balance")
    private Float balance;

    @TableField("add_time")
    private Timestamp addTime;

    @TableField("company_id")
    private Integer company_id;

    @TableField(exist = false)
    private Integer userId;

    @TableField(exist = false)
    private Float rechargeNum;

    @TableField(exist = false)
    private String rangeTime;

}
