package com.xpand.rmi.controller;


import com.xpand.core.RmiThreadLocal;
import com.xpand.rmi.service.TestRmiService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @author: yaozou
 * @Date: 2019/1/3 15:58
 */
@Component
@Slf4j
public class TestController {

    private TestRmiService testRmiService;
    private RmiProxyFactoryBean rmiService;

    public TestController(){}

    @Autowired
    public TestController(RmiProxyFactoryBean rmiService){
        this.rmiService = rmiService;
        this.testRmiService = (TestRmiService)rmiService.getObject();
    }

    public void test(String name,String desc){
        UUID uuid = UUID.randomUUID();
        RmiThreadLocal.set(uuid.toString());
        log.info("--------start:"+uuid.toString()+"--------");
        String str = testRmiService.test();
        log.info("--------end--------");
    }

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(TestController.class);
        logger.info("info ---------");
        log.info("--------end--------");
    }
}
