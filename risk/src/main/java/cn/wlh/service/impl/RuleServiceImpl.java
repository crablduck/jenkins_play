package cn.wlh.service.impl;

import cn.wlh.mapper.RuleMapper;
import cn.wlh.model.DrlRela1;
import cn.wlh.model.Rule;
import cn.wlh.model.Strategy;
import cn.wlh.model.StrategyManage;
import cn.wlh.service.IRuleService;
import cn.wlh.service.IStrategyManageService;
import cn.wlh.service.IStrategyService;
import cn.wlh.util.TemplateDrl;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RuleServiceImpl extends ServiceImpl<RuleMapper, Rule> implements IRuleService {

    @Value("${baseAppName}")
    private String baseAppName;

    @Autowired
    private IStrategyManageService strategyManageService;

    @Autowired
    private IStrategyService strategyService;

    @Override
    public Page getRule(Page<Rule> page, Rule rule) {

        Integer strategyId = rule.getStrategyId();
        String ruleName = rule.getRuleName();
        Integer status = rule.getStatus();
        Wrapper<Rule> wrapper = new EntityWrapper<>();
        if(null != status){
            wrapper.eq("status", status);
        }
        if(null != strategyId){
            wrapper.eq("strategy_id", strategyId);
        }
        if(null != ruleName && !"".equalsIgnoreCase(ruleName)){
            wrapper.andNew().like("rule_name", ruleName)
                    .or().like("description", ruleName);
        }

        return this.selectPage(page, wrapper);
    }

    @Override
    public boolean addOrUpdateRule(Rule rule) throws IOException, TemplateException {
        String appName = rule.getAppName();


//        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
//        String baseFreStr = null;
//        if (baseAppName.equals(appName)) {
//            baseFreStr = Rule.class.getResource("/") + "templates/tupulian";
//            baseFreStr = baseFreStr.substring(6, baseFreStr.length());
//        } else {
//            baseFreStr = Rule.class.getResource("/") + "templates/customer";
//            baseFreStr = baseFreStr.substring(6, baseFreStr.length());
//        }

        String baseFreStr = null;
        if (baseAppName.equals(appName)) {
            baseFreStr =   "templates/tupulian";

        } else {
            baseFreStr = "templates/customer";

        }


//        cfg.setDirectoryForTemplateLoading(new File(baseFreStr));
//        cfg.setDefaultEncoding("UTF-8");

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setTemplateLoader(new ClassTemplateLoader(getClass().getClassLoader(), baseFreStr));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);

        Map root = new HashMap();

        root.put("rule", rule);


        String switchTemp = null;
        if (baseAppName.equals(appName)) {
            switchTemp = switchBaseTemp(rule.getRuleType());
        } else {
            switchTemp = switchTemp(rule.getRuleType());
        }



        Template temp = cfg.getTemplate(switchTemp);
        StringWriter writer = new StringWriter();
        temp.process(root, writer);
        String str = writer.toString(); //存到数据库
        System.out.println(str);
        rule.setRuleContent(str);

        boolean b = this.insertOrUpdate(rule);
        if (b == true) {
            Integer strategyId = rule.getStrategyId();
            //策略规则数目加一
            Strategy strategy = strategyService.selectById(strategyId);
            Integer ruleNum = strategy.getRuleNum();
            ruleNum = ruleNum + 1;
            strategy.setRuleNum(ruleNum);
            strategyService.updateById(strategy);

            //策略集数目加一
            Integer strategiesId = strategy.getStrategiesId();
            StrategyManage strategyManage = strategyManageService.selectById(strategiesId);
            Integer strategyM = strategyManage.getRuleNum();
            strategyM = strategyM + 1;
            strategyManage.setRuleNum(strategyM);
            strategyManageService.updateById(strategyManage);
            return b;
        }
        return false;

    }

    @Override
    public Rule getRuleById(Integer ruleId) {
        return this.selectById(ruleId);
    }

    @Override
    public boolean deleteRule(Integer ruleId) {
        Rule rule = this.selectById(ruleId);
        Integer strategyId = rule.getStrategyId();

        boolean b = this.deleteById(ruleId);
        if (b == true) {
            //策略规则数目加一
            Strategy strategy = strategyService.selectById(strategyId);
            Integer ruleNum = strategy.getRuleNum();
            ruleNum = ruleNum - 1;
            strategy.setRuleNum(ruleNum);
            strategyService.updateById(strategy);

            //策略集数目加一
            Integer strategiesId = strategy.getStrategiesId();
            StrategyManage strategyManage = strategyManageService.selectById(strategiesId);
            Integer strategyM = strategyManage.getRuleNum();
            strategyM = strategyM - 1;
            strategyManage.setRuleNum(strategyM);
            strategyManageService.updateById(strategyManage);

            return true;
        }
        return false;
    }

    @Override
    public boolean updateRuleStatus(Integer ruleId) {
        Rule byId = this.selectById(ruleId);
        Integer status = byId.getStatus();
        if (status == 0) {
            status = 1;
        } else {
            status =0;
        }
        byId.setStatus(status);
        return this.updateById(byId);
    }

    @Override
    public boolean addRuleContent(Rule rule) {

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        String str1 = RuleServiceImpl.class.getResource("/")+"templates";
        str1 = str1.substring(6, str1.length());
        try {
            cfg.setDirectoryForTemplateLoading(new File(str1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        cfg.setDefaultEncoding("UTF-8");

        Map root = new HashMap();

        DrlRela1 age1 = TemplateDrl.swagger("cn.wlh", "age", "23", "50");
        root.put("drl", age1);
        Template temp = null;
        try {
            temp = cfg.getTemplate("relaTemp.ftl");
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringWriter writer = new StringWriter();
        try {
            temp.process(root, writer);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String str = writer.toString(); //存到数据库

        rule.setRuleContent(str);

        return this.insert(rule);
    }

    @Override
    public List<Rule> getRuleByCompanyId(Integer companyId) {
        Wrapper<Rule> ruleEntityWrapper = new EntityWrapper<>();
        ruleEntityWrapper.eq("company_id", companyId);
        return this.selectList(ruleEntityWrapper);
    }

    @Override
    public List<Rule> getRuleByAppname(String appName, Integer ruleStatus) {
        Wrapper<Rule> ruleEntityWrapper = new EntityWrapper<>();
        ruleEntityWrapper.eq("app_name", appName);
        ruleEntityWrapper.eq("status", ruleStatus);
        return this.selectList(ruleEntityWrapper);
    }

    @Override
    public List<Rule> getRuleByStrategyId(Integer integer, int i) {
        EntityWrapper<Rule> ruleEntityWrapper = new EntityWrapper<>();
        ruleEntityWrapper.eq("strategy_id", integer);
        ruleEntityWrapper.eq("status", 1);
        return this.selectList(ruleEntityWrapper);
    }


    //普通用户的模板
    private String switchTemp(String ruleType){

        String temp = null;
        switch (ruleType) {
            case "over_day":
            case "age":
            case "vocation":
            case "education":
                temp = "relaTemp.ftl";
                break;
            case "city":
                temp = "occupation.ftl";
                break;
            case "entry_area":
                temp = "entryArea.ftl";
                break;
            case "address_book_number":
                temp = "communicationNum.ftl";
                break;
            case "idcard":
                temp = "idCard.ftl";
                break;
            case "equipment_number":
                temp = "equipmentNum.ftl";
                break;
            case "mail_count":
            case "phone_number":
                temp = "phoneNum.ftl";
                break;
//            default:
            case "phone_same":
                temp = "sameContact.ftl";
                break;

        }
        return temp;
    }

    //图谱连专用模板
    private String switchBaseTemp(String ruleType){

        String temp = null;
        switch (ruleType) {
            case "mail_count":
            case "age":
            case "over_day":
            case "vocation":
            case "education":
                temp = "relaTemp.ftl";
                break;

            case "city":
                temp = "occupation.ftl";
                break;
            case "idcard":
                temp = "idCard.ftl";
                break;
            case "equipment_number":
                temp = "equipmentNum.ftl";
                break;
            case "phone_same":
                temp = "sameContact.ftl";
                break;


        }
        return temp;
    }
}
