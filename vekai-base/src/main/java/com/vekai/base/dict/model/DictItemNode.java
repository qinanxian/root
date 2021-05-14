package com.vekai.base.dict.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.raw.lang.TreeNode;

import java.io.Serializable;
import java.util.Stack;

/**
 * 数据字典代码表
 */
@JsonPropertyOrder({"code","name","fullName","namePinin","nameI18nCode","value","sortCode","hotspot","correlation","summary","status","children"})
public class DictItemNode extends TreeNode<DictItemNode> implements Serializable, Cloneable {
    private String code;
    private String name;
    private String nameI18nCode;
    private String namePinyin;
    private String value;
    private String sortCode;
    private int hotspot;
    private String correlation;
    private String status;
    private String summary;

    @JsonIgnore
    public void setValues(DictItemEntry itemEntry){
        BeanKit.copyProperties(itemEntry,this);
    }

    @JsonIgnore
    public DictItemEntry getDictItemEntry(){
        DictItemEntry itemEntry = new DictItemEntry();
        BeanKit.copyProperties(this,itemEntry);
        return itemEntry;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 层层向上找，最终找到得到一个完整路径
     *
     * @param delimiter delimiter
     */
    public String getFullName(String delimiter){
        ValidateKit.notNull(delimiter);

        Stack<String> stack = new Stack<String>();

        DictItemNode current = this;
        while(current!=null){
            stack.push(current.getName());
            current = current.getParent();
        }

        StringBuffer sbName = new StringBuffer();
        while(!stack.empty()){
            sbName.append(delimiter).append(stack.pop());
        }

        return sbName.substring(delimiter.length());

    }

    public String getNameI18nCode() {
        return nameI18nCode;
    }

    public void setNameI18nCode(String nameI18nCode) {
        this.nameI18nCode = nameI18nCode;
    }

    public String getNamePinyin() {
        return namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public int getHotspot() {
        return hotspot;
    }

    public void setHotspot(int hotspot) {
        this.hotspot = hotspot;
    }

    public String getCorrelation() {
        return correlation;
    }

    public void setCorrelation(String correlation) {
        this.correlation = correlation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
