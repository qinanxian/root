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
package cn.fisok.sqloy.dialect.impl;

import cn.fisok.sqloy.core.DBType;
import cn.fisok.sqloy.parser.SqlSelectParser;
import cn.fisok.raw.kit.ValidateKit;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : PostgreSQL处理方言
 */
public class PostgreSQLDialect extends AbstractSqlDialect {

    public String getCountSql(String sql) {
        SqlSelectParser parser = new SqlSelectParser(sql, DBType.POSTGRESQL);
        parser.setSql(sql);
        parser.parse();

        return buildCountAllSql(parser);
    }

    public String getPaginationSql(String sql,int index,int size) {
        if(size <= 0) return sql;

        //select * from DEMO_PERSON where CODE > :code LIMIT ${起始行索引号},${每页记录数}
        StringBuffer sbSql = new StringBuffer(sql);
        //起始行号，从1开始，但是不包含当前行
        ValidateKit.isTrue(index>-1,"pagination index must be greater than -1,index={0}",index);
        sbSql.append(" LIMIT ");
        if(index == 0){ //首页不需要起始行索引号
            sbSql.append(size);
        }else{
            int rowIndex = index*size;
//            sbSql.append(rowIndex).append(",").append(size);
            sbSql.append(size).append(" offset ").append(index);
        }

        return sbSql.toString();
    }
}
