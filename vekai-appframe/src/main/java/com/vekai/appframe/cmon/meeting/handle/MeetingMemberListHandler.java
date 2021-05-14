package com.vekai.appframe.cmon.meeting.handle;

import cn.fisok.raw.lang.MapObject;
import com.vekai.appframe.cmon.meeting.constant.MeetingConst;
import com.vekai.appframe.cmon.meeting.entity.Meeting;
import com.vekai.appframe.constant.AppframeConst;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by luyu on 2018/1/31.
 */
@Component
public class MeetingMemberListHandler extends MapDataListHandler {

    @Autowired
    private BeanCruder beanCruder;

    @Override
    @Transactional
    public Integer delete(DataForm dataForm, List<MapObject> dataList) {
        int result = 0;
        for (MapObject mapObject : dataList) {
            String meetingId = (String) mapObject.get("meetingId");
            String attendance = (String) mapObject.get("attendance");
            String voteAction = (String) mapObject.get("voteAction");
            if (meetingId != null) {
                updateCmonMeeting(meetingId,attendance,voteAction);
            }
        }
        result += super.delete(dataForm,dataList);
        return result;
    }

    private void updateCmonMeeting(String meetingId, String attendance, String voteAction) {
        String meetingSql = "SELECT * FROM CMON_MEETING where MEETING_ID =:meetingId";
        Meeting meeting = beanCruder.selectOne(Meeting.class,meetingSql,"meetingId",meetingId);
        meeting.setAttendanceCount(meeting.getAttendanceCount() - 1);
        if (AppframeConst.YES_NO_Y.equals(attendance)) {
            meeting.setActualAttendanceCount(meeting.getActualAttendanceCount() - 1);
        }
        if (voteAction != null) {
            switch (voteAction) {
                case MeetingConst.MEETING_VOTE_ACTION_ABSTAIN:
                    meeting.setAbstainVote(meeting.getAbstainVote() - 1);
                    break;
                case MeetingConst.MEETING_VOTE_ACTION_FAVOT:
                    meeting.setFavorVote(meeting.getFavorVote() - 1);
                    break;
                case MeetingConst.MEETING_VOTE_ACTION_NEGATIVE:
                    meeting.setNegativeVote(meeting.getNegativeVote() - 1);
                    break;
                default:
                    break;
            }
        }
        beanCruder.save(meeting);
    }
}
