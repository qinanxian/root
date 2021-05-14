package com.vekai.office.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.vekai.office", ignoreUnknownFields = true)
public class OfficeProperties {

    private PageOfficeProperties pageOffice = new PageOfficeProperties();

    public PageOfficeProperties getPageOffice() {
        return pageOffice;
    }

    public void setPageOffice(PageOfficeProperties pageOffice) {
        this.pageOffice = pageOffice;
    }
}
