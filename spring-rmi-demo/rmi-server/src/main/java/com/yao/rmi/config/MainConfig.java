package com.yao.rmi.config;

import com.yao.core.XpandRmiProxyFactoryBean;
import com.yao.core.XpandRmiServiceExporter;
import com.yao.db.service.TestDbService;
import com.yao.rmi.service.TestRmiService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.remoting.rmi.RmiServiceExporter;

/**
 * @Description:
 * @author: yaozou
 * @Date: 2019/1/3 15:47
 */
@Configuration
@ComponentScan(basePackages = { "com.yao.rmi.*" })
public class MainConfig {
    @Bean
    public RmiServiceExporter rmiExporter(TestRmiService service){
        RmiServiceExporter exporter = new XpandRmiServiceExporter();
        exporter.setService(service);
        //serviceName属性用来在RMI注册表中注册一个服务
        exporter.setServiceName("testRmiService");
        exporter.setServiceInterface(TestRmiService.class);
        return exporter;
    }

    @Bean(name="testDbService")
    public RmiProxyFactoryBean testDbService(){
        RmiProxyFactoryBean proxyFactoryBean = new XpandRmiProxyFactoryBean();
        proxyFactoryBean.setServiceUrl("rmi://localhost/testDbService");
        proxyFactoryBean.setServiceInterface(TestDbService.class);
        return proxyFactoryBean;
    }
}
