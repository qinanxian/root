package com.vekai.auth.audit.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="AUTH_DATA_AUDIT_ITEM")
public class DataAuditItemEntity implements Serializable,Cloneable {
    /** 数据项唯一编号 */
    @Id
    @GeneratedValue
    private String auditItemId ;
    /** 审计ID */
    private String dataAuditId ;
    /** 属性代码 */
    private String itemCode ;
    /** 属性名称 */
    private String itemName ;
    /** 字段代码 */
    private String columnName ;
    /** 数据类型 */
    private String dataType ;
    /** 是否数据字典 */
    private String isDict ;
    /** 修改前描述值;如果有数据字典的，则转换数据字典值 */
    private String valueExplain ;
    /** 修改后描述值;如果有数据字典的，则转换数据字典值 */
    private String valueExplainOld ;
    /** 修改前字串值 */
    private String valueStr ;
    /** 修改后字串值 */
    private String valueStrOld ;
    /** 修改前数字值 */
    private Double valueNumber ;
    /** 修改后数字值 */
    private Double valueNumberOld ;
    /** 修改前日期值 */
    private Date valueDate ;
    /** 修改后日期值 */
    private Date valueDateOld ;

    /** 数据项唯一编号 */
    public String getAuditItemId(){
        return this.auditItemId;
    }
    /** 数据项唯一编号 */
    public void setAuditItemId(String auditItemId){
        this.auditItemId = auditItemId;
    }
    /** 审计ID */
    public String getDataAuditId(){
        return this.dataAuditId;
    }
    /** 审计ID */
    public void setDataAuditId(String dataAuditId){
        this.dataAuditId = dataAuditId;
    }
    /** 属性代码 */
    public String getItemCode(){
        return this.itemCode;
    }
    /** 属性代码 */
    public void setItemCode(String itemCode){
        this.itemCode = itemCode;
    }
    /** 属性名称 */
    public String getItemName(){
        return this.itemName;
    }
    /** 属性名称 */
    public void setItemName(String itemName){
        this.itemName = itemName;
    }
    /** 字段代码 */
    public String getColumnName(){
        return this.columnName;
    }
    /** 字段代码 */
    public void setColumnName(String columnName){
        this.columnName = columnName;
    }
    /** 数据类型 */
    public String getDataType(){
        return this.dataType;
    }
    /** 数据类型 */
    public void setDataType(String dataType){
        this.dataType = dataType;
    }
    /** 是否数据字典 */
    public String getIsDict(){
        return this.isDict;
    }
    /** 是否数据字典 */
    public void setIsDict(String isDict){
        this.isDict = isDict;
    }
    /** 修改前描述值;如果有数据字典的，则转换数据字典值 */
    public String getValueExplain(){
        return this.valueExplain;
    }
    /** 修改前描述值;如果有数据字典的，则转换数据字典值 */
    public void setValueExplain(String valueExplain){
        this.valueExplain = valueExplain;
    }
    /** 修改后描述值;如果有数据字典的，则转换数据字典值 */
    public String getValueExplainOld(){
        return this.valueExplainOld;
    }
    /** 修改后描述值;如果有数据字典的，则转换数据字典值 */
    public void setValueExplainOld(String valueExplainOld){
        this.valueExplainOld = valueExplainOld;
    }
    /** 修改前字串值 */
    public String getValueStr(){
        return this.valueStr;
    }
    /** 修改前字串值 */
    public void setValueStr(String valueStr){
        this.valueStr = valueStr;
    }
    /** 修改后字串值 */
    public String getValueStrOld(){
        return this.valueStrOld;
    }
    /** 修改后字串值 */
    public void setValueStrOld(String valueStrOld){
        this.valueStrOld = valueStrOld;
    }
    /** 修改前数字值 */
    public Double getValueNumber(){
        return this.valueNumber;
    }
    /** 修改前数字值 */
    public void setValueNumber(Double valueNumber){
        this.valueNumber = valueNumber;
    }
    /** 修改后数字值 */
    public Double getValueNumberOld(){
        return this.valueNumberOld;
    }
    /** 修改后数字值 */
    public void setValueNumberOld(Double valueNumberOld){
        this.valueNumberOld = valueNumberOld;
    }
    /** 修改前日期值 */
    public Date getValueDate(){
        return this.valueDate;
    }
    /** 修改前日期值 */
    public void setValueDate(Date valueDate){
        this.valueDate = valueDate;
    }
    /** 修改后日期值 */
    public Date getValueDateOld(){
        return this.valueDateOld;
    }
    /** 修改后日期值 */
    public void setValueDateOld(Date valueDateOld){
        this.valueDateOld = valueDateOld;
    }
}