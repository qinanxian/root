package com.vekai.appframe.conf.dossier.model;

/**
 * 文档配置项
 */
public class ConfDossierItemFileEntity {
    private String itemDefId ;
    /** 文档清单模板代码 */
    private String dossierDefKey ;
    /** 明细项代码 */
    private String itemDefKey ;
    /** 分组;ID:名称的形式 */
    private String itemGroup ;
    /** 明细项名称 */
    private String itemName ;
    /** 排序代码 */
    private String sortCode ;
    /** 统一识别码 */
    private String uniqueCode ;
    /** 明细项说明 */
    private String itemIntro ;
    /** 重要性 */
    private String importance ;
    /** 模版文件Id */
    private String tplFileId ;
    /** 状态 */
    private String itemStatus ;
    /** 文件ID */
    private String fileId ;
    /** 外部业务码 */
    private String outerId ;
    /** 查看码;用于查看文件的查看码，每次查看后失效 */
    private String showCode ;
    /** 名称 */
    private String name ;
    /** 版本代码 */
    private String versionCode ;
    /** 文件介绍 */
    private String intro ;
    /** MIME类型 */
    private String mimeType ;
    /** ContentType */
    private String contentType ;
    /** 文件大小 */
    private Long fileSize ;
    /** 文件类型 */
    private String fileType ;
    /** 条形码 */
    private String barCode ;
    /** 二维码 */
    private String qrCode ;
    /** 关键字 */
    private String keyWords ;
    /** 对象类型 */
    private String objectType ;
    /** 对象ID */
    private String objectId ;
    /** 文件存储内容;文件URL或地址或其他存储相关的ID */
    private String storedContent ;
    /** 加密公钥 */
    private String publicKey ;
    /** 加密私钥 */
    private String privateKey ;
    /** 加密密钥 */
    private String secretKey ;
    /** 签名值 */
    private String signature ;
    /** 文件管存储理器 */
    private String storeServiceName ;

    public String getItemDefId() {
        return itemDefId;
    }

    public void setItemDefId(String itemDefId) {
        this.itemDefId = itemDefId;
    }

    public String getDossierDefKey() {
        return dossierDefKey;
    }

    public void setDossierDefKey(String dossierDefKey) {
        this.dossierDefKey = dossierDefKey;
    }

    public String getItemDefKey() {
        return itemDefKey;
    }

    public void setItemDefKey(String itemDefKey) {
        this.itemDefKey = itemDefKey;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getItemIntro() {
        return itemIntro;
    }

    public void setItemIntro(String itemIntro) {
        this.itemIntro = itemIntro;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getTplFileId() {
        return tplFileId;
    }

    public void setTplFileId(String tplFileId) {
        this.tplFileId = tplFileId;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getOuterId() {
        return outerId;
    }

    public void setOuterId(String outerId) {
        this.outerId = outerId;
    }

    public String getShowCode() {
        return showCode;
    }

    public void setShowCode(String showCode) {
        this.showCode = showCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getStoredContent() {
        return storedContent;
    }

    public void setStoredContent(String storedContent) {
        this.storedContent = storedContent;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getStoreServiceName() {
        return storeServiceName;
    }

    public void setStoreServiceName(String storeServiceName) {
        this.storeServiceName = storeServiceName;
    }
}
