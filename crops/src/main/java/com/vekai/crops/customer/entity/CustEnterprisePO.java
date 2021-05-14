package com.vekai.crops.customer.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Table(name = "CUST_ENT")
public class CustEnterprisePO  implements Serializable,Cloneable {
    /** 客户号 */
    @Id
    @GeneratedValue
    private String custId ;
    /** 客户名称 */
    private String custName ;
    /** 客户简称 */
    private String shortName ;
    /** 客户英文名 */
    private String engName ;
    /** 贷款卡号 */
    private String loanCardId ;
    /** 贷款卡密码 */
    private String loanCardPass ;
    /** 统一社会信用代码 */
    private String unifiedCreditCode ;
    /** 营业执照号;三证合一 */
    private String licenseCode ;
    /** 执照起始日 */
    private Date licenseEffectiveDate ;
    /** 执照到期日 */
    private Date licenseExpirtyDate ;
    /** 企业成立日 */
    private Date estabDate ;
    /** 组织类型;国有，集体，外资等类型 */
    private String enterpriseType ;
    /** 企业性质;中央，省部署，市属，区县属，地方政府融资平台以及其他 */
    private String enterpriseNature ;
    /** 法人代表证件类型 */
    private String representCertType ;
    /** 法人代表证件号 */
    private String representCertId ;
    /** 法人代表 */
    private String representName ;
    /** 企业规模 */
    private String enterpriseScale ;
    /** 注册币种 */
    private String registerCurrency ;
    /** 注册资本 */
    private Double registerCapital ;
    /** 公司地址 */
    private String address ;
    /** 公司网址 */
    private String website ;
    /** 国标行业分类 */
    private String gbIndustry ;
    /** 国标行业分类1级 */
    private String gbIndustryL1 ;
    /** 国标行业分类2级 */
    private String gbIndustryL2 ;
    /** 员工人数 */
    private Integer employeeNumber ;
    /** 资产总额 */
    private Double totalAssets ;
    /** 存续交易总额 */
    private Double duringTransactionSum ;
    /** 负债总额 */
    private Double totalDebt ;
    /** 经营场地面积 */
    private Integer operatingSiteArea ;
    /** 经营场地所有权 */
    private String operatingSiteDroit ;
    /** 经营范围 */
    private String businessScope ;
    /** 是否上市公司 */
    private String isPublicCompany ;
    /** 上市地点 */
    private String listedPlace ;
    /** 股票代码 */
    private String stockCode ;
    /** 是否政府融资平台 */
    private String isGovFinancingPlatform ;
    /** 是否有涉诉信息 */
    private String existsLawsuit ;
    /** 是否有负面舆情信息 */
    private String existsNagativeConsensus ;
    /** 是否被执行人 */
    private String isBeExecuted ;
    /** 被执行人最新立案时间 */
    private Date lawCaseDate ;
    /** 是否失信被执行人 */
    private String isDishonest ;
    /** 失信被执行人最新发布时间 */
    private Date dishonestIssueDate ;
    /** 企业经营状态 */
    private String operationStatus ;
    /** 暂存草稿 */
    private String isDraft ;
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

    /** 客户号 */
    public String getCustId(){
        return this.custId;
    }
    /** 客户号 */
    public void setCustId(String custId){
        this.custId = custId;
    }
    /** 客户名称 */
    public String getCustName(){
        return this.custName;
    }
    /** 客户名称 */
    public void setCustName(String custName){
        this.custName = custName;
    }
    /** 客户简称 */
    public String getShortName(){
        return this.shortName;
    }
    /** 客户简称 */
    public void setShortName(String shortName){
        this.shortName = shortName;
    }
    /** 客户英文名 */
    public String getEngName(){
        return this.engName;
    }
    /** 客户英文名 */
    public void setEngName(String engName){
        this.engName = engName;
    }
    /** 贷款卡号 */
    public String getLoanCardId(){
        return this.loanCardId;
    }
    /** 贷款卡号 */
    public void setLoanCardId(String loanCardId){
        this.loanCardId = loanCardId;
    }
    /** 贷款卡密码 */
    public String getLoanCardPass(){
        return this.loanCardPass;
    }
    /** 贷款卡密码 */
    public void setLoanCardPass(String loanCardPass){
        this.loanCardPass = loanCardPass;
    }
    /** 统一社会信用代码 */
    public String getUnifiedCreditCode(){
        return this.unifiedCreditCode;
    }
    /** 统一社会信用代码 */
    public void setUnifiedCreditCode(String unifiedCreditCode){
        this.unifiedCreditCode = unifiedCreditCode;
    }
    /** 营业执照号;三证合一 */
    public String getLicenseCode(){
        return this.licenseCode;
    }
    /** 营业执照号;三证合一 */
    public void setLicenseCode(String licenseCode){
        this.licenseCode = licenseCode;
    }
    /** 执照起始日 */
    public Date getLicenseEffectiveDate(){
        return this.licenseEffectiveDate;
    }
    /** 执照起始日 */
    public void setLicenseEffectiveDate(Date licenseEffectiveDate){
        this.licenseEffectiveDate = licenseEffectiveDate;
    }
    /** 执照到期日 */
    public Date getLicenseExpirtyDate(){
        return this.licenseExpirtyDate;
    }
    /** 执照到期日 */
    public void setLicenseExpirtyDate(Date licenseExpirtyDate){
        this.licenseExpirtyDate = licenseExpirtyDate;
    }
    /** 企业成立日 */
    public Date getEstabDate(){
        return this.estabDate;
    }
    /** 企业成立日 */
    public void setEstabDate(Date estabDate){
        this.estabDate = estabDate;
    }
    /** 组织类型;国有，集体，外资等类型 */
    public String getEnterpriseType(){
        return this.enterpriseType;
    }
    /** 组织类型;国有，集体，外资等类型 */
    public void setEnterpriseType(String enterpriseType){
        this.enterpriseType = enterpriseType;
    }
    /** 企业性质;中央，省部署，市属，区县属，地方政府融资平台以及其他 */
    public String getEnterpriseNature(){
        return this.enterpriseNature;
    }
    /** 企业性质;中央，省部署，市属，区县属，地方政府融资平台以及其他 */
    public void setEnterpriseNature(String enterpriseNature){
        this.enterpriseNature = enterpriseNature;
    }
    /** 法人代表证件类型 */
    public String getRepresentCertType(){
        return this.representCertType;
    }
    /** 法人代表证件类型 */
    public void setRepresentCertType(String representCertType){
        this.representCertType = representCertType;
    }
    /** 法人代表证件号 */
    public String getRepresentCertId(){
        return this.representCertId;
    }
    /** 法人代表证件号 */
    public void setRepresentCertId(String representCertId){
        this.representCertId = representCertId;
    }
    /** 法人代表 */
    public String getRepresentName(){
        return this.representName;
    }
    /** 法人代表 */
    public void setRepresentName(String representName){
        this.representName = representName;
    }
    /** 企业规模 */
    public String getEnterpriseScale(){
        return this.enterpriseScale;
    }
    /** 企业规模 */
    public void setEnterpriseScale(String enterpriseScale){
        this.enterpriseScale = enterpriseScale;
    }
    /** 注册币种 */
    public String getRegisterCurrency(){
        return this.registerCurrency;
    }
    /** 注册币种 */
    public void setRegisterCurrency(String registerCurrency){
        this.registerCurrency = registerCurrency;
    }
    /** 注册资本 */
    public Double getRegisterCapital(){
        return this.registerCapital;
    }
    /** 注册资本 */
    public void setRegisterCapital(Double registerCapital){
        this.registerCapital = registerCapital;
    }
    /** 公司地址 */
    public String getAddress(){
        return this.address;
    }
    /** 公司地址 */
    public void setAddress(String address){
        this.address = address;
    }
    /** 公司网址 */
    public String getWebsite(){
        return this.website;
    }
    /** 公司网址 */
    public void setWebsite(String website){
        this.website = website;
    }
    /** 国标行业分类 */
    public String getGbIndustry(){
        return this.gbIndustry;
    }
    /** 国标行业分类 */
    public void setGbIndustry(String gbIndustry){
        this.gbIndustry = gbIndustry;
    }
    /** 国标行业分类1级 */
    public String getGbIndustryL1(){
        return this.gbIndustryL1;
    }
    /** 国标行业分类1级 */
    public void setGbIndustryL1(String gbIndustryL1){
        this.gbIndustryL1 = gbIndustryL1;
    }
    /** 国标行业分类2级 */
    public String getGbIndustryL2(){
        return this.gbIndustryL2;
    }
    /** 国标行业分类2级 */
    public void setGbIndustryL2(String gbIndustryL2){
        this.gbIndustryL2 = gbIndustryL2;
    }
    /** 员工人数 */
    public Integer getEmployeeNumber(){
        return this.employeeNumber;
    }
    /** 员工人数 */
    public void setEmployeeNumber(Integer employeeNumber){
        this.employeeNumber = employeeNumber;
    }
    /** 资产总额 */
    public Double getTotalAssets(){
        return this.totalAssets;
    }
    /** 资产总额 */
    public void setTotalAssets(Double totalAssets){
        this.totalAssets = totalAssets;
    }
    /** 存续交易总额 */
    public Double getDuringTransactionSum(){
        return this.duringTransactionSum;
    }
    /** 存续交易总额 */
    public void setDuringTransactionSum(Double duringTransactionSum){
        this.duringTransactionSum = duringTransactionSum;
    }
    /** 负债总额 */
    public Double getTotalDebt(){
        return this.totalDebt;
    }
    /** 负债总额 */
    public void setTotalDebt(Double totalDebt){
        this.totalDebt = totalDebt;
    }
    /** 经营场地面积 */
    public Integer getOperatingSiteArea(){
        return this.operatingSiteArea;
    }
    /** 经营场地面积 */
    public void setOperatingSiteArea(Integer operatingSiteArea){
        this.operatingSiteArea = operatingSiteArea;
    }
    /** 经营场地所有权 */
    public String getOperatingSiteDroit(){
        return this.operatingSiteDroit;
    }
    /** 经营场地所有权 */
    public void setOperatingSiteDroit(String operatingSiteDroit){
        this.operatingSiteDroit = operatingSiteDroit;
    }
    /** 经营范围 */
    public String getBusinessScope(){
        return this.businessScope;
    }
    /** 经营范围 */
    public void setBusinessScope(String businessScope){
        this.businessScope = businessScope;
    }
    /** 是否上市公司 */
    public String getIsPublicCompany(){
        return this.isPublicCompany;
    }
    /** 是否上市公司 */
    public void setIsPublicCompany(String isPublicCompany){
        this.isPublicCompany = isPublicCompany;
    }
    /** 上市地点 */
    public String getListedPlace(){
        return this.listedPlace;
    }
    /** 上市地点 */
    public void setListedPlace(String listedPlace){
        this.listedPlace = listedPlace;
    }
    /** 股票代码 */
    public String getStockCode(){
        return this.stockCode;
    }
    /** 股票代码 */
    public void setStockCode(String stockCode){
        this.stockCode = stockCode;
    }
    /** 是否政府融资平台 */
    public String getIsGovFinancingPlatform(){
        return this.isGovFinancingPlatform;
    }
    /** 是否政府融资平台 */
    public void setIsGovFinancingPlatform(String isGovFinancingPlatform){
        this.isGovFinancingPlatform = isGovFinancingPlatform;
    }
    /** 是否有涉诉信息 */
    public String getExistsLawsuit(){
        return this.existsLawsuit;
    }
    /** 是否有涉诉信息 */
    public void setExistsLawsuit(String existsLawsuit){
        this.existsLawsuit = existsLawsuit;
    }
    /** 是否有负面舆情信息 */
    public String getExistsNagativeConsensus(){
        return this.existsNagativeConsensus;
    }
    /** 是否有负面舆情信息 */
    public void setExistsNagativeConsensus(String existsNagativeConsensus){
        this.existsNagativeConsensus = existsNagativeConsensus;
    }
    /** 是否被执行人 */
    public String getIsBeExecuted(){
        return this.isBeExecuted;
    }
    /** 是否被执行人 */
    public void setIsBeExecuted(String isBeExecuted){
        this.isBeExecuted = isBeExecuted;
    }
    /** 被执行人最新立案时间 */
    public Date getLawCaseDate(){
        return this.lawCaseDate;
    }
    /** 被执行人最新立案时间 */
    public void setLawCaseDate(Date lawCaseDate){
        this.lawCaseDate = lawCaseDate;
    }
    /** 是否失信被执行人 */
    public String getIsDishonest(){
        return this.isDishonest;
    }
    /** 是否失信被执行人 */
    public void setIsDishonest(String isDishonest){
        this.isDishonest = isDishonest;
    }
    /** 失信被执行人最新发布时间 */
    public Date getDishonestIssueDate(){
        return this.dishonestIssueDate;
    }
    /** 失信被执行人最新发布时间 */
    public void setDishonestIssueDate(Date dishonestIssueDate){
        this.dishonestIssueDate = dishonestIssueDate;
    }
    /** 企业经营状态 */
    public String getOperationStatus(){
        return this.operationStatus;
    }
    /** 企业经营状态 */
    public void setOperationStatus(String operationStatus){
        this.operationStatus = operationStatus;
    }
    /** 暂存草稿 */
    public String getIsDraft(){
        return this.isDraft;
    }
    /** 暂存草稿 */
    public void setIsDraft(String isDraft){
        this.isDraft = isDraft;
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
