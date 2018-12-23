package cn.wlh;
import  cn.wlh.model.Person;

rule "${rule.ruleName}"
    when
        $p: Person(${rule.ruleType} ${rule.compareRelaValue} "${rule.ruleValue}")
    then
        $p.setDescriptions("${rule.description}");
        $p.setOrder_status(${rule.riskDecision});
end

