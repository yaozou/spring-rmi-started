package com.yao.db.service.impl;

import com.yao.db.service.TestDbService;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @author: yaozou
 * @Date: 2019/1/23 16:00
 */
@Service
@Slf4j
public class TestDbserviceImpl implements TestDbService {
    @Override
    public void save() {
        log.info("db save");
    }
}
