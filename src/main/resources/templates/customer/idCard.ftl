package cn.wlh;
import  cn.wlh.model.Person;
import cn.wlh.service.impl.OrderServiceImpl;
global OrderServiceImpl orderService;
rule "${rule.ruleName}"
    when
        $p : Person()
        $pp : Person(idcard  ==  $p.idcard && ${rule.compareRelaValue}) from orderService.getListOrderByStatus(0);

    then
        $p.setDescriptions("${rule.description}");
        $p.setOrder_status(${rule.riskDecision});
end
