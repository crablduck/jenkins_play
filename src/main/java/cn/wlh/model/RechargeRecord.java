package cn.wlh.model;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@TableName("recharge_record")
public class RechargeRecord {

    @TableId(value = "recharge_id", type = IdType.AUTO)
    private Integer rechargeId;

    @TableField("recharge_amount")
    private Float rechargeAmount;

    @TableField("current_amount")
    private Float currentAmount;

    @TableField("after_amount")
    private Float afterAmount;

    @TableField("operator_name")
    private String operatorName;

    @TableField("account_id")
    private Integer accountId;

    @TableField("add_time")
    private Date addTime;

    @TableField("company_name")
    private String company_name;

//    @TableField(exist = false)
//    private String rechargeTime;
//
//    public void setRecharge_time(Timestamp addTime) {
//        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String format = sdf.format(addTime);
//        this.rechargeTime = format;
//    }
//
//    public String getRecharge_time() {
//        return rechargeTime;
//    }
}

