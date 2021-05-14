package com.vekai.auth.audit.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="AUTH_DATA_AUDIT")
public class DataAuditEntity implements Serializable,Cloneable{
    /** 审计ID */
    @Id
    @GeneratedValue
    private String dataAuditId ;
    /** 操作类型;INSERT,UPDATE,DELETE */
    private String actionType ;
    /** 操作位置;显示模板或页面URL等 */
    private String originLocation ;
    /** 操作描述;在什么位置，作了什么操作 */
    private String actionSummary ;
    /** 请求标识 */
    private String requestId ;
    /** 会话标识 */
    private String sessionId ;
    /** 数据表 */
    private String dataTable ;
    /** 对应的数据CLASS */
    private String dataClass ;
    /** 主键 */
    private String primaryKey ;
    /** 主键值 */
    private String primaryKeyValue ;
    /** 主键1;多主键时，会有用 */
    private String primaryKey1 ;
    /** 主键1值;多主键时，会有用 */
    private String primaryKey1Value ;
    /** 主键2;多主键时，会有用 */
    private String primaryKey2 ;
    /** 主键2值;多主键时，会有用 */
    private String primaryKey2Value ;
    /** 主键3;多主键时，会有用 */
    private String primaryKey3 ;
    /** 主键3值;多主键时，会有用 */
    private String primaryKey3Value ;
    /** 主键4;多主键时，会有用 */
    private String primaryKey4 ;
    /** 主键4值;多主键时，会有用 */
    private String primaryKey4Value ;
    /** 客户端IP */
    private String clientAddress ;
    /** UA信息 */
    private String userAgent ;
    /** 服务器地址 */
    private String hostAddress ;
    /** 服务器主机名 */
    private String hostName ;
    /** 请求URL */
    private String requestUri ;
    /** 请求参数 */
    private String requestQueryString ;
    /** 上下文 */
    private String contextPath ;
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

    /** 审计ID */
    public String getDataAuditId(){
        return this.dataAuditId;
    }
    /** 审计ID */
    public void setDataAuditId(String dataAuditId){
        this.dataAuditId = dataAuditId;
    }
    /** 操作类型;INSERT,UPDATE,DELETE */
    public String getActionType(){
        return this.actionType;
    }
    /** 操作类型;INSERT,UPDATE,DELETE */
    public void setActionType(String actionType){
        this.actionType = actionType;
    }
    /** 操作位置;显示模板或页面URL等 */
    public String getOriginLocation(){
        return this.originLocation;
    }
    /** 操作位置;显示模板或页面URL等 */
    public void setOriginLocation(String originLocation){
        this.originLocation = originLocation;
    }
    /** 操作描述;在什么位置，作了什么操作 */
    public String getActionSummary(){
        return this.actionSummary;
    }
    /** 操作描述;在什么位置，作了什么操作 */
    public void setActionSummary(String actionSummary){
        this.actionSummary = actionSummary;
    }
    /** 请求标识 */
    public String getRequestId(){
        return this.requestId;
    }
    /** 请求标识 */
    public void setRequestId(String requestId){
        this.requestId = requestId;
    }
    /** 会话标识 */
    public String getSessionId(){
        return this.sessionId;
    }
    /** 会话标识 */
    public void setSessionId(String sessionId){
        this.sessionId = sessionId;
    }
    /** 数据表 */
    public String getDataTable(){
        return this.dataTable;
    }
    /** 数据表 */
    public void setDataTable(String dataTable){
        this.dataTable = dataTable;
    }
    /** 对应的数据CLASS */
    public String getDataClass(){
        return this.dataClass;
    }
    /** 对应的数据CLASS */
    public void setDataClass(String dataClass){
        this.dataClass = dataClass;
    }
    /** 主键 */
    public String getPrimaryKey(){
        return this.primaryKey;
    }
    /** 主键 */
    public void setPrimaryKey(String primaryKey){
        this.primaryKey = primaryKey;
    }
    /** 主键值 */
    public String getPrimaryKeyValue(){
        return this.primaryKeyValue;
    }
    /** 主键值 */
    public void setPrimaryKeyValue(String primaryKeyValue){
        this.primaryKeyValue = primaryKeyValue;
    }
    /** 主键1;多主键时，会有用 */
    public String getPrimaryKey1(){
        return this.primaryKey1;
    }
    /** 主键1;多主键时，会有用 */
    public void setPrimaryKey1(String primaryKey1){
        this.primaryKey1 = primaryKey1;
    }
    /** 主键1值;多主键时，会有用 */
    public String getPrimaryKey1Value(){
        return this.primaryKey1Value;
    }
    /** 主键1值;多主键时，会有用 */
    public void setPrimaryKey1Value(String primaryKey1Value){
        this.primaryKey1Value = primaryKey1Value;
    }
    /** 主键2;多主键时，会有用 */
    public String getPrimaryKey2(){
        return this.primaryKey2;
    }
    /** 主键2;多主键时，会有用 */
    public void setPrimaryKey2(String primaryKey2){
        this.primaryKey2 = primaryKey2;
    }
    /** 主键2值;多主键时，会有用 */
    public String getPrimaryKey2Value(){
        return this.primaryKey2Value;
    }
    /** 主键2值;多主键时，会有用 */
    public void setPrimaryKey2Value(String primaryKey2Value){
        this.primaryKey2Value = primaryKey2Value;
    }
    /** 主键3;多主键时，会有用 */
    public String getPrimaryKey3(){
        return this.primaryKey3;
    }
    /** 主键3;多主键时，会有用 */
    public void setPrimaryKey3(String primaryKey3){
        this.primaryKey3 = primaryKey3;
    }
    /** 主键3值;多主键时，会有用 */
    public String getPrimaryKey3Value(){
        return this.primaryKey3Value;
    }
    /** 主键3值;多主键时，会有用 */
    public void setPrimaryKey3Value(String primaryKey3Value){
        this.primaryKey3Value = primaryKey3Value;
    }
    /** 主键4;多主键时，会有用 */
    public String getPrimaryKey4(){
        return this.primaryKey4;
    }
    /** 主键4;多主键时，会有用 */
    public void setPrimaryKey4(String primaryKey4){
        this.primaryKey4 = primaryKey4;
    }
    /** 主键4值;多主键时，会有用 */
    public String getPrimaryKey4Value(){
        return this.primaryKey4Value;
    }
    /** 主键4值;多主键时，会有用 */
    public void setPrimaryKey4Value(String primaryKey4Value){
        this.primaryKey4Value = primaryKey4Value;
    }
    /** 客户端IP */
    public String getClientAddress(){
        return this.clientAddress;
    }
    /** 客户端IP */
    public void setClientAddress(String clientAddress){
        this.clientAddress = clientAddress;
    }
    /** UA信息 */
    public String getUserAgent(){
        return this.userAgent;
    }
    /** UA信息 */
    public void setUserAgent(String userAgent){
        this.userAgent = userAgent;
    }
    /** 服务器地址 */
    public String getHostAddress(){
        return this.hostAddress;
    }
    /** 服务器地址 */
    public void setHostAddress(String hostAddress){
        this.hostAddress = hostAddress;
    }
    /** 服务器主机名 */
    public String getHostName(){
        return this.hostName;
    }
    /** 服务器主机名 */
    public void setHostName(String hostName){
        this.hostName = hostName;
    }
    /** 请求URL */
    public String getRequestUri(){
        return this.requestUri;
    }
    /** 请求URL */
    public void setRequestUri(String requestUri){
        this.requestUri = requestUri;
    }
    /** 请求参数 */
    public String getRequestQueryString(){
        return this.requestQueryString;
    }
    /** 请求参数 */
    public void setRequestQueryString(String requestQueryString){
        this.requestQueryString = requestQueryString;
    }
    /** 上下文 */
    public String getContextPath(){
        return this.contextPath;
    }
    /** 上下文 */
    public void setContextPath(String contextPath){
        this.contextPath = contextPath;
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