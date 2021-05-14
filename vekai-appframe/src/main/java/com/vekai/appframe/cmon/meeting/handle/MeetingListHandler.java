package com.vekai.appframe.cmon.meeting.handle;

import com.vekai.appframe.cmon.meeting.entity.Meeting;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luyu on 2018/1/31.
 */
@Component
public class MeetingListHandler extends BeanDataListHandler<Meeting> {

    @Override
    @Transactional
    public Integer delete(DataForm dataForm, List<Meeting> dataList) {
        int result = 0;
        for (Meeting meeting : dataList) {
            String meetingId = meeting.getMeetingId();
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("meetingId", meetingId);
            String deleteMeetingMemberSql = "DELETE FROM CMON_MEETING_MEMBER WHERE MEETING_ID= :meetingId";
            result += beanCruder.execute(deleteMeetingMemberSql,paramMap);
            result += beanCruder.delete(meeting);
        }
        return result;
    }
}
