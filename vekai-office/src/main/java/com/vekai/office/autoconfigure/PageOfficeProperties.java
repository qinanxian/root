package com.vekai.office.autoconfigure;

public class PageOfficeProperties {

    private Boolean enable = false;
    private String licensePath = "/data/rober/pageoffice";
    private String staticResourceUrl = "/office/pageoffice/static";

    public static final String DIFF_PATH_SEGMENT = "/real";

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getLicensePath() {
        return licensePath;
    }

    public void setLicensePath(String licensePath) {
        this.licensePath = licensePath;
    }

    public String getStaticResourceUrl() {
        return staticResourceUrl;
    }

    public void setStaticResourceUrl(String staticResourceUrl) {
        this.staticResourceUrl = staticResourceUrl;
    }
}
