package cn.wlh.controller.order;

import cn.wlh.controller.user.RiskController;
import cn.wlh.model.*;
import cn.wlh.service.*;
import cn.wlh.service.impl.OrderServiceImpl;
import cn.wlh.util.UUIdUtil;
import cn.wlh.vo.SimpleMsgVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.kie.api.KieBase;
import org.kie.api.command.Command;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.command.CommandFactory;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class AsynController  extends BaseController{

    @Value("${baseAppName}")
    private String baseAppName;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IRuleService ruleServiceImpl;

    @Autowired
    SystemCompanyService systemCompanyServiceImpl;

    @Autowired
    private IStrategyManageService strategyManageService;

    @Autowired
    private IStrategyService strategyService;

    @Autowired
    CommodityService commodityServiceImpl;

    /**
     * 1. 验证用户
     * @param person
     * @return
     */
    @RequestMapping("/asyn")
    public Object asyn(@RequestBody Person person){
        //检查3元素
        SimpleMsgVO o = Check3El(person);
        String code = o.getCode();

        if ("200".equals(code)) {
            //根据公司名字进行查找规则, 如果应用名是公司基础账号就执行一次， 不然则再执行一次传入的appName的规则
            String companyBase= commodityServiceImpl.getBaseAppName(person.getApp_name());
            List<Rule> ruleByAppName = new ArrayList<>();

            String app_name = person.getApp_name();
            if (app_name.equals(companyBase)) {

                //找公司及账号的应用
                List<Rule> miniRuel = this.getMiniRuel(app_name);
                ruleByAppName.addAll(miniRuel);
            } else {
                List<Rule> companyBaseRule = this.getMiniRuel(companyBase);
                List<Rule> appRule =  this.getMiniRuel(app_name);
                ruleByAppName.addAll(companyBaseRule);
                ruleByAppName.addAll(appRule);
            }


            //这里是区区域管理员的规则
            List<Rule> baseRule = this.getMiniRuel(this.baseAppName);
            ruleByAppName.addAll(baseRule);

            ArrayList<String> ruleContents = new ArrayList<>();
            for (Rule rule:
                 ruleByAppName) {
                 ruleContents.add(rule.getRuleContent());
            }

            //把规则内容都过一遍然后存到person的描述里面
            for ( String ruleContent : ruleContents){
                    if (ruleContent != null && !ruleContent.equals("")){
                        asynUtil(ruleContent, person);
                    }
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = simpleDateFormat.format(System.currentTimeMillis());

            person.setAdd_time(new Date());
            String uuid = UUIdUtil.getUUID();
            person.setApply_oid(person.getOrder_no() + uuid);

            boolean addOrderStatus = orderService.addOrder(person);
           if (addOrderStatus == true) {
               return SimpleMsgVO.getOk(person.getApply_oid());
           }else {
               return SimpleMsgVO.get500Fail("添加不成功");
           }
        }else {
            return SimpleMsgVO.get407Fail();
        }

    }

    @RequestMapping("getRest")
    public Object getRest(@RequestBody Person person) {
        HashMap<String, Object> orderByOid = orderService.getOrderByOid(person);

        return SimpleMsgVO.getOk(orderByOid);
    }

    private void asynUtil(String ruleContent, Person person){

        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(ruleContent, ResourceType.DRL);
        KieBase build = kieHelper.build();
        StatelessKieSession statelessKieSession = build.newStatelessKieSession();

        System.out.println(orderService.getAllOrderList());

        //把紧急联系人拿出来,进行拼成person 然后进行对比
        Set<String> conTactList = person.getConTactList();
        ArrayList<Person> people = new ArrayList<>();
        Iterator<String> iterator = conTactList.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            Person person1 = new Person();
            person1.setPhone(next);

            people.add(person1);
        }

        statelessKieSession.setGlobal("phoneList", people);
        statelessKieSession.setGlobal("orderService", orderService);
        Command command = CommandFactory.newInsert(person);

        statelessKieSession.execute(command);
    }

    private SimpleMsgVO Check3El(Person person) {
        HashMap<String, String> che3E = new HashMap<>();
        che3E.put("company_secret", person.getCompany_secret());
        che3E.put("app_name", person.getApp_name());
        che3E.put("app_code", person.getApp_code());

        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(che3E);
        SimpleMsgVO o = null;
        try {
            return o = (SimpleMsgVO) check3Ele(jsonObject.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SimpleMsgVO.get500Fail();
    }

    public Object check3Ele(String companyInfo){
        JSONObject data= new JSONObject();

        JSONObject companyInfoJson = null;
        //判断json格式
        try{
            companyInfoJson = JSON.parseObject(companyInfo);
        }catch(Exception e){
            return SimpleMsgVO.get407Fail();
        }
        //非空校验
        String validateMsg = validate(companyInfoJson);
        if(null != validateMsg){
            return SimpleMsgVO.get406Fail(validateMsg);
        }
        //判断该公司是否存在
        SystemCompany systemCompany = systemCompanyServiceImpl.checkCompanyBySecret(companyInfoJson.getString("company_secret"));
        if(null == systemCompany){
            return SimpleMsgVO.get408Fail("company secret is error");
        }
        //判断该公司下是否有该商品
        if(!commodityServiceImpl.checkCommodity(companyInfoJson)){
            return SimpleMsgVO.get408Fail("app code or name is error");
        }
        data.put("company_id",systemCompany.getCompanyId());
        return  SimpleMsgVO.getOk(data);
    }

    /**
     * 非空校验
     * @param json
     * @return
     */
    private String validate(JSONObject json) {
        if(StringUtils.isEmpty(json.getString("company_secret"))){
            return "company_secret can not be empty";
        }else if(StringUtils.isEmpty(json.getString("app_code"))){
            return "app_code can not be empty";
        }else if(StringUtils.isEmpty(json.getString("app_name"))){
            return "app_name can not be empty";
        }else{
            return null;
        }
    }

    /**
     * 根据appName得到， 启用 并且 appName属性一致的rule, 要求要一层层下去的
     */
    private List<Rule> getMiniRuel(String app_name) {

        //根据appName得到第一层的id
        List<StrategyManage> byAppNameStatus = strategyManageService.getByAppNameStatus(app_name, 1);
        List<Integer> strategyManageIds = new ArrayList<>();
        for (int i = 0; i < byAppNameStatus.size(); i++) {
            StrategyManage strategyManage = byAppNameStatus.get(i);
            Integer strategyManageId = strategyManage.getStrategyManageId();
            strategyManageIds.add(strategyManageId);
        }

        //根据第一层id得到第二层id
        ArrayList<Integer> strategies = new ArrayList<>();
        for (int i = 0; i < strategyManageIds.size(); i++) {
            List<Strategy> strateById = strategyService.getStrateByIdStatus(strategyManageIds.get(i), 1);

            for (int j = 0; j < strateById.size(); j++) {
                Strategy strategy = strateById.get(j);
                Integer strategyId = strategy.getStrategyId();
                strategies.add(strategyId);
            }

        }

        //根据id来定位规则
        ArrayList<Rule> rules = new ArrayList<>();
        for (int i = 0; i < strategies.size(); i++) {
            List<Rule> rule = ruleServiceImpl.getRuleByStrategyId(strategies.get(i), 1);
            rules.addAll(rule);
        }
        return rules;
    }

}
