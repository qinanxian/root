package com.vekai.dataform.autoconfigure;

import cn.fisok.raw.kit.ListKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "com.vekai.dataform", ignoreUnknownFields = true)
public class DataFormProperties {
    public static final String TYPE_XML = "xml";
    public static final String TYPE_DATABASE = "database";
    public static final String TYPE_JSON = "json";

    private String devBaseDir = "";
    private String dataformDataClasspath = "";
    private List<String> dataformIgnoreResources = ListKit.listOf(
            "repo/dataform/category.json"
    );

    private String dataformTagName = "dataform";
    private String resourcePathTpl = "{0}/src/main/resources";
    private boolean authResourcePath = true;    //自动计算处理资源文件路径
    private String resourcePath = "/dataform";
    private String dataFormMapperType = TYPE_JSON;

    private Map<String,String> mustTouchFields = MapKit.newHashMap();   //针对哪些显示模板一定要添加哪些字段,主要解决默认创建人，创建时间，更新人，更新时间没有配置引导致属性为空问题



    private List<String> autoIncludes = ListKit.listOf(
            "/dataform/macro/fieldset.ftl"
            , "/dataform/macro/field/field.ftl"
            , "/dataform/macro/field/text.ftl"
            , "/dataform/macro/field/radio.ftl"
            , "/dataform/macro/field/date.ftl"
            , "/dataform/macro/field/number.ftl"
            , "/dataform/macro/field/money.ftl"
            , "/dataform/macro/field/money.ftl"
            , "/dataform/macro/field/picker.ftl"
            , "/dataform/macro/field/select.ftl"
            , "/dataform/macro/field/datemonth.ftl"
            , "/dataform/macro/field/richtext.ftl"
            , "/dataform/macro/detail-info.ftl"
    );

    @PostConstruct
    public void init(){
        if(authResourcePath){
            String runDir = this.getClass().getClassLoader().getResource("").getFile();
            if(runDir.endsWith("classes/")){
                File file = new File(runDir);
                String basePath = file.getParentFile().getParentFile().getAbsolutePath();
                resourcePath = StringKit.format(resourcePathTpl,basePath);
            }
        }
    }

    public String getDevBaseDir() {
        return devBaseDir;
    }

    public void setDevBaseDir(String devBaseDir) {
        this.devBaseDir = devBaseDir;
    }

    public String getDataformDataClasspath() {
        return dataformDataClasspath;
    }

    public void setDataformDataClasspath(String dataformDataClasspath) {
        this.dataformDataClasspath = dataformDataClasspath;
    }

    public List<String> getDataformIgnoreResources() {
        return dataformIgnoreResources;
    }

    public void setDataformIgnoreResources(List<String> dataformIgnoreResources) {
        this.dataformIgnoreResources = dataformIgnoreResources;
    }

    public String getDataformTagName() {
        return dataformTagName;
    }

    public void setDataformTagName(String dataformTagName) {
        this.dataformTagName = dataformTagName;
    }

    public String getResourcePathTpl() {
        return resourcePathTpl;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public List<String> getAutoIncludes() {
        return autoIncludes;
    }

    public void setAutoIncludes(List<String> autoIncludes) {
        this.autoIncludes = autoIncludes;
    }

    public String getDataFormMapperType() {
        return dataFormMapperType;
    }

    public void setDataFormMapperType(String dataFormMapperType) {
        this.dataFormMapperType = dataFormMapperType;
    }

    public Map<String, String> getMustTouchFields() {
        return mustTouchFields;
    }

    public void setMustTouchFields(Map<String, String> mustTouchFields) {
        this.mustTouchFields = mustTouchFields;
    }
}
