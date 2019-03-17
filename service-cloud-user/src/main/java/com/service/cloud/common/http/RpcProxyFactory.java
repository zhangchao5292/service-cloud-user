package com.service.cloud.common.http;

import java.lang.reflect.Proxy;

public class RpcProxyFactory {
    public static <T> T getService(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] { interfaceClass },
                new RpcHandler());
    }
}
