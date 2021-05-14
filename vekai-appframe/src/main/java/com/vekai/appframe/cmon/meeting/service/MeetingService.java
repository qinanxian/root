package com.vekai.appframe.cmon.meeting.service;

import com.vekai.appframe.cmon.meeting.entity.Meeting;
import com.vekai.appframe.cmon.meeting.entity.MeetingMember;
import cn.fisok.raw.lang.BizException;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by luyu on 2018/3/13.
 */
@Service
public class MeetingService {

    @Autowired
    private BeanCruder beanCruder;

    public Meeting getMeetingByMeetingId(String meetingId) {
        String meetingSql = "select * from CMON_MEETING where meeting_id =:meetingId";
        Meeting meeting = beanCruder.selectOne(Meeting.class, meetingSql, "meetingId", meetingId);
        Optional.ofNullable(meeting)
            .orElseThrow(() -> new BizException("会议信息不存在，id ：" + meetingId));
        return meeting;
    }

    public List<MeetingMember> getMeetingsByMeetingId(String meetingId) {
        String sql = "select * from CMON_MEETING_MEMBER where MEETING_ID =:meetingId";
        return beanCruder.selectList(MeetingMember.class, sql, "meetingId", meetingId);
    }

    public MeetingMember getMeetingMemberByMeetingIdAndId(String meetingId, String id) {
        String sql =
            "select * from CMON_MEETING_MEMBER where MEETING_ID =:meetingId and MEMBER_ID=:id";
        return beanCruder.selectOne(MeetingMember.class, sql, "meetingId", meetingId, "id", id);
    }

    public Meeting getMeetingByPlanIdAndIsDisabled(String planId) {
        String meetingSql =
            "select * from CMON_MEETING where PLAN_ID =:planId and IS_DISABLED=:isDisabled";
        Meeting meeting = beanCruder
            .selectOne(Meeting.class, meetingSql, "planId", planId, "isDisabled", "Y");
        return meeting;
    }
}
