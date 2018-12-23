package cn.wlh;
import  cn.wlh.model.Person;
import  java.util.*;
global ArrayList<Person> phoneList;
rule " ${rule.ruleName}"
    when
        $p : Person()
        $pp : Person(phone  ==  $p.phone && ${rule.compareRelaValue}) from phoneList;

    then
        $p.setDescriptions("${rule.description}");
        $p.setOrder_status(${rule.riskDecision});

end



