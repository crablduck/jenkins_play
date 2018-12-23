package cn.wlh;
import  cn.wlh.model.Person;

//通讯规则
rule "${rule.ruleName}"
    when
        $p: Person(${rule.ruleType} ${rule.compareRelaValue} ${rule.ruleValue})
    then
        $p.setBaseDescriptions("${rule.description}");
        $p.setTotalScore(  $p.getTotalScore() - ${rule.hitScore});
end

