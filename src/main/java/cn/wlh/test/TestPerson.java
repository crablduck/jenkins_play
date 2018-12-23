package cn.wlh.test;


import cn.wlh.model.Person;
import cn.wlh.service.IOrderService;
import cn.wlh.service.impl.OrderServiceImpl;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class TestPerson {

    @Autowired
    private static OrderServiceImpl orderService;

    public static void main(String[] args) {
        KieContainer kc = KieServices.Factory.get().getKieClasspathContainer();
        KieSession kieSession =
                kc.newKieSession("age");
        Person person4 = new Person();
        person4.setIdcard("545234234");
        person4.setPhone("1235");
        person4.setUser_id(2);
        person4.setAge(17);

//                ArrayList<Person> people = new ArrayList<>();
//        Person person = new Person();
//        Person person1 = new Person();
//        person1.setIdcard("431");
//
//
//        Person person2 = new Person();
//        person.setIdcard("545234234");
//        person.setPhone("1235");
//        person.setUser_id(23);
//        person2.setIdcard("545234234");
//
//        people.add(person);
//        people.add(person1);
//        people.add(person2);


        kieSession.setGlobal("orderService", orderService);
        kieSession.insert(person4);
        kieSession.fireAllRules();

        System.out.println(person4);

    }


    public void testPerson() {
        getPerson("hello");
        getPerson("goodBoy");
    }

    private String getPerson(String ruleDescription) {
        ArrayList<String> descriptions = new ArrayList<>();
        descriptions.add(ruleDescription);
        String description = descriptions.toString();
        return description.substring(0, description.length());
    }
}
