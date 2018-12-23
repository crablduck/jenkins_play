package cn.wlh.controller.order;

import cn.wlh.model.Commodity;
import cn.wlh.model.Rule;
import cn.wlh.model.SystemUser;
import cn.wlh.service.CommodityService;
import cn.wlh.service.IRuleService;
import cn.wlh.service.SystemUserService;
import cn.wlh.vo.SimpleMsgVO;
import cn.wlh.vo.TableMsgVO;
import com.baomidou.mybatisplus.plugins.Page;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("rule")
public class RuleController extends BaseController{

    @Autowired
    private SystemUserService systemUserServiceImpl;

    @Autowired
    private CommodityService commodityServiceImpl;

    @Autowired
    private IRuleService ruleService;

    @RequestMapping("/getRuleList")
    public Object getRuleList(Page<Rule> page, Rule rule) {
        try {
            page  = ruleService.getRule(page, rule);
        } catch (Exception e) {
            logger.info("服务器调用失败");
            return SimpleMsgVO.get500Fail();
        }
        logger.info("调用成功");
        return TableMsgVO.getOk(page.getTotal(),page.getRecords());
    }

    @RequestMapping("/addOrUpdateRule/{userId}")
    public Object addOrUpdateRule(Rule rule, @PathVariable("userId") Integer userId){
        boolean addStatus = false;
//        try {

        //找到app名字然后放入到数据库中， 之后要根据app名字进行查找
        Integer commodityId = systemUserServiceImpl.getUserByUserId(userId).getCommodityId();
        Commodity appNameByUserId =
                commodityServiceImpl.getAppNameByUserId(commodityId);
        String commodityName = appNameByUserId.getCommodityName();
        rule.setAppName(commodityName);

        rule.setAddTime(new Date());
        try {
            addStatus  = ruleService.addOrUpdateRule(rule);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
//        } catch (Exception e) {
//            logger.info("服务器调用失败");
//            return SimpleMsgVO.get500Fail();
//        }
        logger.info("调用成功");
        return SimpleMsgVO.getOk(addStatus);
    }

    @RequestMapping("/getRuleById/{ruleId}")
    public Object getRuleById(@PathVariable("ruleId") Integer ruleId){

        Rule rule;
        try {
            rule  = ruleService.getRuleById(ruleId);
        } catch (Exception e) {
            logger.info("服务器调用失败");
            return SimpleMsgVO.get500Fail();
        }
        logger.info("调用成功");
        return SimpleMsgVO.getOk(rule);
    }

    @RequestMapping("/deleteRule/{ruleId}")
    public Object deleteRule(@PathVariable("ruleId") Integer ruleId){

        boolean delStatus = false;
//        try {
            delStatus  = ruleService.deleteRule(ruleId);
//        } catch (Exception e) {
//            logger.info("服务器调用失败");
//            return SimpleMsgVO.get500Fail();
//        }
        logger.info("调用成功");
        return SimpleMsgVO.getOk(delStatus);
    }

    @RequestMapping("/updateRuleStatus/{ruleId}")
    public Object updateRuleStatus(@PathVariable("ruleId") Integer ruleId){

        boolean delStatus = false;
        try {
            delStatus  = ruleService.updateRuleStatus(ruleId);
        } catch (Exception e) {
            logger.info("服务器调用失败");
            return SimpleMsgVO.get500Fail();
        }
        logger.info("调用成功");
        return SimpleMsgVO.getOk(delStatus);
    }


}
