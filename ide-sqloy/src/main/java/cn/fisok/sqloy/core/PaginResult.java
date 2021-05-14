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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 分页数据模型,支持小计，合计等摘要信息的处理
 */
public class PaginResult<T> implements Serializable,Cloneable{
    private static final long serialVersionUID = -3587059890000403840L;

    /** 每页大小 */
    protected int size;
    /** 总记页数 */
    protected int pageCount;
    /** 当前页索引(从0开始) */
    protected int index;
    /** 分页列表数据 */
    protected List<T> dataList;

    /** 当前页记录条数 */
    protected int rowCount;
    /** 当前页数据的(不分页情况下的概要数据,如:当前页小计等) */
    protected Map<String,Object> summarizes;
    /** 全部数据记录数总*/
    protected int totalRowCount;
    /** 全部数据(不分页情况下的概要数据,如:合计取平均数,最大,最小等) */
    protected Map<String,Object> totalSummarizes;

    public PaginResult() {
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
        this.rowCount = dataList.size();
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public Map<String, Object> getSummarizes() {
        return summarizes;
    }

    public void setSummarizes(Map<String, Object> summarizes) {
        this.summarizes = summarizes;
    }

    public int getTotalRowCount() {
        return totalRowCount;
    }

    public void setTotalRowCount(int totalRowCount) {
        this.totalRowCount = totalRowCount;
    }

    public Map<String, Object> getTotalSummarizes() {
        return totalSummarizes;
    }

    public void setTotalSummarizes(Map<String, Object> totalSummarizes) {
        this.totalSummarizes = totalSummarizes;
    }

}
