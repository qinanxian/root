//package com.vekai.showcase.rules.bom;
//
//import com.bstek.urule.model.Label;
//
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import java.io.Serializable;
//import java.util.Date;
//
//public class LoanDuebillBom implements Serializable,Cloneable{
//    /** 合同号 */
//    @Label("合同号")
//    private String contractId ;
//    /** 出账号 */
//    @Label("出账号")
//    private String putoutId ;
//    /** 借据号 */
//    @Id
//    @GeneratedValue
//    @Label("借据号")
//    private String duebillId ;
//    /** 担保方式 */
//    @Label("担保方式")
//    private String vouchType ;
//    /** 逾期天数 */
//    @Label("逾期天数")
//    private Integer overdueDays ;
//    /** 五级分类 */
//    @Label("五级分类")
//    private String fiveClassfication ;
//    /** 乐观锁 */
//    @Label("乐观锁")
//    private Integer revision ;
//    /** 创建人 */
//    @Label("创建人")
//    private String createdBy ;
//    /** 创建时间 */
//    @Label("创建时间")
//    private Date createdTime ;
//    /** 更新人 */
//    @Label("更新人")
//    private String updatedBy ;
//    /** 更新时间 */
//    @Label("更新时间")
//    private Date updatedTime ;
//
//    /** 合同号 */
//    public String getContractId(){
//        return this.contractId;
//    }
//    /** 合同号 */
//    public void setContractId(String contractId){
//        this.contractId = contractId;
//    }
//    /** 出账号 */
//    public String getPutoutId(){
//        return this.putoutId;
//    }
//    /** 出账号 */
//    public void setPutoutId(String putoutId){
//        this.putoutId = putoutId;
//    }
//    /** 借据号 */
//    public String getDuebillId(){
//        return this.duebillId;
//    }
//    /** 借据号 */
//    public void setDuebillId(String duebillId){
//        this.duebillId = duebillId;
//    }
//    /** 担保方式 */
//    public String getVouchType(){
//        return this.vouchType;
//    }
//    /** 担保方式 */
//    public void setVouchType(String vouchType){
//        this.vouchType = vouchType;
//    }
//    /** 逾期天数 */
//    public Integer getOverdueDays(){
//        return this.overdueDays;
//    }
//    /** 逾期天数 */
//    public void setOverdueDays(Integer overdueDays){
//        this.overdueDays = overdueDays;
//    }
//    /** 五级分类 */
//    public String getFiveClassfication(){
//        return this.fiveClassfication;
//    }
//    /** 五级分类 */
//    public void setFiveClassfication(String fiveClassfication){
//        this.fiveClassfication = fiveClassfication;
//    }
//    /** 乐观锁 */
//    public Integer getRevision(){
//        return this.revision;
//    }
//    /** 乐观锁 */
//    public void setRevision(Integer revision){
//        this.revision = revision;
//    }
//    /** 创建人 */
//    public String getCreatedBy(){
//        return this.createdBy;
//    }
//    /** 创建人 */
//    public void setCreatedBy(String createdBy){
//        this.createdBy = createdBy;
//    }
//    /** 创建时间 */
//    public Date getCreatedTime(){
//        return this.createdTime;
//    }
//    /** 创建时间 */
//    public void setCreatedTime(Date createdTime){
//        this.createdTime = createdTime;
//    }
//    /** 更新人 */
//    public String getUpdatedBy(){
//        return this.updatedBy;
//    }
//    /** 更新人 */
//    public void setUpdatedBy(String updatedBy){
//        this.updatedBy = updatedBy;
//    }
//    /** 更新时间 */
//    public Date getUpdatedTime(){
//        return this.updatedTime;
//    }
//    /** 更新时间 */
//    public void setUpdatedTime(Date updatedTime){
//        this.updatedTime = updatedTime;
//    }
//}