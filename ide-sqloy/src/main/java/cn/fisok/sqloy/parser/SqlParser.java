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
package cn.fisok.sqloy.parser;


import cn.fisok.sqloy.core.DBType;
import com.alibaba.druid.sql.dialect.db2.visitor.DB2SchemaStatVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerSchemaStatVisitor;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : SQL语法解析器
 * 主要依托druid进行语法解析，同时，为了屏蔽应用层对druid的直接依赖，统一放到这个类中来处理，以降低应用对某个特定技术的直接依赖
 */
public class SqlParser {
    static Map<DBType,String> dbTypeMap = new HashMap<DBType,String>();
    static Map<DBType,SchemaStatVisitor> visitorMap = new HashMap<DBType,SchemaStatVisitor>();
    static{
        initMap(DBType.ORACLE,"oracle",new OracleSchemaStatVisitor());
        initMap(DBType.MYSQL,"mysql",new MySqlSchemaStatVisitor());
        initMap(DBType.MARIADB,"mariadb",new MySqlSchemaStatVisitor());
        initMap(DBType.DB2,"db2",new DB2SchemaStatVisitor());
        initMap(DBType.SQL_SERVER,"sqlserver",new SQLServerSchemaStatVisitor());

        initMap(DBType.SQLITE,"sqlite",new SchemaStatVisitor());
        initMap(DBType.H2,"h2",new SchemaStatVisitor());
        initMap(DBType.DERBY,"derby",new SchemaStatVisitor());
        initMap(DBType.POSTGRESQL,"postgresql",new SchemaStatVisitor());
        initMap(DBType.HSQL,"hsql",new SchemaStatVisitor());
        initMap(DBType.HBASE,"hbase",new SchemaStatVisitor());

    }

    private static void initMap(DBType sqlDialectType, String db, SchemaStatVisitor visitor){
        dbTypeMap.put(sqlDialectType,db);
        visitorMap.put(sqlDialectType,visitor);
    }

    protected String sql;
    protected DBType sqlDialectType;
    protected boolean prettyFormat = false;
    protected boolean upperCase = false;

    public boolean isPrettyFormat() {
        return prettyFormat;
    }

    public void setPrettyFormat(boolean prettyFormat) {
        this.prettyFormat = prettyFormat;
    }

    public boolean isUpperCase() {
        return upperCase;
    }

    public void setUpperCase(boolean upperCase) {
        this.upperCase = upperCase;
    }

    protected String getDbType(DBType dialectType){
        String dbType = dbTypeMap.get(dialectType);
        if(dbType==null){
            throw new UnsupportedOperationException(dialectType.name()+" database type is not supported");
        }
        return dbType;
    }
}
