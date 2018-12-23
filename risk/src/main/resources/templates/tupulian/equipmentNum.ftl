// 这个是same型
package cn.wlh;
import  cn.wlh.model.Person;
import cn.wlh.service.impl.OrderServiceImpl;
global OrderServiceImpl orderService;

rule "${rule.ruleName}"
    when
        $p : Person()
        ($pp : Person(mobile_id == $p.mobile_id, user_id != $p.user_id && ${rule.compareRelaValue}) from orderService.getAllOrderList();) or
        (Person(phone  ==  $p.phone, user_id != $p.user_id && ${rule.compareRelaValue}) from orderService.getAllOrderList();)
    then
        $p.setBaseDescriptions("${rule.description}");
        $p.setTotalScore(  $p.getTotalScore() - ${rule.hitScore});
end


