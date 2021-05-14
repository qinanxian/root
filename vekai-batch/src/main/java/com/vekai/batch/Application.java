package com.vekai.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
public class Application {
    public static void main(String[] args){
        ApplicationContext applicationContext = SpringApplication.run(Application.class,args);
    }
}
