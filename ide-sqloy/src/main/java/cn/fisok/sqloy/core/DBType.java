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
package cn.fisok.sqloy.core;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 数据库类型集合
 */
public enum DBType {
    JTDS("odps", ""),
    HSQL("hsql", ""),
    DB2("db2", ""),
    POSTGRESQL("postgresql", "PostgreSQL"),
    SYBASE("sybase", ""),
    SQL_SERVER("sqlserver", ""),
    ORACLE("oracle", ""),
    ALI_ORACLE("AliOracle", ""),
    MYSQL("mysql", ""),
    MARIADB("mariadb", ""),
    DERBY("derby", ""),
    HBASE("hbase", ""),
    HIVE("hive", ""),
    H2("h2", ""),
    DM("dm", "达梦数据库"),
    KINGBASE("kingbase", ""),
    GBASE("gbase", ""),
    XUGU("xugu", ""),
    OCEANBASE("oceanbase", ""),
    INFORMIX("informix", ""),
    ODPS("odps", "阿里云odps"),
    TERADATA("teradata", ""),
    LOG4JDBC("log4jdbc", "Log4JDBC"),
    PHOENIX("phoenix", ""),
    ENTERPRISEDB("edb", ""),
    KYLIN("kylin", ""),
    SQLITE("sqlite", ""),
    ALIYUN_ADS("aliyun_ads", ""),
    ALIYUN_DRDS("aliyun_drds", ""),
    PRESTO("presto", ""),
    ELASTIC_SEARCH("elastic_search", ""),
    CLICKHOUSE("clickhouse", "");

    private String name;
    private String comment;

    DBType(String name) {
        this.name = name;
    }

    DBType(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

}
