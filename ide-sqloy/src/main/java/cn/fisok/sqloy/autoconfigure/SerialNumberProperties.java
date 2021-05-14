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
package cn.fisok.sqloy.autoconfigure;

import cn.fisok.sqloy.serialnum.consts.CursorRecordType;
import cn.fisok.sqloy.serialnum.consts.GeneratorType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 流水号生成器记录表配置信息
 */
public class SerialNumberProperties {

    /**
     * 默认流水号生成器
     */
    private String defaultGenerator = GeneratorType.AUTO_INCREMENT;

    /**
     * 默认流水号生成器模板
     */
    private String defaultTemplate = "000000";

    /**
     * 每个不同的业务场景，指示什么字段，用什么流水号生成器
     */
    private Map<String, String> generatorMap = new HashMap<String, String>();

    /**
     * 每个不同的业务场景，使用什么样的模板来生成流水号
     */
    private Map<String, String> templateMap = new HashMap<String, String>();

    /**
     * 数据存储适配器ID
     */
    private String snCursorRecordType = CursorRecordType.DB_TABLE;

    public String getDefaultGenerator() {
        return defaultGenerator;
    }

    public void setDefaultGenerator(String defaultGenerator) {
        this.defaultGenerator = defaultGenerator;
    }

    public String getDefaultTemplate() {
        return defaultTemplate;
    }

    public void setDefaultTemplate(String defaultTemplate) {
        this.defaultTemplate = defaultTemplate;
    }

    public Map<String, String> getGeneratorMap() {
        return generatorMap;
    }

    public void setGeneratorMap(Map<String, String> generatorMap) {
        this.generatorMap = generatorMap;
    }

    public Map<String, String> getTemplateMap() {
        return templateMap;
    }

    public void setTemplateMap(Map<String, String> templateMap) {
        this.templateMap = templateMap;
    }

    public String getSnCursorRecordType() {
        return snCursorRecordType;
    }

    public void setSnCursorRecordType(String snCursorRecordType) {
        this.snCursorRecordType = snCursorRecordType;
    }
}
