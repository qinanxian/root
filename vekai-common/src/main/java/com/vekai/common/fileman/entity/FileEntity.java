package com.vekai.common.fileman.entity;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "CMON_FILE")
public class FileEntity implements Serializable, Cloneable {
    /** 文件ID */
    @Id
    @GeneratedValue
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

    /** 文件ID */
    public String getFileId(){
        return this.fileId;
    }
    /** 文件ID */
    public void setFileId(String fileId){
        this.fileId = fileId;
    }
    /** 外部业务码 */
    public String getOuterId(){
        return this.outerId;
    }
    /** 外部业务码 */
    public void setOuterId(String outerId){
        this.outerId = outerId;
    }
    /** 查看码;用于查看文件的查看码，每次查看后失效 */
    public String getShowCode(){
        return this.showCode;
    }
    /** 查看码;用于查看文件的查看码，每次查看后失效 */
    public void setShowCode(String showCode){
        this.showCode = showCode;
    }
    /** 名称 */
    public String getName(){
        return this.name;
    }
    /** 名称 */
    public void setName(String name){
        this.name = name;
    }
    /** 版本代码 */
    public String getVersionCode(){
        return this.versionCode;
    }
    /** 版本代码 */
    public void setVersionCode(String versionCode){
        this.versionCode = versionCode;
    }
    /** 文件介绍 */
    public String getIntro(){
        return this.intro;
    }
    /** 文件介绍 */
    public void setIntro(String intro){
        this.intro = intro;
    }
    /** MIME类型 */
    public String getMimeType(){
        return this.mimeType;
    }
    /** MIME类型 */
    public void setMimeType(String mimeType){
        this.mimeType = mimeType;
    }
    /** ContentType */
    public String getContentType(){
        return this.contentType;
    }
    /** ContentType */
    public void setContentType(String contentType){
        this.contentType = contentType;
    }
    /** 文件大小 */
    public Long getFileSize(){
        return this.fileSize;
    }
    /** 文件大小 */
    public void setFileSize(Long fileSize){
        this.fileSize = fileSize;
    }
    /** 文件类型 */
    public String getFileType(){
        return this.fileType;
    }
    /** 文件类型 */
    public void setFileType(String fileType){
        this.fileType = fileType;
    }
    /** 条形码 */
    public String getBarCode(){
        return this.barCode;
    }
    /** 条形码 */
    public void setBarCode(String barCode){
        this.barCode = barCode;
    }
    /** 二维码 */
    public String getQrCode(){
        return this.qrCode;
    }
    /** 二维码 */
    public void setQrCode(String qrCode){
        this.qrCode = qrCode;
    }
    /** 关键字 */
    public String getKeyWords(){
        return this.keyWords;
    }
    /** 关键字 */
    public void setKeyWords(String keyWords){
        this.keyWords = keyWords;
    }
    /** 对象类型 */
    public String getObjectType(){
        return this.objectType;
    }
    /** 对象类型 */
    public void setObjectType(String objectType){
        this.objectType = objectType;
    }
    /** 对象ID */
    public String getObjectId(){
        return this.objectId;
    }
    /** 对象ID */
    public void setObjectId(String objectId){
        this.objectId = objectId;
    }
    /** 文件存储内容;文件URL或地址或其他存储相关的ID */
    public String getStoredContent(){
        return this.storedContent;
    }
    /** 文件存储内容;文件URL或地址或其他存储相关的ID */
    public void setStoredContent(String storedContent){
        this.storedContent = storedContent;
    }
    /** 加密公钥 */
    public String getPublicKey(){
        return this.publicKey;
    }
    /** 加密公钥 */
    public void setPublicKey(String publicKey){
        this.publicKey = publicKey;
    }
    /** 加密私钥 */
    public String getPrivateKey(){
        return this.privateKey;
    }
    /** 加密私钥 */
    public void setPrivateKey(String privateKey){
        this.privateKey = privateKey;
    }
    /** 加密密钥 */
    public String getSecretKey(){
        return this.secretKey;
    }
    /** 加密密钥 */
    public void setSecretKey(String secretKey){
        this.secretKey = secretKey;
    }
    /** 签名值 */
    public String getSignature(){
        return this.signature;
    }
    /** 签名值 */
    public void setSignature(String signature){
        this.signature = signature;
    }
    /** 文件管存储理器 */
    public String getStoreServiceName(){
        return this.storeServiceName;
    }
    /** 文件管存储理器 */
    public void setStoreServiceName(String storeServiceName){
        this.storeServiceName = storeServiceName;
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
