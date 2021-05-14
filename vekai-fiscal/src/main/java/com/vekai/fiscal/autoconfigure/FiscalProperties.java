package com.vekai.fiscal.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 记账处理配置类
 */
@ConfigurationProperties(prefix = "com.vekai.fiscal", ignoreUnknownFields = true)
public class FiscalProperties {
    private Integer bookDefaultInitYear = 10;

    public Integer getBookDefaultInitYear() {
        return bookDefaultInitYear;
    }

    public void setBookDefaultInitYear(Integer bookDefaultInitYear) {
        this.bookDefaultInitYear = bookDefaultInitYear;
    }
}
