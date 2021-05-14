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

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : fisok-jdbc模块自动装配配置信息
 */
@ConfigurationProperties(prefix = "cn.fisok.sqloy", ignoreUnknownFields = true)
public class SqloyProperties {
    private SerialNumberProperties serialNumber = new SerialNumberProperties();

//    private DBType sqlDialectType = DBType.MYSQL;

    private Map<String,Object> datasource = new LinkedHashMap<String,Object>();
    private String druidLoginUser;
    private String druidLoginPass;
    private String druidResetEnable;
    private long snowFlakeDatacenterId = 1L;

//    /**
//     * 每个不同的业务场景，使用什么样的类来生成流水号
//     */
//    public DBType getSqlDialectType() {
//        return sqlDialectType;
//    }
//
//    public void setSqlDialectType(DBType sqlDialectType) {
//        this.sqlDialectType = sqlDialectType;
//    }

    public SerialNumberProperties getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(SerialNumberProperties serialNumberProperties) {
        this.serialNumber = serialNumberProperties;
    }

    public Map<String, Object> getDatasource() {
        return datasource;
    }

    public void setDatasource(Map<String, Object> datasource) {
        this.datasource = datasource;
    }

    public String getDruidLoginUser() {
        return druidLoginUser;
    }

    public void setDruidLoginUser(String druidLoginUser) {
        this.druidLoginUser = druidLoginUser;
    }

    public String getDruidLoginPass() {
        return druidLoginPass;
    }

    public void setDruidLoginPass(String druidLoginPass) {
        this.druidLoginPass = druidLoginPass;
    }

    public String getDruidResetEnable() {
        return druidResetEnable;
    }

    public void setDruidResetEnable(String druidResetEnable) {
        this.druidResetEnable = druidResetEnable;
    }

    public long getSnowFlakeDatacenterId() {
        return snowFlakeDatacenterId;
    }

    public void setSnowFlakeDatacenterId(long snowFlakeDatacenterId) {
        this.snowFlakeDatacenterId = snowFlakeDatacenterId;
    }
}
