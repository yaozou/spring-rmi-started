package com.xpand.core;

/**
 * @Description:
 * @author: yaozou
 * @Date: 2019/1/23 15:44
 */
public class RmiThreadLocal {
    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void set(String uuid){
        threadLocal.set(uuid);
    }

    public static String get(){
        String uuid = threadLocal.get();
        if(uuid == null){
           return null;
        }
        return uuid;
    }
}
