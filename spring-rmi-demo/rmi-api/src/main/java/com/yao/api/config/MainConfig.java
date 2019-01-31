package com.yao.api.config;

import com.yao.core.XpandRmiProxyFactoryBean;
import com.yao.rmi.service.TestRmiService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

/**
 * @Description:
 * @author: yaozou
 * @Date: 2019/1/3 16:01
 */
@Configuration
@ComponentScan(basePackages={"com.yao.api.*"})
public class MainConfig {

    @Bean(name="testRmiService")
    public RmiProxyFactoryBean testRmiService(){
        RmiProxyFactoryBean proxyFactoryBean = new XpandRmiProxyFactoryBean();
        proxyFactoryBean.setServiceUrl("rmi://localhost/testRmiService");
        proxyFactoryBean.setServiceInterface(TestRmiService.class);
        return proxyFactoryBean;
    }
}
