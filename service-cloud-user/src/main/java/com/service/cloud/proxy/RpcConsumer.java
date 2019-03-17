package com.service.cloud.proxy;

import com.service.cloud.common.http.RpcProxyFactory;
import com.service.cloud.service.HelloService;

public class RpcConsumer {
    public static void main(String[] args) {
        HelloService proxy = RpcProxyFactory.getService(HelloService.class);
//        proxy.sayHello执行的时候，实际是在执行RpcHandler的invoke方法，也就是远程调用
        String result = proxy.sayHello("world");
        System.out.println(result);
    }
}
