package com.vekai.appframe.cmon.meeting.entity;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "CMON_MEETING")
public class Meeting implements Serializable,Cloneable {
  @Id
  @GeneratedValue
  private String meetingId;
  private String objectType;
  private String objectId;
  private Date openingTime;
  private Date finishDate;
  private String place;
  private String meetingTopic;
  private String briefDescription;
  private String mettingContent;
  private String mettingMinutes;
  private String meetingVerdict;
  private String verdictInfo;
  private String secretary;
  private String sponsor;
  private String presenter;
  private String mettingStatus;
  private String meetingType;
  private String processStatus;
  private long attendanceCount;
  private long actualAttendanceCount;
  private long favorVote;
  private long negativeVote;
  private long abstainVote;
  private String isPublic;
  private long revision;
  private String createdBy;
  private Date createdTime;
  private String updatedBy;
  private Date updatedTime;
  private String isDisabled;
  private String dueDiligenceType;

  public String getDueDiligenceType() {
    return dueDiligenceType;
  }

  public void setDueDiligenceType(String dueDiligenceType) {
    this.dueDiligenceType = dueDiligenceType;
  }

  public String getIsDisabled() {
    return isDisabled;
  }

  public void setIsDisabled(String isDisabled) {
    this.isDisabled = isDisabled;
  }

  public String getVerdictInfo() {
    return verdictInfo;
  }

  public void setVerdictInfo(String verdictInfo) {
    this.verdictInfo = verdictInfo;
  }

  public String getIsPublic() {
    return isPublic;
  }

  public void setIsPublic(String isPublic) {
    this.isPublic = isPublic;
  }

  public String getBriefDescription() {
    return briefDescription;
  }

  public void setBriefDescription(String briefDescription) {
    this.briefDescription = briefDescription;
  }

  public String getMeetingType() {
    return meetingType;
  }

  public void setMeetingType(String meetingType) {
    this.meetingType = meetingType;
  }

  public String getMeetingId() {
    return meetingId;
  }

  public void setMeetingId(String meetingId) {
    this.meetingId = meetingId;
  }


  public Date getOpeningTime() {
    return openingTime;
  }

  public void setOpeningTime(Date openingTime) {
    this.openingTime = openingTime;
  }


  public Date getFinishDate() {
    return finishDate;
  }

  public void setFinishDate(Date finishDate) {
    this.finishDate = finishDate;
  }


  public String getPlace() {
    return place;
  }

  public void setPlace(String place) {
    this.place = place;
  }


  public String getMeetingTopic() {
    return meetingTopic;
  }

  public void setMeetingTopic(String meetingTopic) {
    this.meetingTopic = meetingTopic;
  }


  public String getMettingContent() {
    return mettingContent;
  }

  public void setMettingContent(String mettingContent) {
    this.mettingContent = mettingContent;
  }


  public String getMettingMinutes() {
    return mettingMinutes;
  }

  public void setMettingMinutes(String mettingMinutes) {
    this.mettingMinutes = mettingMinutes;
  }


  public String getMeetingVerdict() {
    return meetingVerdict;
  }

  public void setMeetingVerdict(String meetingVerdict) {
    this.meetingVerdict = meetingVerdict;
  }


  public String getSecretary() {
    return secretary;
  }

  public void setSecretary(String secretary) {
    this.secretary = secretary;
  }


  public String getSponsor() {
    return sponsor;
  }

  public void setSponsor(String sponsor) {
    this.sponsor = sponsor;
  }


  public String getPresenter() {
    return presenter;
  }

  public void setPresenter(String presenter) {
    this.presenter = presenter;
  }


  public String getMettingStatus() {
    return mettingStatus;
  }

  public void setMettingStatus(String mettingStatus) {
    this.mettingStatus = mettingStatus;
  }


  public long getAttendanceCount() {
    return attendanceCount;
  }

  public void setAttendanceCount(long attendanceCount) {
    this.attendanceCount = attendanceCount;
  }


  public long getActualAttendanceCount() {
    return actualAttendanceCount;
  }

  public void setActualAttendanceCount(long actualAttendanceCount) {
    this.actualAttendanceCount = actualAttendanceCount;
  }


  public long getFavorVote() {
    return favorVote;
  }

  public void setFavorVote(long favorVote) {
    this.favorVote = favorVote;
  }


  public long getNegativeVote() {
    return negativeVote;
  }

  public void setNegativeVote(long negativeVote) {
    this.negativeVote = negativeVote;
  }


  public long getAbstainVote() {
    return abstainVote;
  }

  public void setAbstainVote(long abstainVote) {
    this.abstainVote = abstainVote;
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

  public String getProcessStatus() {
    return processStatus;
  }

  public void setProcessStatus(String processStatus) {
    this.processStatus = processStatus;
  }
}
