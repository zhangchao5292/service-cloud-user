package com.service.cloud.proxy;


import com.service.cloud.common.http.RpcExport;
import com.service.cloud.service.HelloService;
import com.service.cloud.service.impl.HelloServiceImpl;

public class RpcProvider {
    public static void main(String[] args) throws Exception {
        HelloService service = new HelloServiceImpl();
        RpcExport.export(service);
        String a="sdf";
        String b="sdf";
        System.out.println(a==b);
        System.out.println(a.equals(b));
    }
}