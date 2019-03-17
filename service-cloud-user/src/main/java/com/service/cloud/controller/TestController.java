package com.service.cloud.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestController {
    @Value("${spring.cloud.config.server.git.uri:1}")
    private String configUrl;
    @Test
    public void getInfo(){
        System.out.println("get configserver info:"+configUrl);
    }
}
