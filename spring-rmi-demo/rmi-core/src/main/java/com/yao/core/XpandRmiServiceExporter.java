package com.yao.core;

import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.remoting.support.RemoteInvocation;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * @Description:
 * @author: yaozou
 * @Date: 2019/1/21 18:18
 */
public class XpandRmiServiceExporter extends RmiServiceExporter {
    public XpandRmiServiceExporter(){
        super();
    }
    @Override
    protected Object invoke(RemoteInvocation invocation, Object targetObject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Serializable requestId = invocation.getAttribute("requestId");
        if (requestId != null){
            RmiThreadLocal.set((String) requestId);
        }
        return super.invoke(invocation, targetObject);
    }
}
