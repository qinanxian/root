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
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import cn.fisok.raw.lang.PairBond;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : INSERT语句解析，只解析简单的插入语句，复杂的暂时不支持
 */
public class SqlInsertParser extends SqlParser {
    private String table;
    private List<PairBond<String,String>> fields = new ArrayList<PairBond<String,String>>();

    public SqlInsertParser(){
    }

    public SqlInsertParser(String sql, DBType sqlDialectType) {
        this.sql = sql;
        this.sqlDialectType = sqlDialectType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }


    public String getTable() {
        return table;
    }

    public List<PairBond<String, String>> getFields() {
        return fields;
    }

    public SqlInsertParser parse(){
        String dbType = getDbType(sqlDialectType);

        SQLStatementParser stmtParser = new SQLStatementParser(sql,dbType);
        SQLStatement stmt = stmtParser.parseStatement();
        SQLInsertStatement insertStmt = (SQLInsertStatement)stmt;

        List<SQLExpr> columns = insertStmt.getColumns();
//        List<SQLInsertStatement.ValuesClause> values = insertStmt.getValuesList();
        this.table = insertStmt.getTableName().getSimpleName();

        SQLInsertStatement.ValuesClause valuesClause = insertStmt.getValues();
        List<SQLExpr> valuesExpr = valuesClause.getValues();

        for(int i=0;i<columns.size();i++){
            PairBond<String,String> item = new PairBond<String,String>();
            item.setLeft(columns.get(i).toString());
            item.setRight(valuesExpr.get(i).toString());
            fields.add(item);
        }

        return this;
    }
}
