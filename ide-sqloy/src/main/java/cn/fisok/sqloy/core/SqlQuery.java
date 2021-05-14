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

import cn.fisok.raw.kit.SQLKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.raw.lang.PairBond;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : SQL选择语句实体类
 */
public class SqlQuery implements Serializable,Cloneable{
    private String select;
    private List<PairBond> selectItems = new ArrayList<PairBond>();
    private String from;
    private String where;
    private String groupBy;
    private String having;
    private String orderBy;
    /**
     * 数据权限暗示
     */
    private String permitHint;

    public SqlQuery() {
    }

    public SqlQuery(String from) {
        this.from = from;
    }

    public SqlQuery(String select, String from, String where) {
        this.select = select;
        this.from = from;
        this.where = where;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public List<PairBond> getSelectItems() {
        return selectItems;
    }

    public void setSelectItems(List<PairBond> selectItems) {
        this.selectItems = selectItems;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getHaving() {
        return having;
    }

    public void setHaving(String having) {
        this.having = having;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getPermitHint() {
        return permitHint;
    }

    public void setPermitHint(String permitHint) {
        this.permitHint = permitHint;
    }

    public String buildQuerySql(){
        return buildQuerySql(false);
    }

    /**
     * 构建SQL查询语句
     * @param columnAlias 是否对字段列做别名处理
     * @return
     */
    public String buildQuerySql(boolean columnAlias){
        StringBuffer bufferSql = new StringBuffer();
        String _select = StringKit.nvl(select,"SELECT");
        ValidateKit.notBlank(from,"SQL语句错误，FROM部分无内容");

        bufferSql.append(_select).append(" ");

        if(selectItems!=null&&selectItems.size()>0){
            for(int i=0;i<selectItems.size();i++){
                PairBond<String,String> item = selectItems.get(i);
                String alias = item.getLeft();
                String column = item.getRight();
                if(StringKit.isBlank(column))continue;
                bufferSql.append(column);
                //不是虚字段的情况下，才使用别名
                if(!SQLKit.isConstColumn(column) && columnAlias && StringKit.isNotBlank(alias)){
                    bufferSql.append(" AS ").append(alias);
                }
                if(i<selectItems.size()-1){
                    bufferSql.append(",");
                }
            }
        }else{
            bufferSql.append("*");
        }

        bufferSql.append(" FROM ").append(from);

        if(StringKit.isNotBlank(where)) bufferSql.append(" WHERE ").append(where);
        if(StringKit.isNotBlank(groupBy)) bufferSql.append(" GROUP BY ").append(groupBy);
        if(StringKit.isNotBlank(having)) bufferSql.append(" HAVING ").append(having);
        if(StringKit.isNotBlank(orderBy)) bufferSql.append(" ORDER BY ").append(orderBy);

        return bufferSql.toString();
    }
}
