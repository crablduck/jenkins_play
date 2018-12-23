package cn.wlh.test;


import cn.wlh.model.Person;
import cn.wlh.model.Rule;
import cn.wlh.util.StringUtil;
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
import java.text.SimpleDateFormat;
import java.util.*;

public class Freemark3 {

    public static void main(String[] args) throws Exception {
        String emergency_contact = "西门吹雪:1355452451, 孤独大侠:13254565848";
        String[] split = emergency_contact.split(",");
        ArrayList<HashMap<String, String>> hashMaps = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            HashMap<String, String> stringStringHashMap = new HashMap<>();
            String[] split1 = split[i].split(":");
            stringStringHashMap.put(split1[0], split1[1]);
            hashMaps.add(stringStringHashMap);
        }

        System.out.println(hashMaps);
    }


}
