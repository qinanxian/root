package com.vekai.crops.etl.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Table(name="HM_REMIND_MSG")
public class HmRemindMsg implements Serializable,Cloneable{
    /** 消息ID */
    @Id
    @GeneratedValue
    private String msgId ;
    /** 通知渠道 */
    private String chnNo ;
    /** 消息类型 */
    private String msgType ;
    /** 通知人ID */
    private String usrId ;
    /** CRM客户号 */
    private String custNo ;
    /** 客户名称 */
    private String custName ;
    /** VIP类型 */
    private String vipType ;
    /** 办理业务名称 */
    private String bsName ;
    /** 网点ID */
    private String orgId ;
    /** 网点名称 */
    private String orgName ;
    /** 提醒日期 */
    private String remindDate ;
    /** 待处理业务笔数 */
    private Integer busiNum ;
    /** 交易金额 */
    private String busiAmt ;
    /** 借贷标志 */
    private String blFlag ;
    /** 业务到期日期 */
    private String busiDueDate ;
    /** 消息生成时间 */
    private String msgGenerateTime ;
    /** 消息生成渠道 */
    private String msgGenerateChn ;
    /** 消息获取状态 */
    private String msgPullStat ;
    /** 消息获取时间 */
    private String msgPullTime ;
    /** 当前叫号 */
    private String currQueueNo ;
    /** 排队号码 */
    private String queueNo ;
    /** 前面排号数量 */
    private Integer beforeNum ;
    /** 转介发起人编号 */
    private String managerNo ;
    /** 转介发起人名称 */
    private String managerName ;
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

    /** 消息ID */
    public String getMsgId(){
        return this.msgId;
    }
    /** 消息ID */
    public void setMsgId(String msgId){
        this.msgId = msgId;
    }
    /** 通知渠道 */
    public String getChnNo(){
        return this.chnNo;
    }
    /** 通知渠道 */
    public void setChnNo(String chnNo){
        this.chnNo = chnNo;
    }
    /** 消息类型 */
    public String getMsgType(){
        return this.msgType;
    }
    /** 消息类型 */
    public void setMsgType(String msgType){
        this.msgType = msgType;
    }
    /** 通知人ID */
    public String getUsrId(){
        return this.usrId;
    }
    /** 通知人ID */
    public void setUsrId(String usrId){
        this.usrId = usrId;
    }
    /** CRM客户号 */
    public String getCustNo(){
        return this.custNo;
    }
    /** CRM客户号 */
    public void setCustNo(String custNo){
        this.custNo = custNo;
    }
    /** 客户名称 */
    public String getCustName(){
        return this.custName;
    }
    /** 客户名称 */
    public void setCustName(String custName){
        this.custName = custName;
    }
    /** VIP类型 */
    public String getVipType(){
        return this.vipType;
    }
    /** VIP类型 */
    public void setVipType(String vipType){
        this.vipType = vipType;
    }
    /** 办理业务名称 */
    public String getBsName(){
        return this.bsName;
    }
    /** 办理业务名称 */
    public void setBsName(String bsName){
        this.bsName = bsName;
    }
    /** 网点ID */
    public String getOrgId(){
        return this.orgId;
    }
    /** 网点ID */
    public void setOrgId(String orgId){
        this.orgId = orgId;
    }
    /** 网点名称 */
    public String getOrgName(){
        return this.orgName;
    }
    /** 网点名称 */
    public void setOrgName(String orgName){
        this.orgName = orgName;
    }
    /** 提醒日期 */
    public String getRemindDate(){
        return this.remindDate;
    }
    /** 提醒日期 */
    public void setRemindDate(String remindDate){
        this.remindDate = remindDate;
    }
    /** 待处理业务笔数 */
    public Integer getBusiNum(){
        return this.busiNum;
    }
    /** 待处理业务笔数 */
    public void setBusiNum(Integer busiNum){
        this.busiNum = busiNum;
    }
    /** 交易金额 */
    public String getBusiAmt(){
        return this.busiAmt;
    }
    /** 交易金额 */
    public void setBusiAmt(String busiAmt){
        this.busiAmt = busiAmt;
    }
    /** 借贷标志 */
    public String getBlFlag(){
        return this.blFlag;
    }
    /** 借贷标志 */
    public void setBlFlag(String blFlag){
        this.blFlag = blFlag;
    }
    /** 业务到期日期 */
    public String getBusiDueDate(){
        return this.busiDueDate;
    }
    /** 业务到期日期 */
    public void setBusiDueDate(String busiDueDate){
        this.busiDueDate = busiDueDate;
    }
    /** 消息生成时间 */
    public String getMsgGenerateTime(){
        return this.msgGenerateTime;
    }
    /** 消息生成时间 */
    public void setMsgGenerateTime(String msgGenerateTime){
        this.msgGenerateTime = msgGenerateTime;
    }
    /** 消息生成渠道 */
    public String getMsgGenerateChn(){
        return this.msgGenerateChn;
    }
    /** 消息生成渠道 */
    public void setMsgGenerateChn(String msgGenerateChn){
        this.msgGenerateChn = msgGenerateChn;
    }
    /** 消息获取状态 */
    public String getMsgPullStat(){
        return this.msgPullStat;
    }
    /** 消息获取状态 */
    public void setMsgPullStat(String msgPullStat){
        this.msgPullStat = msgPullStat;
    }
    /** 消息获取时间 */
    public String getMsgPullTime(){
        return this.msgPullTime;
    }
    /** 消息获取时间 */
    public void setMsgPullTime(String msgPullTime){
        this.msgPullTime = msgPullTime;
    }
    /** 当前叫号 */
    public String getCurrQueueNo(){
        return this.currQueueNo;
    }
    /** 当前叫号 */
    public void setCurrQueueNo(String currQueueNo){
        this.currQueueNo = currQueueNo;
    }
    /** 排队号码 */
    public String getQueueNo(){
        return this.queueNo;
    }
    /** 排队号码 */
    public void setQueueNo(String queueNo){
        this.queueNo = queueNo;
    }
    /** 前面排号数量 */
    public Integer getBeforeNum(){
        return this.beforeNum;
    }
    /** 前面排号数量 */
    public void setBeforeNum(Integer beforeNum){
        this.beforeNum = beforeNum;
    }
    /** 转介发起人编号 */
    public String getManagerNo(){
        return this.managerNo;
    }
    /** 转介发起人编号 */
    public void setManagerNo(String managerNo){
        this.managerNo = managerNo;
    }
    /** 转介发起人名称 */
    public String getManagerName(){
        return this.managerName;
    }
    /** 转介发起人名称 */
    public void setManagerName(String managerName){
        this.managerName = managerName;
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