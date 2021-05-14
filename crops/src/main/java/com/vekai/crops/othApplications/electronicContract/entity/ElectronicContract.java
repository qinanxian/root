package com.vekai.crops.othApplications.electronicContract.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name="ELECTRONIC_CONTRACT")
public class ElectronicContract implements Serializable,Cloneable{

    /** 编号 */
    @Id
    @GeneratedValue
    private String id ;
    /** 姓名 */
    private String certName ;
    /** 身份证号 */
    private String certId ;
    /** 电话号码 */
    private String phoneNo ;
    /** 开始时间 */
    private String startDate ;
    /** 结束时间 */
    private String endDate ;
    /** 借款金额 */
    private Double loanAmt ;
    /** LPR基准 */
    private Double lpr  ;
    /** 上浮基点 */
    private Integer point  ;
    /** 利率 */
    private Double rate  ;
    /** 借款账号 */
    private String loanAcct ;
    /** 还款方式 */
    private String payment ;
    /** 还款账号 */
    private String repayAcct ;
    /** 还款方式 */
    private String repayType ;
    /** 地址 */
    private String address ;
    /** 用途 */
    private String loanUse ;
    /** pdf文件id */
    private String pdfFileId ;
    /** pdf文件name */
    private String pdfFileName ;
    /** 确认状态 */
    private String state ;
    /** 确认时间 */
    private String time ;
    /** oa */
    private String oa ;
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
    /** docid */
    private String docid ;
    /** 客户经理名称 */
    private String managerName ;
    /** 客户经理OA号 */
    private String managerAccunt ;

    /** 编号 */
    public String getId() {
        return id;
    }
    /** 编号 */
    public void setId(String id) {
        this.id = id;
    }
    /** 姓名 */
    public String getCertName() {
        return certName;
    }
    /** 姓名 */
    public void setCertName(String certName) {
        this.certName = certName;
    }
    /** 身份证号 */
    public String getCertId() {
        return certId;
    }
    /** 身份证号 */
    public void setCertId(String certId) {
        this.certId = certId;
    }
    /** 电话号码 */
    public String getPhoneNo() {
        return phoneNo;
    }
    /** 电话号码 */
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
    /** 开始时间 */
    public String getStartDate() {
        return startDate;
    }
    /** 开始时间 */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    /** 结束时间 */
    public String getEndDate() {
        return endDate;
    }
    /** 结束时间 */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    /** 贷款金额 */
    public Double getLoanAmt() {
        return loanAmt;
    }
    /** 贷款金额 */
    public void setLoanAmt(Double loanAmt) {
        this.loanAmt = loanAmt;
    }
    /** LPR基准 */
    public Double getLpr() {
        return lpr;
    }
    /** LPR基准 */
    public void setLpr(Double lpr) {
        this.lpr = lpr;
    }
    /** 上浮基点 */
    public Integer getPoint() {
        return point;
    }
    /** 上浮基点 */
    public void setPoint(Integer point) {
        this.point = point;
    }
    /** 利率 */
    public Double getRate() {
        return rate;
    }
    /** 利率 */
    public void setRate(Double rate) {
        this.rate = rate;
    }
    /** 贷款账号 */
    public String getLoanAcct() {
        return loanAcct;
    }
    /** 贷款账号 */
    public void setLoanAcct(String loanAcct) {
        this.loanAcct = loanAcct;
    }
    /** 资金支付方式 */
    public String getPayment() {
        return payment;
    }
    /** 资金支付方式 */
    public void setPayment(String payment) {
        this.payment = payment;
    }
    /** 还款账号 */
    public String getRepayAcct() {
        return repayAcct;
    }
    /** 还款账号 */
    public void setRepayAcct(String repayAcct) {
        this.repayAcct = repayAcct;
    }
    /** 还款方式 */
    public String getRepayType() {
        return repayType;
    }
    /** 还款方式 */
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    /** 地址 */
    public String getAddress() {
        return address;
    }
    /** 地址 */
    public void setAddress(String address) {
        this.address = address;
    }
    /** 贷款用途 */
    public String getLoanUse() {
        return loanUse;
    }
    /** 贷款用途 */
    public void setLoanUse(String loanUse) {
        this.loanUse = loanUse;
    }
    /** pdf文件id */
    public String getPdfFileId() {
        return pdfFileId;
    }
    /** pdf文件id */
    public void setPdfFileId(String pdfFileId) {
        this.pdfFileId = pdfFileId;
    }
    /** pdf文件name */
    public String getPdfFileName() {
        return pdfFileName;
    }
    /** pdf文件name */
    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }
    /** 状态 */
    public String getState() {
        return state;
    }
    /** 状态 */
    public void setState(String state) {
        this.state = state;
    }
    /** 确认时间 */
    public String getTime() {
        return time;
    }
    /** 确认时间 */
    public void setTime(String time) {
        this.time = time;
    }
    /** oa */
    public String getOa() {
        return oa;
    }
    /** oa */
    public void setOa(String oa) {
        this.oa = oa;
    }
    /** docid */
    public String getDocid() {
        return docid;
    }
    /** docid */
    public void setDocid(String docid) {
        this.docid = docid;
    }
     /** 乐观锁 */
    public Integer getRevision() {
        return revision;
    }
    /** 乐观锁 */
    public void setRevision(Integer revision) {
        this.revision = revision;
    }
    /** 创建人 */
    public String getCreatedBy() {
        return createdBy;
    }
    /** 创建人 */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    /** 创建时间 */
    public Date getCreatedTime() {
        return createdTime;
    }
    /** 创建时间 */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
    /** 更新人 */
    public String getUpdatedBy() {
        return updatedBy;
    }
    /** 更新人 */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    /** 更新时间 */
    public Date getUpdatedTime() {
        return updatedTime;
    }
    /** 更新时间 */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
    /** 客户经理姓名 */
    public String getManagerName() {
        return managerName;
    }
    /** 客户经理姓名 */
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
    /** 客户经理OA号 */
    public String getManagerAccunt() {
        return managerAccunt;
    }
    /** 客户经理OA号 */
    public void setManagerAccunt(String managerAccunt) {
        this.managerAccunt = managerAccunt;
    }

}