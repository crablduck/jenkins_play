package cn.wlh.model;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

@Data
@TableName("consume_record")
public class ConsumeRecord {

    @TableId(value = "consume_id", type = IdType.AUTO)
    private Integer consumeId;

    @TableField("consume_amount")
    private Float consumeAmount;

    @TableField("invoke_time")
    private Integer invokeTime;

    @TableField("consume_price")
    private Float consumePrice;

    @TableField("application_name")
    private String applicationName;

    @TableField("project_name")
    private String projectName;

    @TableField("consume_time")
    private Date consumeTime;

    @TableField("account_id")
    private Integer accountId;

    @TableField("company_name")
    private String company_name;


}
