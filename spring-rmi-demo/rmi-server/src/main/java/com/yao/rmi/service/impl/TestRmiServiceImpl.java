package com.yao.rmi.service.impl;

import com.yao.core.TestModel;
import com.yao.db.service.TestDbService;
import com.yao.rmi.service.TestRmiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @author: yaozou
 * @Date: 2019/1/3 15:50
 */
@Service
@Slf4j
public class TestRmiServiceImpl implements TestRmiService {
    private TestDbService testDbService;
    private RmiProxyFactoryBean rmiService;

    public TestRmiServiceImpl(){}
    @Autowired
    public TestRmiServiceImpl(RmiProxyFactoryBean rmiService){
        this.rmiService = rmiService;
        testDbService = (TestDbService)rmiService.getObject();
    }

    @Override
    public String test(TestModel model) {
        log.info("----rmi start-----");
        testDbService.save();
        log.info("----rmi end-----");
        return "hello";
    }
}
