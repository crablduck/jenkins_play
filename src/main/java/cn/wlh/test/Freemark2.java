package cn.wlh.test;


import cn.wlh.model.Person;
import cn.wlh.model.Rule;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.kie.api.KieBase;
import org.kie.api.command.Command;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.command.CommandFactory;
import org.kie.internal.utils.KieHelper;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class Freemark2 {

    public static void main(String[] args) throws Exception {

        /* ------------------------------------------------------------------------ */
        /* You should do this ONLY ONCE in the whole application life-cycle:        */

        /* Create and adjust the configuration singleton */
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        String str1 = Freemark2.class.getResource("/")+"templates";
        str1 = str1.substring(6, str1.length());
        cfg.setDirectoryForTemplateLoading(new File(str1));
        cfg.setDefaultEncoding("UTF-8");
//        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW);

        /* ------------------------------------------------------------------------ */
        /* You usually do these for MULTIPLE TIMES in the application life-cycle:   */


        /**
         * 使用者要输入的： field
         */

        Map root = new HashMap();

        Rule rule = new Rule();
        rule.setRuleName("不要军人");
        rule.setRuleType("vocation");
        rule.setCompareRelaValue("==");
        rule.setRuleValue("军人");
        rule.setDescription("他是军人");

        root.put("rule", rule);
        Template temp = cfg.getTemplate("occupation.ftl");
        StringWriter writer = new StringWriter();
        temp.process(root, writer);
        String str = writer.toString(); //存到数据库

        System.out.println(writer.toString());


        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(str, ResourceType.DRL);
        KieBase build = kieHelper.build();
        StatelessKieSession statelessKieSession = build.newStatelessKieSession();


        Person person = new Person();
        person.setAge(20);
        person.setVocation(1);
        Command command = CommandFactory.newInsert(person);

        statelessKieSession.execute(command);
        System.out.println(person);


    }


}
