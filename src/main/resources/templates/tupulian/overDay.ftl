package cn.wlh;
import  cn.wlh.model.Person;

//逾期天数
rule "${rule.ruleName}"
    when
        $p: Person(${rule.ruleType} ${rule.compareRelaValue} ${rule.ruleValue})
    then
        $p.setDescriptions("${rule.description}");
        $p.setTotalScore(  $p.getTotalScore() - 25);
end

