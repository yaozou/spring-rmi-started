package com.xpand.rmi.config;

import com.xpand.rmi.XpandRmiServiceExporter;
import com.xpand.rmi.service.TestRmiService;

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
@ComponentScan(basePackages = { "com.xpand.rmi.*" })
public class MainConfig {
    @Bean
    public RmiServiceExporter rmiExporter(TestRmiService service){
        RmiServiceExporter exporter = new XpandRmiServiceExporter();
        exporter.setService(service);
        exporter.setServiceName("testRmiService");
        exporter.setServiceInterface(TestRmiService.class);
        return exporter;
    }

}
