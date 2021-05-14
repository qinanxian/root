package com.vekai.showcase.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties(ShowCaseProperties.class)
@ComponentScan(basePackages={"com.vekai.showcase"})
public class ShowCaseAutoConfiguration {

    private ShowCaseProperties properties;

    public ShowCaseAutoConfiguration(ShowCaseProperties properties) {
        this.properties = properties;
    }


}
