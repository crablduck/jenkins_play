package cn.wlh.model;

import lombok.Data;

@Data
public abstract class Drl {
    //包名
    protected String packageName;

    //import的类名
    protected String importClass;

    //类名
    protected String className;

    //属性名
    protected String fieldName;

    //规则名字
    protected String ruleName;

    //lhf
    protected String whenConfition;

    //rgh
    protected String thenDo;


}
