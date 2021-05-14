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
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.ValidateKit;

import java.util.List;
import java.util.Set;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : ORACLE处理方言
 */
public class DB2Dialect extends AbstractSqlDialect {

    public String getCountSql(String sql) {
        SqlSelectParser parser = new SqlSelectParser(sql, DBType.MYSQL);
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

        SqlSelectParser parser = new SqlSelectParser(sql, DBType.DB2);
        parser.setSql(sql);
        parser.parse();

        StringBuffer sqlBuffer = new StringBuffer();
        if(index == 0){ //首页不需要起始行索引号
            sbSql.append(" fetch first ").append(size).append(" rows only");
        }else{
            List<String> columnList = parser.getColumns();
            Set<String> fromAliaSet = parser.getFromTableAlias();
            buildQuerySql(parser,sqlBuffer,columnList,fromAliaSet,index,size);
            return sqlBuffer.toString();
        }

        return sbSql.toString();
    }

    private void buildQuerySql(SqlSelectParser parser,StringBuffer buffer,List<String> columnList,Set<String> fromAliaSet,int index,int size){
        String from = parser.getFrom();
        String where = parser.getWhere();
        String order = parser.getOrder();

        StringBuffer columnsBuffer = buildSelectColumnClause(columnList,fromAliaSet);
        if(columnsBuffer.charAt(0)==','){
            columnsBuffer.deleteCharAt(0);
        }

        buffer.append("SELECT * FROM (SELECT rownumber() over() AS ROW_NO ,STV.*") ;//rownumber() over()从1开始
        buffer.append(" FROM (");
        buffer.append("SELECT ");
        buffer.append(columnsBuffer);
        buffer.append(" FROM ").append(from);
        if(StringKit.isNotBlank(where)){
            buffer.append(" WHERE ").append(where);
        }
        if(StringKit.isNotBlank(order)){
            buffer.append(" ").append(order);
        }
        buffer.append(" ) STV)");

        buffer.append(" WHERE");
        buffer.append(" ROW_NO BETWEEN ").append(index*size+1).append(" AND ").append((index+1)*size);
    }
}
