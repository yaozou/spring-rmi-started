package com.yao.core;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.rmi.RmiInvocationHandler;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.remoting.support.RemoteInvocation;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

/**
 * @Description:
 * @author: yaozou
 * @Date: 2019/1/21 18:15
 */
public class XpandRmiProxyFactoryBean extends RmiProxyFactoryBean {

    @Override
    protected Object doInvoke(MethodInvocation methodInvocation, RmiInvocationHandler invocationHandler) throws RemoteException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String requestId = RmiThreadLocal.get();
        RemoteInvocation remoteInvocation = createRemoteInvocation(methodInvocation);
        remoteInvocation.addAttribute("requestId",requestId);
        return invocationHandler.invoke(remoteInvocation);
    }
}
