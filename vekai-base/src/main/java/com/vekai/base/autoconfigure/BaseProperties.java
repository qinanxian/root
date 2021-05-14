package com.vekai.base.autoconfigure;

import cn.fisok.raw.kit.ListKit;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "com.vekai.base", ignoreUnknownFields = true)
public class BaseProperties {

    private String viewPath = "/views";

    private String numberFormat = "#";

    private List<String> autoIncludes = ListKit.listOf("/base/macro/body.ftl","/base/macro/pagelet.ftl");

    private long multipartMaxFileSize = 1024L * 1024L * 100;//最大上传文件大小，100M

    private String devBaseDir = "";
    private String dictDataClasspath = "";

    public long getMultipartMaxFileSize() {
        return multipartMaxFileSize;
    }

    public void setMultipartMaxFileSize(long multipartMaxFileSize) {
        this.multipartMaxFileSize = multipartMaxFileSize;
    }

    public String getViewPath() {
        return viewPath;
    }

    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }

    public List<String> getAutoIncludes() {
        return autoIncludes;
    }

    public void setAutoIncludes(List<String> autoIncludes) {
        this.autoIncludes = autoIncludes;
    }

    public String getNumberFormat() {
        return numberFormat;
    }

    public void setNumberFormat(String numberFormat) {
        this.numberFormat = numberFormat;
    }

    public String getDevBaseDir() {
        return devBaseDir;
    }

    public void setDevBaseDir(String devBaseDir) {
        this.devBaseDir = devBaseDir;
    }

    public String getDictDataClasspath() {
        return dictDataClasspath;
    }

    public void setDictDataClasspath(String dictDataClasspath) {
        this.dictDataClasspath = dictDataClasspath;
    }
}
