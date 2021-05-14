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

import cn.fisok.sqloy.dialect.*;
import cn.fisok.sqloy.dialect.impl.DB2Dialect;
import cn.fisok.sqloy.dialect.impl.MySqlDialect;
import cn.fisok.sqloy.dialect.impl.OracleDialect;
import cn.fisok.sqloy.dialect.impl.PostgreSQLDialect;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : Jdbc处理模块常量
 */
public abstract class SqlConsts {
    public static final String DS_MASTER = "master";
    public static Map<DBType, SqlDialect> DIALECT_MAP = new HashMap<DBType, SqlDialect>();
    static {
        DIALECT_MAP.put(DBType.MYSQL,new MySqlDialect());
        DIALECT_MAP.put(DBType.ORACLE,new OracleDialect());
        DIALECT_MAP.put(DBType.POSTGRESQL,new PostgreSQLDialect());
        DIALECT_MAP.put(DBType.DB2,new DB2Dialect());
    }
}
