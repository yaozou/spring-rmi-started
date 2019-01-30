package com.xpand.rmi.service.impl;

import com.xpand.rmi.service.TestRmiService;

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

    @Override
    public String test() {
        log.info("----rmi start-----");
        log.info("----rmi end-----");
        return "hello";
    }
}
