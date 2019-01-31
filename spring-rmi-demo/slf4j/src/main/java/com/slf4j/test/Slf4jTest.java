package com.slf4j.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * @author: yaozou
 * @Date: 2019/1/29 10:05
 */
public class Slf4jTest {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Slf4jTest.class);
        logger.info("ssdfsdf");
    }
}
