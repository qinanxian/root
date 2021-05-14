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

import cn.fisok.sqloy.dialect.SqlDialect;
import cn.fisok.sqloy.parser.SqlSelectParser;
import cn.fisok.raw.kit.StringKit;

import java.util.List;
import java.util.Set;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 抽象公共部分
 */
public abstract class AbstractSqlDialect implements SqlDialect {
    public String buildCountAllSql(SqlSelectParser parser) {

        String whereClause = parser.getWhere();

        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT COUNT(1)");
        buffer.append(" FROM ").append(parser.getFrom());
        if (StringKit.isNotBlank(whereClause)) {
            buffer.append(" WHERE ");
            buffer.append(whereClause);
        }
        return buffer.toString();
    }

    /**
     * 构建select后面的字段列表子句
     * @param columnList select 选择的字段列表
     * @param fromAliaSet from表的别名
     * @return
     */
    protected StringBuffer buildSelectColumnClause(List<String> columnList, Set<String> fromAliaSet){
        StringBuffer buffer = new StringBuffer();
        for(String colum : columnList){
            buffer.append(",");
            //如果是 "*" 则需要对每一个查询的子表增加*
            String cleanColumn = colum.replaceAll("\\s*","");
            if("*".equals(cleanColumn)){
                for(String t : fromAliaSet){
                    buffer.append(t).append(".*");
                }
            }else{
                buffer.append(colum);
            }
        }
        return buffer;
    }
}
