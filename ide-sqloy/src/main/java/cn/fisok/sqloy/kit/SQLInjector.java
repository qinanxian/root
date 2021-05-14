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
package cn.fisok.sqloy.kit;

import cn.fisok.sqloy.core.DBType;
import cn.fisok.sqloy.parser.SqlSelectParser;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.PairBond;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : SQL注入器，用于对SQL进行一些注入调整
 */
public abstract class SQLInjector {

    /**
     * 注入排序数据
     *
     * @param sql       SQL语句
     * @param sortRules 排序规则KEY=数据库字段名，VALUE=ASC/DESC
     * @return String
     */
    public static String injectSQLOrder(DBType dialectType, String sql, Map<String, String> sortRules) {
//        SqlDialectType dialectType = MapObjectAccessor.getMapObjectQuery().getSqlDialectType();
        SqlSelectParser parser = new SqlSelectParser(sql, dialectType);
        parser.parse();

        String select = parser.getSelect();
        String from = parser.getFrom();
        String where = parser.getWhere();
        List<PairBond<String, String>> sqlOrderItems = parser.getOrderItems();
        List<PairBond<String, String>> newOrderItems = new ArrayList<>();

        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(" SELECT ").append(select);
        sqlBuffer.append(" FROM ").append(from);
        if (StringKit.isNotBlank(where)) {
            sqlBuffer.append(" WHERE ").append(where);
        }

        //规则配置的排序，放到最前面
        Iterator<String> iterator = sortRules.keySet().iterator();
        while (iterator.hasNext()) {
            String column = iterator.next();
            String r = sortRules.get(column);
            if (r.equalsIgnoreCase("ASC")) {
                r = "ASC";
            } else {
                r = "DESC";
            }
            newOrderItems.add(new PairBond<String, String>(column, r));
        }

        //把SQL中本来的排序拼上去，如果SQL中有的和传入的排序规则重复，使用传入的排序规则
        for (PairBond<String, String> pair : sqlOrderItems) {
            String column = pair.getLeft();
            if (!sortRules.containsKey(column)) {
                if("ASC".equalsIgnoreCase(pair.getRight())||"DESC".equalsIgnoreCase(pair.getRight())){
                    newOrderItems.add(pair);

                }
            }
        }


        for (int i = 0; i < newOrderItems.size(); i++) {
            PairBond<String, String> pair = newOrderItems.get(i);
            if (i == 0) {
                sqlBuffer.append(" ORDER BY ");
                sqlBuffer.append(pair.getLeft()).append(" ").append(pair.getRight());
            } else {
                sqlBuffer.append(",").append(pair.getLeft()).append(" ").append(pair.getRight());
            }
        }

        return sqlBuffer.toString();
    }
}
