package com.vekai.auth.audit.model;

import java.io.Serializable;
import java.util.Map;

public class AuditDataObject implements Serializable,Cloneable {
    /** 操作位置;显示模板或页面URL等 */
    private String originLocation ;
    /** 操作描述;在什么位置，作了什么操作 */
    private String actionSummary ;
    /** 请求标识 */
    private String requestId ;
    /** 会话标识 */
    private String sessionId ;
    /** 客户端IP */
    private String clientAddress ;
    /** UA信息 */
    private String userAgent ;
    /** 其他的扩展属性放这里来 */
    private Map<String,Object> properies;

    public String getOriginLocation() {
        return originLocation;
    }

    public void setOriginLocation(String originLocation) {
        this.originLocation = originLocation;
    }

    public String getActionSummary() {
        return actionSummary;
    }

    public void setActionSummary(String actionSummary) {
        this.actionSummary = actionSummary;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Map<String, Object> getProperies() {
        return properies;
    }

    public void setProperies(Map<String, Object> properies) {
        this.properies = properies;
    }
}
