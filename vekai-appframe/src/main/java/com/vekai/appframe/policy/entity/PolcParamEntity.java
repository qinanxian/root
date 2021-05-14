package com.vekai.appframe.policy.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="POLC_PARAM")
public class PolcParamEntity implements Serializable,Cloneable{


    public PolcParamEntity(){
    }

    public PolcParamEntity(String paramCode,String paramName){
        this.paramCode = paramCode;
        this.paramName = paramName;
    }

    public PolcParamEntity(String paramCode,String paramName,String groupName){
        this.paramCode = paramCode;
        this.paramName = paramName;
        this.groupName = groupName;

    }

    public PolcParamEntity(String paramCode,String paramName,String groupName,String editorMode){
        this.paramCode = paramCode;
        this.paramName = paramName;
        this.groupName = groupName;
        this.editorMode = editorMode;
    }
    /** 参数ID */
    @Id
    @GeneratedValue
    private String paramId ;
    /** 产品ID */
    private String policyId ;
    /** 参数代码 */
    private String paramCode ;
    /** 排序值 */
    private Integer sortValue ;
    /** 参数名 */
    private String paramName ;
    /** 参数说明 */
    private String paramDesc ;
    /** 参数所在分组 */
    private String groupName ;
    /** 数据类型;String,Double,Integer,Date */
    private String dataType ;
    /** 参数值控制模式;Single单值，Multi多值，Range数字范围 */
    private String dataValueMode ;
    /** 是否为公式;代码：Y/N */
    private String isExpression ;
    /** 参数值 */
    private String valueExpr ;
    /** 参数值-最小值 */
    private String valueMinExpr ;
    /** 参数值-最小值 */
    private String valueMaxExpr ;
    /** 默认值 */
    private String defaultValue ;
    /** 输入输出类型;In:输入，Out:输出。输入参数：根据传入业务对象，获取值，输出参数：计算后，最后输出到业务对象上。 */
    private String inOut ;
    /** 配置界面编辑方式;Text:文本框,Select:下拉框,RadioBox:单选框,CheckBox:多选框,TextArea:多行文本框,Label:无编辑,ReferComponent:引用组件 */
    private String editorMode ;
    /** 界面选项来源方式;Dict:数据字典,SQL:SQL语句,CodeTable:代码表 */
    private String editorSourceMode ;
    /** 配置界面选项数据源 */
    private String editorSourceData ;
    /** 所占行数 */
    private Integer spanRows ;
    /** 所占列数 */
    private Integer spanCols ;
    /** 标题内容布局;Horizontal左右结构，Vertical上下结构 */
    private String layoutMode ;
    /** 引用组件URL;引用其他组件时用 */
    private String referUrl ;
    /** 引用组件关键字;用于标识引用组件内部的参数关键字 */
    private String referKey ;
    /** 应用层编辑模式;Label:标签显示，Readonly:只读，Editable:可编辑,Hidden:隐藏
     , */
    private String applyEditorMode ;
    /** 乐观锁 */
    private Integer revision ;
    /** 创建人 */
    private String createdBy ;
    /** 创建时间 */
    private Date createdTime ;
    /** 更新人 */
    private String updatedBy ;
    /** 更新时间 */
    private Date updatedTime ;

    /** 参数ID */
    public String getParamId(){
        return this.paramId;
    }
    /** 参数ID */
    public void setParamId(String paramId){
        this.paramId = paramId;
    }
    /** 产品ID */
    public String getPolicyId(){
        return this.policyId;
    }
    /** 产品ID */
    public void setPolicyId(String policyId){
        this.policyId = policyId;
    }
    /** 参数代码 */
    public String getParamCode(){
        return this.paramCode;
    }
    /** 参数代码 */
    public void setParamCode(String paramCode){
        this.paramCode = paramCode;
    }
    /** 排序值 */
    public Integer getSortValue(){
        return this.sortValue;
    }
    /** 排序值 */
    public void setSortValue(Integer sortValue){
        this.sortValue = sortValue;
    }
    /** 参数名 */
    public String getParamName(){
        return this.paramName;
    }
    /** 参数名 */
    public void setParamName(String paramName){
        this.paramName = paramName;
    }
    /** 参数说明 */
    public String getParamDesc(){
        return this.paramDesc;
    }
    /** 参数说明 */
    public void setParamDesc(String paramDesc){
        this.paramDesc = paramDesc;
    }
    /** 参数所在分组 */
    public String getGroupName(){
        return this.groupName;
    }
    /** 参数所在分组 */
    public void setGroupName(String groupName){
        this.groupName = groupName;
    }
    /** 数据类型;String,Double,Integer,Date */
    public String getDataType(){
        return this.dataType;
    }
    /** 数据类型;String,Double,Integer,Date */
    public void setDataType(String dataType){
        this.dataType = dataType;
    }
    /** 参数值控制模式;Single单值，Multi多值，Range数字范围 */
    public String getDataValueMode(){
        return this.dataValueMode;
    }
    /** 参数值控制模式;Single单值，Multi多值，Range数字范围 */
    public void setDataValueMode(String dataValueMode){
        this.dataValueMode = dataValueMode;
    }
    /** 是否为公式;代码：Y/N */
    public String getIsExpression(){
        return this.isExpression;
    }
    /** 是否为公式;代码：Y/N */
    public void setIsExpression(String isExpression){
        this.isExpression = isExpression;
    }
    /** 参数值 */
    public String getValueExpr(){
        return this.valueExpr;
    }
    /** 参数值 */
    public void setValueExpr(String valueExpr){
        this.valueExpr = valueExpr;
    }
    /** 参数值-最小值 */
    public String getValueMinExpr(){
        return this.valueMinExpr;
    }
    /** 参数值-最小值 */
    public void setValueMinExpr(String valueMinExpr){
        this.valueMinExpr = valueMinExpr;
    }
    /** 参数值-最小值 */
    public String getValueMaxExpr(){
        return this.valueMaxExpr;
    }
    /** 参数值-最小值 */
    public void setValueMaxExpr(String valueMaxExpr){
        this.valueMaxExpr = valueMaxExpr;
    }
    /** 默认值 */
    public String getDefaultValue(){
        return this.defaultValue;
    }
    /** 默认值 */
    public void setDefaultValue(String defaultValue){
        this.defaultValue = defaultValue;
    }
    /** 输入输出类型;In:输入，Out:输出。输入参数：根据传入业务对象，获取值，输出参数：计算后，最后输出到业务对象上。 */
    public String getInOut(){
        return this.inOut;
    }
    /** 输入输出类型;In:输入，Out:输出。输入参数：根据传入业务对象，获取值，输出参数：计算后，最后输出到业务对象上。 */
    public void setInOut(String inOut){
        this.inOut = inOut;
    }
    /** 配置界面编辑方式;Text:文本框,Select:下拉框,RadioBox:单选框,CheckBox:多选框,TextArea:多行文本框,Label:无编辑,ReferComponent:引用组件 */
    public String getEditorMode(){
        return this.editorMode;
    }
    /** 配置界面编辑方式;Text:文本框,Select:下拉框,RadioBox:单选框,CheckBox:多选框,TextArea:多行文本框,Label:无编辑,ReferComponent:引用组件 */
    public void setEditorMode(String editorMode){
        this.editorMode = editorMode;
    }
    /** 界面选项来源方式;Dict:数据字典,SQL:SQL语句,CodeTable:代码表 */
    public String getEditorSourceMode(){
        return this.editorSourceMode;
    }
    /** 界面选项来源方式;Dict:数据字典,SQL:SQL语句,CodeTable:代码表 */
    public void setEditorSourceMode(String editorSourceMode){
        this.editorSourceMode = editorSourceMode;
    }
    /** 配置界面选项数据源 */
    public String getEditorSourceData(){
        return this.editorSourceData;
    }
    /** 配置界面选项数据源 */
    public void setEditorSourceData(String editorSourceData){
        this.editorSourceData = editorSourceData;
    }
    /** 所占行数 */
    public Integer getSpanRows(){
        return this.spanRows;
    }
    /** 所占行数 */
    public void setSpanRows(Integer spanRows){
        this.spanRows = spanRows;
    }
    /** 所占列数 */
    public Integer getSpanCols(){
        return this.spanCols;
    }
    /** 所占列数 */
    public void setSpanCols(Integer spanCols){
        this.spanCols = spanCols;
    }
    /** 标题内容布局;Horizontal左右结构，Vertical上下结构 */
    public String getLayoutMode(){
        return this.layoutMode;
    }
    /** 标题内容布局;Horizontal左右结构，Vertical上下结构 */
    public void setLayoutMode(String layoutMode){
        this.layoutMode = layoutMode;
    }
    /** 引用组件URL;引用其他组件时用 */
    public String getReferUrl(){
        return this.referUrl;
    }
    /** 引用组件URL;引用其他组件时用 */
    public void setReferUrl(String referUrl){
        this.referUrl = referUrl;
    }
    /** 引用组件关键字;用于标识引用组件内部的参数关键字 */
    public String getReferKey(){
        return this.referKey;
    }
    /** 引用组件关键字;用于标识引用组件内部的参数关键字 */
    public void setReferKey(String referKey){
        this.referKey = referKey;
    }
    /** 应用层编辑模式;Label:标签显示，Readonly:只读，Editable:可编辑,Hidden:隐藏
     , */
    public String getApplyEditorMode(){
        return this.applyEditorMode;
    }
    /** 应用层编辑模式;Label:标签显示，Readonly:只读，Editable:可编辑,Hidden:隐藏
     , */
    public void setApplyEditorMode(String applyEditorMode){
        this.applyEditorMode = applyEditorMode;
    }
    /** 乐观锁 */
    public Integer getRevision(){
        return this.revision;
    }
    /** 乐观锁 */
    public void setRevision(Integer revision){
        this.revision = revision;
    }
    /** 创建人 */
    public String getCreatedBy(){
        return this.createdBy;
    }
    /** 创建人 */
    public void setCreatedBy(String createdBy){
        this.createdBy = createdBy;
    }
    /** 创建时间 */
    public Date getCreatedTime(){
        return this.createdTime;
    }
    /** 创建时间 */
    public void setCreatedTime(Date createdTime){
        this.createdTime = createdTime;
    }
    /** 更新人 */
    public String getUpdatedBy(){
        return this.updatedBy;
    }
    /** 更新人 */
    public void setUpdatedBy(String updatedBy){
        this.updatedBy = updatedBy;
    }
    /** 更新时间 */
    public Date getUpdatedTime(){
        return this.updatedTime;
    }
    /** 更新时间 */
    public void setUpdatedTime(Date updatedTime){
        this.updatedTime = updatedTime;
    }
}