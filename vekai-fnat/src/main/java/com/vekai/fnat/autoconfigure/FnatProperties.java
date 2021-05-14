package com.vekai.fnat.autoconfigure;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.vekai.fnat", ignoreUnknownFields = true)
public class FnatProperties {
}
