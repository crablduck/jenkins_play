package cn.wlh.util;

import cn.wlh.model.DrlRela1;
import cn.wlh.model.Person;


public class TemplateDrl {

    /**
     * 关系型的规则表达式
     * @param packageName
     * @param field
     * @param upAge
     * @param downAge
     * @return
     */
    public static DrlRela1 swagger(String packageName, String field, String upAge, String downAge){
        DrlRela1 drlRela1 = new DrlRela1();
        drlRela1.setPackageName(packageName);

        String importClass = Person.class.toString();
        importClass = importClass.substring(importClass.lastIndexOf(" ")+1, importClass.length());
        String className = importClass.substring(importClass.lastIndexOf(".")+1, importClass.length());

        drlRela1.setImportClass(importClass);
        drlRela1.setClassName(className);
        drlRela1.setFieldName(field);

        drlRela1.setUpCondition(upAge);
        drlRela1.setDownCondition(downAge);
        return drlRela1;
    }

    /**
     *
     */
}
