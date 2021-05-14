package com.vekai.dataform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vekai.dataform.model.types.FormDataModelType;
import com.vekai.dataform.model.types.FormStyle;
import cn.fisok.raw.kit.ListKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.sqloy.core.SqlQuery;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.*;

/**
 * DataForm模型定义,定义一个SQL查询对应的业务元素以及UI展示方式指示<br/>
 * 参考于PowerBuilder的DataWindow设计思想
 * Created by tisir yangsong158@qq.com on 2017-05-20
 */
@Table(name = "FOWK_DATAFORM")
public class DataForm implements Serializable,Cloneable{
    /** 全局唯一编号 id = ${pack}.${code} */
    @Id
    private String id;
    /** 命名空间/包 */
    protected String pack;
    /** 标识代码 */
    protected String code;
    /** 名称 */
    protected String name;
    /** 描述说明 */
    protected String description;
    /** 调用权限 */
    protected String invokePermit;
    /** 排序代码 */
    protected String sortCode;
    /** 分类 */
    protected String classification;
    /** 标签,多个标签使用逗号分割 */
    protected String tags;
    /** 数据模型类别 */
    protected FormDataModelType dataModelType = FormDataModelType.DataMap;
    /** 更新的数据表,多个数据表使用逗号分割 */
    protected String dataModel;
    /** 执行的SQL查询语句 */
    protected SqlQuery query;
    /** 统一处理器(数据) */
    protected String handler;
    /** 指导UI界面生成 */
    protected DataFormUIHint formUIHint = new DataFormUIHint();
    /** 业务要素集合 */
    protected List<DataFormElement> elements = new ArrayList<DataFormElement>();
    /** 过滤器集合 */
    protected List<DataFormFilter> filters = new ArrayList<DataFormFilter>();
    /** 扩展属性 */
    protected Map<String,Object> properties = new LinkedHashMap<String,Object>();
    /** 版本号,每修改一次,加一 */
    protected Integer revision = 0;
    /** 创建人 */
    protected String createdBy;
    /** 创建时间 */
    protected Date createdTime;
    /** 修改人 */
    protected String updatedBy;
    /** 修改时间 */
    protected Date updatedTime;

    //以下两个变量不需要作持久化
    protected transient Map<String,DataFormElement> elementMap = MapKit.newHashMap();
    protected transient Map<String,DataFormFilter> filterMap = MapKit.newHashMap();


    public String getId() {
        id = code;
        if(StringKit.isNotBlank(pack)){
            id = StringKit.format("{0}-{1}",pack,code);
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
        int pos = id.lastIndexOf("-");
        if(pos < 0){
            this.code = id;
        }else{
            this.pack = id.substring(0,pos);
            this.code = id.substring(pos + 1);
        }
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInvokePermit() {
        return invokePermit;
    }

    public void setInvokePermit(String invokePermit) {
        this.invokePermit = invokePermit;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public FormDataModelType getDataModelType() {
        return dataModelType;
    }

    public void setDataModelType(FormDataModelType dataModelType) {
        this.dataModelType = dataModelType;
    }

    public String getDataModel() {
        return dataModel;
    }

    public void setDataModel(String dataModel) {
        this.dataModel = dataModel;
    }

    public SqlQuery getQuery() {
        return query;
    }

    public DataForm setQuery(SqlQuery query) {
        this.query = query;
        return this;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    @JsonIgnore
    public List<DataFormElement> getPrimaryKeyElements(){
        List<DataFormElement> elementList = ListKit.newArrayList();

        for(DataFormElement  element : elements){
            if(element.getPrimaryKey()){
                elementList.add(element);
            }
        }

        return elementList;
    }

    public List<DataFormElement> getElements() {
        return elements;
    }

    public void refreshElementMap(){
        setElements(this.getElements());
    }

    public void setElements(List<DataFormElement> elements) {
        if(elementMap == null){
            elementMap = MapKit.newHashMap();
        }
        for(int i=0;i<elements.size();i++){
            this.elementMap.put(elements.get(i).getCode(),elements.get(i));
        }
        this.elements = elements;
    }

    public DataForm addElement(DataFormElement element){
        this.elements.add(element);
        this.elementMap.put(element.getCode(),element);
        return this;
    }
    public DataForm removeElement(String code){
        DataFormElement element = this.elementMap.get(code);
        if(element!=null)this.elements.remove(element);
        return this;
    }
    public DataForm removeElement(DataFormElement element){
        this.elementMap.remove(element.getCode());
        this.elements.remove(element);
        return this;
    }
    public DataFormElement getElement(String elementCode){
        if(elementMap==null)elementMap = MapKit.newHashMap();
        for(DataFormElement element : elements){
            elementMap.put(element.getCode(),element);
        }
        return elementMap.get(elementCode);
    }

    public List<DataFormFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<DataFormFilter> filters) {
        for(DataFormFilter filter : filters){
            filterMap.put(filter.getCode(),filter);
        }
        this.filters = filters;
    }

    public DataForm addFilter(DataFormFilter filter){
        this.filters.add(filter);
        this.filterMap.put(filter.getCode(),filter);
        return this;
    }
    public DataFormFilter getFilter(String filterCode){
        if(filterMap==null)filterMap = MapKit.newHashMap();
        for(DataFormFilter filter : filters){
            this.filterMap.put(filter.getCode(),filter);
        }
        return filterMap.get(filterCode);
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public DataFormUIHint getFormUIHint() {
        return formUIHint;
    }

    public void setFormUIHint(DataFormUIHint formUIHint) {
        this.formUIHint = formUIHint;
    }

    /**
     * UI展示方式指示
     */
    public static class DataFormUIHint  implements Serializable,Cloneable{
        /** 一般用在DetailInfo模式下,一个页面显示成为多少列 */
        protected int columnNumber = 1;
        /** 指导页面的显示方式 */
        protected FormStyle formStyle = FormStyle.DataTable;

        public int getColumnNumber() {
            return columnNumber;
        }

        public void setColumnNumber(int columnNumber) {
            this.columnNumber = columnNumber;
        }

        public FormStyle getFormStyle() {
            return formStyle;
        }

        public void setFormStyle(FormStyle formStyle) {
            this.formStyle = formStyle;
        }
    }

}
