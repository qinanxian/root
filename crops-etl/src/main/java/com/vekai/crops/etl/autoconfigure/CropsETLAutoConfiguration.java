package com.vekai.crops.etl.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CropsETLProperties.class)
@ComponentScan(basePackages={"com.vekai.crops.etl"})
public class CropsETLAutoConfiguration {

    private CropsETLProperties properties;

    public CropsETLAutoConfiguration(CropsETLProperties properties) {
        this.properties = properties;
    }
}
