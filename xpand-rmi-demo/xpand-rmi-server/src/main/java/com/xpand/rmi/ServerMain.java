package com.xpand.rmi;


import com.xpand.rmi.config.MainConfig;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @author: yaozou
 * @Date: 2019/1/3 15:43
 */
@Slf4j
public class ServerMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        log.info("服务发布已发布。");
    }
}
