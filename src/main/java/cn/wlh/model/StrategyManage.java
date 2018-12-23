package cn.wlh.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

@Data
@TableName("strategy_manage")
public class StrategyManage {

    /**
     * 主键id
     */
    @TableId(value = "strategy_manage_id", type = IdType.AUTO)
    private Integer strategyManageId;

    /**
     * 策略集合
     */
    private String strategy;

    /**
     * 策略数量
     */
    @TableField("strategy_num")
    private Integer strategyNum;

    /**
     * 规则数量
     */
    @TableField("rule_num")
    private Integer ruleNum;

    /**
     * 描述
     */
    private String description;

    /**
     * 应用名称
     */
    @TableField("app_name")
    private String appName;

    @TableField("user_id")
    private Integer userId;

    /**
     * 状态（0：启用，1：禁用）
     */
    private Integer status;

    /**
     * 添加时间
     */
    @TableField("add_time")
    private Date addTime;

    /**
     * 更新的时间
     */
    @TableField("modify_time")
    private Date modifyTime;
}
