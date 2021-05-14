package com.vekai.appframe.holiday.service;

import com.vekai.appframe.holiday.model.Holiday;

import java.util.Date;
import java.util.List;


public interface HolidayAdminService {
    void initWeekend (String date);
    void updateToWorkday (Date date);
    void addLegal (List<Holiday> list);
    List<Holiday> getAllHolidays (String date);
    boolean isHoliday (Date date);
    Date getPreviousWorkday (Date date);
    Date getNextWorkday (Date date);

}
