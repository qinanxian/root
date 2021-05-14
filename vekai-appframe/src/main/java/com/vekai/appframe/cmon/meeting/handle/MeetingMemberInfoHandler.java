package com.vekai.appframe.cmon.meeting.handle;

import com.vekai.appframe.cmon.meeting.constant.MeetingConst;
import com.vekai.appframe.cmon.meeting.entity.Meeting;
import com.vekai.appframe.cmon.meeting.entity.MeetingMember;
import com.vekai.appframe.constant.AppframeConst;
import com.vekai.dataform.handler.impl.BeanDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.StringKit;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by luyu on 2018/1/31.
 */
@Component
public class MeetingMemberInfoHandler extends BeanDataOneHandler<MeetingMember> {

    public int save(DataForm dataForm, MeetingMember meetingMember) {
        String meetingMemberId = meetingMember.getMeetingMemberId();
        String meetingId = meetingMember.getMeetingId();
        String meetingSql = "SELECT * FROM CMON_MEETING where MEETING_ID =:meetingId";
        Meeting meeting = beanCruder.selectOne(Meeting.class,meetingSql,"meetingId",meetingId);
        String attendance = meetingMember.getAttendance();
        String voteAction = meetingMember.getVoteAction();

        long actualAttendanceCount = meeting.getActualAttendanceCount();
        if (StringKit.isBlank(meetingMemberId)) {
            meeting.setAttendanceCount(meeting.getAttendanceCount() + 1);
            if (AppframeConst.YES_NO_Y.equals(attendance)) {
                meeting.setActualAttendanceCount(actualAttendanceCount + 1);
            }
            updateVoteInfo(meeting, voteAction, 1);
        } else {
            String meetingMemberSql = "SELECT * FROM CMON_MEETING_MEMBER where meeting_Member_Id =:meetingMemberId";
            MeetingMember originalMeetingMember = beanCruder.selectOne(
                    MeetingMember.class,meetingMemberSql,"meetingMemberId",meetingMemberId);
            String originalAttendance = originalMeetingMember.getAttendance();
            String originalVoteAction = originalMeetingMember.getVoteAction();
            if (!Optional.ofNullable(attendance).orElseGet(() -> "")
                    .equals(Optional.ofNullable(originalAttendance).orElseGet(() -> ""))) {
                if (AppframeConst.YES_NO_Y.equals(attendance)) {
                    meeting.setActualAttendanceCount(actualAttendanceCount + 1);
                } else if (AppframeConst.YES_NO_N.equals(attendance) && originalAttendance != null) {
                    meeting.setActualAttendanceCount(actualAttendanceCount - 1);
                }
            }

            if (!Optional.ofNullable(voteAction).orElseGet(() -> "")
                    .equals(Optional.ofNullable(originalVoteAction).orElseGet(() -> ""))) {
                updateVoteInfo(meeting, voteAction, 1);
                updateVoteInfo(meeting, originalVoteAction, -1);
            }

        }
        beanCruder.save(meeting);
        return beanCruder.save(meetingMember);
    }

    private void updateVoteInfo(Meeting meeting, String voteAction, int i) {
        long abstainVote = meeting.getAbstainVote();
        long favorVote = meeting.getFavorVote();
        long negativeVote = meeting.getNegativeVote();
        if (null != voteAction) {
            switch (voteAction) {
                case MeetingConst.MEETING_VOTE_ACTION_ABSTAIN:
                    meeting.setAbstainVote(abstainVote + i);
                    break;
                case MeetingConst.MEETING_VOTE_ACTION_FAVOT:
                    meeting.setFavorVote(favorVote + i);
                    break;
                case MeetingConst.MEETING_VOTE_ACTION_NEGATIVE:
                    meeting.setNegativeVote(negativeVote + i);
                    break;
                default:
                    break;
            }
        }
    }
}
