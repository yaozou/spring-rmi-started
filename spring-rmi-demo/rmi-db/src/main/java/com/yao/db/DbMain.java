package com.yao.db;


import com.yao.db.config.MainConfig;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Description:
 * @author: yaozou
 * @Date: 2019/1/3 15:43
 */
public class DbMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        System.out.println("服务发布已发布。");
    }
}
