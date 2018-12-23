package cn.wlh.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

@Data
public class Strategy {

    /**
     * 主键id
     */
    @TableId(value = "strategy_id", type = IdType.AUTO)
    private Integer strategyId;

    /**
     * 策略
     */
    private String strategy;

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
     * 状态（0：启用，1：禁用）
     */
    private Integer status;

    /**
     * 策略集的id
     */
    @TableField("strategies_id")
    private Integer strategiesId;

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
