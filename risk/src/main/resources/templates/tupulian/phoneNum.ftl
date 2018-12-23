// 这个是same型
package cn.wlh;
import  cn.wlh.model.Person;
import cn.wlh.service.impl.OrderServiceImpl;
global OrderServiceImpl orderService;

rule "${rule.ruleName}"
    when
        $p : Person()
        $pp : Person(phone == $p.phone, user_id != $p.user_id) from orderService.getListOrderByStatus(0);
    then
        $p.setBaseDescriptions("${rule.description}");
        $p.setTotalScore(  $p.getTotalScore() - ${rule.hitScore});
end

