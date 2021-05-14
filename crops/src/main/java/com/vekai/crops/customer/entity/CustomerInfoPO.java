package com.vekai.crops.customer.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="CUSTOMER_INFO")
public class CustomerInfoPO implements Serializable,Cloneable{
    /** 编号 */
    @Id
    @GeneratedValue
    private String id ;
    /** 应用编号 */
    private String appId ;
    /** 公众号OpenID */
    private String openId ;
    /** 是否实名认证 */
    private String realAuth ;
    /** 当前实名步骤 */
    private String authStep ;
    /** (码上办)是否信息绑定 */
    private String realBind ;
    /** (码上办)是否VIP */
    private String isVip ;
    /** (码上办)VIP类别 */
    private String vipType ;
    /** (码上办)贵宾层级 */
    private String vipLevel ;
    /** 手机号 */
    private String tel ;
    /** 身份证号 */
    private String certId ;
    /** 银行卡卡号 */
    private String cardNo ;
    /** 姓名 */
    private String certName ;
    /** 性别 */
    private String sex ;
    /** 年龄 */
    private Integer age ;
    /** 出生年月 */
    private String birth ;
    /** 民族 */
    private String nation ;
    /** 常住地址 */
    private String permanntAddr ;
    /** 邮箱 */
    private String email ;
    /** 身份证正面文件Id */
    private String frontImageId ;
    /** 身份证正面文件名称 */
    private String frontImageName ;
    /** 身份证反面文件Id */
    private String reverseImageId ;
    /** 身份证反面文件名称 */
    private String reverseImageName ;
    /** 人脸识别状态 */
    private String faceStatus ;
    /** 最近人脸识别时间 */
    private String recentFaceTime ;
    /** 人脸识别文件Id */
    private String faceFileId ;
    /** 人脸识别失败次数 */
    private String faceFailCount ;
    /** 营业执照ID */
    private String bizLicenseId ;
    /** 是否征信授权 */
    private String realCredit ;
    /** 授权文件ID */
    private String creditFileId ;
    /** 客户来源渠道 */
    private String sourceChannel ;
    /** 客户来源OA号 */
    private String sourceOa ;
    /** 客户来源机构号 */
    private String sourceOrg ;
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

    /** 编号 */
    public String getId(){
        return this.id;
    }
    /** 编号 */
    public void setId(String id){
        this.id = id;
    }
    /** 应用编号 */
    public String getAppId(){
        return this.appId;
    }
    /** 应用编号 */
    public void setAppId(String appId){
        this.appId = appId;
    }
    /** 公众号OpenID */
    public String getOpenId(){
        return this.openId;
    }
    /** 公众号OpenID */
    public void setOpenId(String openId){
        this.openId = openId;
    }
    /** 是否实名认证 */
    public String getRealAuth(){
        return this.realAuth;
    }
    /** 是否实名认证 */
    public void setRealAuth(String realAuth){
        this.realAuth = realAuth;
    }
    /** 当前实名步骤 */
    public String getAuthStep(){
        return this.authStep;
    }
    /** 当前实名步骤 */
    public void setAuthStep(String authStep){
        this.authStep = authStep;
    }
    /** (码上办)是否信息绑定 */
    public String getRealBind(){
        return this.realBind;
    }
    /** (码上办)是否信息绑定 */
    public void setRealBind(String realBind){
        this.realBind = realBind;
    }
    /** (码上办)是否VIP */
    public String getIsVip(){
        return this.isVip;
    }
    /** (码上办)是否VIP */
    public void setIsVip(String isVip){
        this.isVip = isVip;
    }
    /** (码上办)VIP类别 */
    public String getVipType(){
        return this.vipType;
    }
    /** (码上办)VIP类别 */
    public void setVipType(String vipType){
        this.vipType = vipType;
    }
    /** (码上办)贵宾层级 */
    public String getVipLevel(){
        return this.vipLevel;
    }
    /** (码上办)贵宾层级 */
    public void setVipLevel(String vipLevel){
        this.vipLevel = vipLevel;
    }
    /** 手机号 */
    public String getTel(){
        return this.tel;
    }
    /** 手机号 */
    public void setTel(String tel){
        this.tel = tel;
    }
    /** 身份证号 */
    public String getCertId(){
        return this.certId;
    }
    /** 身份证号 */
    public void setCertId(String certId){
        this.certId = certId;
    }
    /** 银行卡卡号 */
    public String getCardNo(){
        return this.cardNo;
    }
    /** 银行卡卡号 */
    public void setCardNo(String cardNo){
        this.cardNo = cardNo;
    }
    /** 姓名 */
    public String getCertName(){
        return this.certName;
    }
    /** 姓名 */
    public void setCertName(String certName){
        this.certName = certName;
    }
    /** 性别 */
    public String getSex(){
        return this.sex;
    }
    /** 性别 */
    public void setSex(String sex){
        this.sex = sex;
    }
    /** 年龄 */
    public Integer getAge(){
        return this.age;
    }
    /** 年龄 */
    public void setAge(Integer age){
        this.age = age;
    }
    /** 出生年月 */
    public String getBirth(){
        return this.birth;
    }
    /** 出生年月 */
    public void setBirth(String birth){
        this.birth = birth;
    }
    /** 民族 */
    public String getNation(){
        return this.nation;
    }
    /** 民族 */
    public void setNation(String nation){
        this.nation = nation;
    }
    /** 常住地址 */
    public String getPermanntAddr(){
        return this.permanntAddr;
    }
    /** 常住地址 */
    public void setPermanntAddr(String permanntAddr){
        this.permanntAddr = permanntAddr;
    }
    /** 邮箱 */
    public String getEmail(){
        return this.email;
    }
    /** 邮箱 */
    public void setEmail(String email){
        this.email = email;
    }
    /** 身份证正面文件Id */
    public String getFrontImageId(){
        return this.frontImageId;
    }
    /** 身份证正面文件Id */
    public void setFrontImageId(String frontImageId){
        this.frontImageId = frontImageId;
    }
    /** 身份证正面文件名称 */
    public String getFrontImageName(){
        return this.frontImageName;
    }
    /** 身份证正面文件名称 */
    public void setFrontImageName(String frontImageName){
        this.frontImageName = frontImageName;
    }
    /** 身份证反面文件Id */
    public String getReverseImageId(){
        return this.reverseImageId;
    }
    /** 身份证反面文件Id */
    public void setReverseImageId(String reverseImageId){
        this.reverseImageId = reverseImageId;
    }
    /** 身份证反面文件名称 */
    public String getReverseImageName(){
        return this.reverseImageName;
    }
    /** 身份证反面文件名称 */
    public void setReverseImageName(String reverseImageName){
        this.reverseImageName = reverseImageName;
    }
    /** 人脸识别状态 */
    public String getFaceStatus(){
        return this.faceStatus;
    }
    /** 人脸识别状态 */
    public void setFaceStatus(String faceStatus){
        this.faceStatus = faceStatus;
    }
    /** 最近人脸识别时间 */
    public String getRecentFaceTime(){
        return this.recentFaceTime;
    }
    /** 最近人脸识别时间 */
    public void setRecentFaceTime(String recentFaceTime){
        this.recentFaceTime = recentFaceTime;
    }
    /** 人脸识别文件Id */
    public String getFaceFileId(){
        return this.faceFileId;
    }
    /** 人脸识别文件Id */
    public void setFaceFileId(String faceFileId){
        this.faceFileId = faceFileId;
    }
    /** 人脸识别失败次数 */
    public String getFaceFailCount(){
        return this.faceFailCount;
    }
    /** 人脸识别失败次数 */
    public void setFaceFailCount(String faceFailCount){
        this.faceFailCount = faceFailCount;
    }
    /** 营业执照ID */
    public String getBizLicenseId(){
        return this.bizLicenseId;
    }
    /** 营业执照ID */
    public void setBizLicenseId(String bizLicenseId){
        this.bizLicenseId = bizLicenseId;
    }
    /** 是否征信授权 */
    public String getRealCredit(){
        return this.realCredit;
    }
    /** 是否征信授权 */
    public void setRealCredit(String realCredit){
        this.realCredit = realCredit;
    }
    /** 授权文件ID */
    public String getCreditFileId(){
        return this.creditFileId;
    }
    /** 授权文件ID */
    public void setCreditFileId(String creditFileId){
        this.creditFileId = creditFileId;
    }
    /** 客户来源渠道 */
    public String getSourceChannel(){
        return this.sourceChannel;
    }
    /** 客户来源渠道 */
    public void setSourceChannel(String sourceChannel){
        this.sourceChannel = sourceChannel;
    }
    /** 客户来源OA号 */
    public String getSourceOa(){
        return this.sourceOa;
    }
    /** 客户来源OA号 */
    public void setSourceOa(String sourceOa){
        this.sourceOa = sourceOa;
    }
    /** 客户来源机构号 */
    public String getSourceOrg(){
        return this.sourceOrg;
    }
    /** 客户来源机构号 */
    public void setSourceOrg(String sourceOrg){
        this.sourceOrg = sourceOrg;
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