package com.yao.db.config;

import com.yao.core.XpandRmiServiceExporter;
import com.yao.db.service.TestDbService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;

/**
 * @Description:
 * @author: yaozou
 * @Date: 2019/1/3 15:47
 */
@Configuration
@ComponentScan(basePackages = { "com.yao.db.*" })
public class MainConfig {
    @Bean
    public RmiServiceExporter rmiExporter(TestDbService dbservice){
        RmiServiceExporter exporter = new XpandRmiServiceExporter();
        exporter.setService(dbservice);
        //serviceName属性用来在RMI注册表中注册一个服务
        exporter.setServiceName("testDbService");
        exporter.setServiceInterface(TestDbService.class);
        return exporter;
    }
}
