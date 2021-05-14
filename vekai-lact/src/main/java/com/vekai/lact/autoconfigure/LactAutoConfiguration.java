package com.vekai.lact.autoconfigure;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@EnableConfigurationProperties(LactProperties.class)
@ComponentScan(basePackages={"com.vekai.lact"})
@ImportResource("classpath:application-context-lact.xml")
public class LactAutoConfiguration {

//    @Bean
//    @LoadBalanced
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }

}
