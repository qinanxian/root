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

import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.lang.PairBond;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 分页查询对象
 */
public class PaginQuery {
    /** 每页大小,-1表示不分页 */
    protected int size = -1;
    /** 当前页索引(从0开始) */
    protected int index = 0;
    /** 查询语句 */
    protected String query;
    /** 查询语句对应的参数 */
    protected Map<String,Object> parameterMap = MapKit.newEmptyMap();
    /** 是否做当前页的概括统计(小计) */
    protected boolean summary = true;
    /** 是否所有数据的概括统计(合计) */
    protected boolean wholeSummary = true;
    /** 字段的概括统计表达式,Key为字段名,Value为统计表达式,如sum(${COLUMN}) */
    protected Map<PairBond<String,String>,String> summarizesExpressions = new LinkedHashMap<PairBond<String,String>,String>();

    public PaginQuery() {
    }

    public PaginQuery(String query) {
        this.query = query;
    }

    public PaginQuery(String query, Map<String, Object> parameterMap) {
        this.query = query;
        this.parameterMap = parameterMap;
    }

    public PaginQuery(String query, Map<String, Object> parameterMap, int index, int size ) {
        this.query = query;
        this.parameterMap = parameterMap;
        this.index = index;
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public PaginQuery setSize(int size) {
        this.size = size;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public PaginQuery setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getQuery() {
        return query;
    }

    public PaginQuery setQuery(String query) {
        this.query = query;
        return this;
    }

    public boolean isSummary() {
        return summary;
    }

    public PaginQuery setSummary(boolean summary) {
        this.summary = summary;
        return this;
    }

    public boolean isWholeSummary() {
        return wholeSummary;
    }

    public PaginQuery setWholeSummary(boolean wholeSummary) {
        this.wholeSummary = wholeSummary;
        return this;
    }

    public Map<PairBond<String,String>, String> getSummarizesExpressions() {
        return summarizesExpressions;
    }

    public PaginQuery setSummarizesExpressions(Map<PairBond<String,String>, String> summarizesExpressions) {
        this.summarizesExpressions = summarizesExpressions;
        return this;
    }

    public PaginQuery addSummaryExpression(PairBond<String,String> name, String expression){
        this.summarizesExpressions.put(name,expression);
        return this;
    }
    public PaginQuery addParameter(String name, Object value){
        parameterMap.put(name,value);
        return this;
    }
    public Object removeParameter(String name,Object value){
        return parameterMap.remove(name);
    }

    public Map<String, Object> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, Object> parameterMap) {
        this.parameterMap = parameterMap;
    }
}
