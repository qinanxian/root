package com.vekai.appframe.cmon.meeting.entity;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Table(name = "CMON_MEETING_MEMBER")
public class MeetingMember implements Serializable,Cloneable {

  @Id
  @GeneratedValue
  private String meetingMemberId;
  private String meetingId;
  private String memberId;
  private String memberName;
  private String memberIntro;
  private String attendance;
  private Date attendanceTime;
  private String voteAction;
  private String voteOpinion;
  private Date voteTime;
  private long revision;
  private String createdBy;
  private Date createdTime;
  private String updatedBy;
  private Date updatedTime;

  public Date getVoteTime() {
    return voteTime;
  }

  public void setVoteTime(Date voteTime) {
    this.voteTime = voteTime;
  }

  public String getVoteOpinion() {
    return voteOpinion;
  }

  public void setVoteOpinion(String voteOpinion) {
    this.voteOpinion = voteOpinion;
  }

  public String getMeetingMemberId() {
    return meetingMemberId;
  }

  public void setMeetingMemberId(String meetingMemberId) {
    this.meetingMemberId = meetingMemberId;
  }


  public String getMeetingId() {
    return meetingId;
  }

  public void setMeetingId(String meetingId) {
    this.meetingId = meetingId;
  }


  public String getMemberId() {
    return memberId;
  }

  public void setMemberId(String memberId) {
    this.memberId = memberId;
  }


  public String getMemberName() {
    return memberName;
  }

  public void setMemberName(String memberName) {
    this.memberName = memberName;
  }


  public String getMemberIntro() {
    return memberIntro;
  }

  public void setMemberIntro(String memberIntro) {
    this.memberIntro = memberIntro;
  }


  public String getAttendance() {
    return attendance;
  }

  public void setAttendance(String attendance) {
    this.attendance = attendance;
  }


  public Date getAttendanceTime() {
    return attendanceTime;
  }

  public void setAttendanceTime(Date attendanceTime) {
    this.attendanceTime = attendanceTime;
  }


  public String getVoteAction() {
    return voteAction;
  }

  public void setVoteAction(String voteAction) {
    this.voteAction = voteAction;
  }


  public long getRevision() {
    return revision;
  }

  public void setRevision(long revision) {
    this.revision = revision;
  }


  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }


  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }


  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }


  public Date getUpdatedTime() {
    return updatedTime;
  }

  public void setUpdatedTime(Date updatedTime) {
    this.updatedTime = updatedTime;
  }

}
