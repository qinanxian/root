package com.vekai.office.autoconfigure;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@EnableConfigurationProperties(OfficeProperties.class)
@ComponentScan(basePackages = "com.vekai.office")
public class OfficeAutoConfiguration {
    protected OfficeProperties properties;

    public OfficeAutoConfiguration(OfficeProperties properties) {
        this.properties = properties;
    }
}
