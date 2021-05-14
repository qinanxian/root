package com.vekai.showcase.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.vekai.showcase", ignoreUnknownFields = true)
public class ShowCaseProperties {
}
