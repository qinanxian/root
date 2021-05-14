package com.vekai.fnat.autoconfigure;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FnatProperties.class)
@ComponentScan(basePackages={"com.vekai.fnat"})
public class FnatAutoConfiguration {
}
