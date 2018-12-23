// 这个是same型
package cn.wlh;
import  cn.wlh.model.Person;
import cn.wlh.service.impl.OrderServiceImpl;
global OrderServiceImpl orderService;

rule "ID"
    when
        $p : Person()
        $pp : Person(phone == $p.phone, user_id != $p.user_id) from orderService.getListOrderByStatus(0);
    then
        System.out.println("电话号码相同， 用户id不同");
        $p.setTotalScore(  $p.getTotalScore() - 25);
end

