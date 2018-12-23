package cn.wlh;
import  cn.wlh.model.Person;
import cn.wlh.service.impl.OrderServiceImpl;
global OrderServiceImpl orderService;
rule "${rule.ruleName}"
    when
        $p : Person(order_status == 1)
        $pp : Person(idcard  ==  $p.idcard && ${rule.compareRelaValue}) from orderService.getListOrderByStatus(0);

    then
        $p.setBaseDescriptions("${rule.description}");
        $p.setTotalScore( $p.getTotalScore() - ${rule.hitScore});
end
