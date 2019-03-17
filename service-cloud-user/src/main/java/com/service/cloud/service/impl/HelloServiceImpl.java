package com.service.cloud.service.impl;


import com.service.cloud.service.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "it is ok:"+name;
    }
}
