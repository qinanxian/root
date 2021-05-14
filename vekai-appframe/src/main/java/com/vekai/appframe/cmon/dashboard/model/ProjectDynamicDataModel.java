package com.vekai.appframe.cmon.dashboard.model;

/**
 * @Author: qyyao
 * @Description: 投前项目数据统计模型
 * @Date: Created in 17:38 04/07/2018
 */
public class ProjectDynamicDataModel {

    //本月会议数量
    private String meetingPM;
    //本年会议数量
    private String meetingPY;

    //本月立项通过的项目数量
    private String ivstPlanPM;

    //本年立项通过的项目数量
    private String ivstPlanPY;

    //本月尽调审核通过项目数量
    private String dueDiligencePM;

    //本年尽调审核通过的项目数量
    private String dueDiligencePY;


    //本月项目下签约的主合同
    private String contractCompletedPM;

    //本年项目下签约的主合同
    private String contractCompletedPY;

    //本月项目下首次出资审核通过的数量
    private String ivstPlanLoanPM;

    //本年项目下首次出资审核通过的数量
    private  String ivstPlanLoanPY;


    public String getMeetingPM() {
        return meetingPM;
    }

    public void setMeetingPM(String meetingPM) {
        this.meetingPM = meetingPM;
    }

    public String getMeetingPY() {
        return meetingPY;
    }

    public void setMeetingPY(String meetingPY) {
        this.meetingPY = meetingPY;
    }

    public String getIvstPlanPM() {
        return ivstPlanPM;
    }

    public void setIvstPlanPM(String ivstPlanPM) {
        this.ivstPlanPM = ivstPlanPM;
    }

    public String getIvstPlanPY() {
        return ivstPlanPY;
    }

    public void setIvstPlanPY(String ivstPlanPY) {
        this.ivstPlanPY = ivstPlanPY;
    }

    public String getDueDiligencePM() {
        return dueDiligencePM;
    }

    public void setDueDiligencePM(String dueDiligencePM) {
        this.dueDiligencePM = dueDiligencePM;
    }

    public String getDueDiligencePY() {
        return dueDiligencePY;
    }

    public void setDueDiligencePY(String dueDiligencePY) {
        this.dueDiligencePY = dueDiligencePY;
    }

    public String getContractCompletedPM() {
        return contractCompletedPM;
    }

    public void setContractCompletedPM(String contractCompletedPM) {
        this.contractCompletedPM = contractCompletedPM;
    }

    public String getContractCompletedPY() {
        return contractCompletedPY;
    }

    public void setContractCompletedPY(String contractCompletedPY) {
        this.contractCompletedPY = contractCompletedPY;
    }

    public String getIvstPlanLoanPM() {
        return ivstPlanLoanPM;
    }

    public void setIvstPlanLoanPM(String ivstPlanLoanPM) {
        this.ivstPlanLoanPM = ivstPlanLoanPM;
    }

    public String getIvstPlanLoanPY() {
        return ivstPlanLoanPY;
    }

    public void setIvstPlanLoanPY(String ivstPlanLoanPY) {
        this.ivstPlanLoanPY = ivstPlanLoanPY;
    }
}
