package com.vekai.auth.holder;

import com.vekai.auth.audit.model.AuditDataObject;

import java.util.Map;
import java.util.Optional;

/**
 * 审计对象保持器
 */
public class AuditHolder {
    private static final ThreadLocal<AuditDataObject> holder = new ThreadLocal<AuditDataObject>();

    public static AuditDataObject getDataObject(){
        return holder.get();
    }
    public static void setDataObject(AuditDataObject dataObject){
        holder.set(dataObject);
    }
    public static void clear(){
        holder.remove();
    }


    public static String getOriginLocation() {
//        AuditDataObject dataObject = getDataObject();
//        return Optional.ofNullable(dataObject).orElseGet(() -> new AuditDataObject()).getOriginLocation();
        return Optional.ofNullable(getDataObject())
                .orElseGet(AuditDataObject::new).getOriginLocation();
    }

    public static void setOriginLocation(String originLocation) {
//        getDataObject().setOriginLocation(originLocation);
        Optional.ofNullable(getDataObject())
                .orElseGet(AuditDataObject::new).setOriginLocation(originLocation);
    }

    public static String getActionSummary() {
//        return getDataObject().getActionSummary();
        return Optional.ofNullable(getDataObject())
                .orElseGet(AuditDataObject::new).getActionSummary();
    }

    public static void setActionSummary(String actionSummary) {
        Optional.ofNullable(getDataObject())
                .orElseGet(AuditDataObject::new).setActionSummary(actionSummary);
    }

    public static String getRequestId() {
//        return getDataObject().getRequestId();
        return Optional.ofNullable(getDataObject())
                .orElseGet(AuditDataObject::new).getRequestId();
    }

    public static void setRequestId(String requestId) {
//        getDataObject().setRequestId(requestId);
        Optional.ofNullable(getDataObject())
                .orElseGet(AuditDataObject::new).setRequestId(requestId);
    }

    public static String getSessionId() {
//        return getDataObject().getSessionId();
        return Optional.ofNullable(getDataObject())
                .orElseGet(AuditDataObject::new).getSessionId();
    }

    public static void setSessionId(String sessionId) {
//        getDataObject().setSessionId(sessionId);
        Optional.ofNullable(getDataObject())
                .orElseGet(AuditDataObject::new).setSessionId(sessionId);
    }

    public static Map<String, Object> getProperies() {
//        return getDataObject().getProperies();
        return Optional.ofNullable(getDataObject())
                .orElseGet(AuditDataObject::new).getProperies();
    }

    public static void setProperies(Map<String, Object> properies) {
        Optional.ofNullable(getDataObject())
                .orElseGet(AuditDataObject::new).setProperies(properies);
    }

    public static String getClientAddress() {
//        return getDataObject().getClientAddress();
        return Optional.ofNullable(getDataObject())
                .orElseGet(AuditDataObject::new).getClientAddress();
    }

    public static void setClientAddress(String clientAddress) {
//        getDataObject().setClientAddress(clientAddress);
        Optional.ofNullable(getDataObject())
                .orElseGet(AuditDataObject::new).setClientAddress(clientAddress);
    }

    public static String getUserAgent() {
//        return getDataObject().getUserAgent();
        return Optional.ofNullable(getDataObject())
                .orElseGet(AuditDataObject::new).getUserAgent();
    }

    public static void setUserAgent(String userAgent) {
//        getDataObject().setUserAgent(userAgent);
        Optional.ofNullable(getDataObject())
                .orElseGet(AuditDataObject::new).setUserAgent(userAgent);
    }
}
