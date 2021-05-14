package com.vekai.dataform.model;

import com.vekai.base.dict.model.DictItemNode;

import java.util.List;
import java.util.Map;

/**
 * 把显示模板元数据和数据组合到一起
 * @param <T>
 */
public class DataFormCombiner<T> {
    private DataForm meta;
    private Map<String,List<DictItemNode>> dict;
    private T body;

    public DataFormCombiner() {
    }

    public DataForm getMeta() {
        return meta;
    }

    public void setMeta(DataForm meta) {
        this.meta = meta;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public Map<String, List<DictItemNode>> getDict() {
        return dict;
    }

    public void setDict(Map<String, List<DictItemNode>> dict) {
        this.dict = dict;
    }
}
