package cn.wlh.util;

import java.util.UUID;

public class UUIdUtil {

    public static String getUUID(){
        String uuid = UUID.randomUUID().toString();

        //去掉“-”符号
        return uuid.replaceAll("-", "");
    }
}
