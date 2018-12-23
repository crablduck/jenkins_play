package cn.wlh.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

@Data
public class Rule {

    @TableId(value = "rule_id", type = IdType.AUTO)
    private Integer ruleId;

    @TableField("rule_name")
    private String ruleName;

    @TableField("rule_type")
    private String ruleType;

    @TableField("app_name")
    private String appName;

    @TableField("compare_rela_value")
    private String compareRelaValue;

    @TableField("risk_decision")
    private Integer riskDecision;

    @TableField("description")
    private String description;

    @TableField("status")
    private Integer status;

    @TableField("company_id")
    private Integer company_id;

    @TableField("strategy_id")
    private Integer strategyId;

    @TableField("add_time")
    private Date addTime;

    @TableField("modify_time")
    private Date modifyTime;

    @TableField("rule_content")
    private String ruleContent;

    @TableField("rule_value")
    private String ruleValue;

    @TableField("hit_score")
    private Integer hitScore;
}

