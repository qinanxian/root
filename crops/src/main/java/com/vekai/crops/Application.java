package com.vekai.crops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

//@SpringBootApplication(exclude = { SwaggerAutoConfiguration.class,
//        org.activiti.spring.boot.SecurityAutoConfiguration.class,
//        org.apache.shiro.spring.config.web.autoconfigure.ShiroAnnotationProcessorAutoConfiguration.class
//})
//@Import({
//        DispatcherServletAutoConfiguration.class,
//        EmbeddedServletContainerAutoConfiguration.class,
//        ErrorMvcAutoConfiguration.class,
//        HttpEncodingAutoConfiguration.class,
//        HttpMessageConvertersAutoConfiguration.class,
//        JacksonAutoConfiguration.class,
//        JmxAutoConfiguration.class,
//        MultipartAutoConfiguration.class,
//        ServerPropertiesAutoConfiguration.class,
//        PropertyPlaceholderAutoConfiguration.class,
//        WebMvcAutoConfiguration.class,
//        WebSocketAutoConfiguration.class,
//        JdbcTemplateAutoConfiguration.class,
//        BaseAutoConfiguration.class,
//        SqlAutoConfiguration.class
//})
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
@EnableCaching
@EnableAsync
public class Application {
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
        ApplicationContext applicationContext = SpringApplication.run(Application.class,args);
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
