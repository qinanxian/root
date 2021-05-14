package com.vekai.showcase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

//@SpringBootApplication(exclude = { SwaggerAutoConfiguration.class,
//        org.activiti.spring.boot.SecurityAutoConfiguration.class,
//        org.apache.shiro.spring.config.web.autoconfigure.ShiroAnnotationProcessorAutoConfiguration.class
//})
@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
public class ShowCaseApplication {
    @Configuration
    static class Cfg {}

    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourceLoader() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setIgnoreUnresolvablePlaceholders(true);
        configurer.setOrder(1);
        return configurer;
    }

    public static void main(String[] args){
        ApplicationContext applicationContext = SpringApplication.run(ShowCaseApplication.class,args);
    }
}
