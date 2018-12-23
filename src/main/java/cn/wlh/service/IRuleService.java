package cn.wlh.service;

import cn.wlh.model.Rule;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.List;

public interface IRuleService extends IService<Rule> {
    Page getRule(Page<Rule> page, Rule rule);

    boolean addOrUpdateRule(Rule rule) throws IOException, TemplateException;

    Rule getRuleById(Integer ruleId);

    boolean deleteRule(Integer ruleId);

    boolean updateRuleStatus(Integer ruleId);

    boolean addRuleContent(Rule rule) throws IOException;

    List<Rule> getRuleByCompanyId(Integer compayId);

    List<Rule> getRuleByAppname(String appName, Integer ruleStatus);

    List<Rule> getRuleByStrategyId(Integer integer, int i);
}
