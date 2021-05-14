/*
 * Copyright 2019-2029 FISOK(www.fisok.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.fisok.raw.autoconfigure;

import cn.fisok.raw.cache.CacheProperties;
import cn.fisok.raw.cache.enums.CacheDialect;
import cn.fisok.raw.kit.IOKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/01/04
 * @desc :
 */
@ConfigurationProperties(prefix = "cn.fisok.raw", ignoreUnknownFields = true)
public class RawProperties {
    public static enum RunModel {
        DEV,PROD
    }
    private Charset charset = Charset.defaultCharset();
    /** 运行模式：开发|生产 */
    private RunModel runModel = RunModel.PROD;
    private CacheProperties cache = new CacheProperties();

    private String longDateFormat = "yyyy-MM-dd HH:mm:ss.SSS";
    private String shorDateFormat = "yyyy-MM-dd";
    /**
     * JSON序列化，保密字段（向前端输出后，显示为 ${secret-mask})
     *json-serialize-property-secrets:
     *  - com.vekai.auth.entity.User.password
     *  - com.vekai.auth.entity.User.status
     */
    private List<String> jsonSerializePropertySecrets = new ArrayList<String>();
    /** JSON序列化，保密字段统一使显示文字 */
    private String jsonSerializeSecretMask = "******";
    /**
     * JSON序列化，需要排除的字段（向前端输出后，这些字段不输出)
     * json-serialize-property-excludes:
     *  - com.vekai.auth.entity.User.revision
     *  - com.vekai.auth.entity.User.orgId
     */
    private List<String> jsonSerializePropertyExcludes = new ArrayList<String>();
    private String scriptEngineName = "groovy";
    private String globalScript = "";

    private Logger logger = LoggerFactory.getLogger(getClass());

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public RunModel getRunModel() {
        return runModel;
    }

    public void setRunModel(RunModel runModel) {
        this.runModel = runModel;
    }

    public CacheProperties getCache() {
        return cache;
    }

    public void setCache(CacheProperties cache) {
        this.cache = cache;
    }

    public String getLongDateFormat() {
        return longDateFormat;
    }

    public void setLongDateFormat(String longDateFormat) {
        this.longDateFormat = longDateFormat;
    }

    public String getShorDateFormat() {
        return shorDateFormat;
    }

    public void setShorDateFormat(String shorDateFormat) {
        this.shorDateFormat = shorDateFormat;
    }

    public List<String> getJsonSerializePropertySecrets() {
        return jsonSerializePropertySecrets;
    }

    public void setJsonSerializePropertySecrets(List<String> jsonSerializePropertySecrets) {
        this.jsonSerializePropertySecrets = jsonSerializePropertySecrets;
    }

    public String getJsonSerializeSecretMask() {
        return jsonSerializeSecretMask;
    }

    public void setJsonSerializeSecretMask(String jsonSerializeSecretMask) {
        this.jsonSerializeSecretMask = jsonSerializeSecretMask;
    }

    public List<String> getJsonSerializePropertyExcludes() {
        return jsonSerializePropertyExcludes;
    }

    public void setJsonSerializePropertyExcludes(List<String> jsonSerializePropertyExcludes) {
        this.jsonSerializePropertyExcludes = jsonSerializePropertyExcludes;
    }

    public String getScriptEngineName() {
        return scriptEngineName;
    }

    public void setScriptEngineName(String scriptEngineName) {
        this.scriptEngineName = scriptEngineName;
    }

    public String getGlobalScript() {
        return globalScript;
    }

    public void setGlobalScript(String globalScript) {
        this.globalScript = globalScript;
    }

    @PostConstruct
    public void init(){
        String res = "classpath:GlobalScript.txt";
        URL url = null;
        InputStream inputStream = null;
        try {
            url = ResourceUtils.getURL(res);
            inputStream = url.openStream();
            globalScript = IOKit.toString(inputStream, Charset.defaultCharset());
        } catch (FileNotFoundException e) {
            logger.warn("全局资源脚本不存在",e);
        } catch (IOException e) {
            logger.warn("读取全局资源脚本出错",e);
        } finally {
            IOKit.close(inputStream);
        }
    }
}
