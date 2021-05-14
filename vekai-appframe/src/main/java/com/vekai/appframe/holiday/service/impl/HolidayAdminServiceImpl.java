package com.vekai.appframe.holiday.service.impl;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.appframe.holiday.model.Holiday;
import com.vekai.appframe.holiday.service.HolidayAdminService;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class HolidayAdminServiceImpl implements HolidayAdminService{

    @Autowired
    BeanCruder beanCruder;

    public void clearWeekend(String date) {
        Integer year = DateKit.getYear(DateKit.parse(date));
        Date start = DateKit.parse(year + "-1-1 00:00:00");
        Date end = DateKit.parse(year + "-12-31 23:59:59");
        beanCruder.execute("delete from FNAT_HOLIDAY where HOLIDAY_DATE between :start and :end",
                MapKit.mapOf("start", start, "end", end));
    }
    @Override
    public void initWeekend(String date) {
        clearWeekend(date);
        List<Holiday> list = new ArrayList<>();
        Date startDate = null;
        if (null == date) {
            startDate = DateKit.now();
        } else {
            startDate = DateKit.parse(date);
        }
        int year = DateKit.getYear(startDate);
        boolean leapYear = (year%4==0&&year%100!=0||year%400==0);
        int days = leapYear?366:365;
        for(int i=0;i<days;i++){
            Date incDate = DateKit.plusDays(DateKit.parse(year + "-1-1 00:00:00"),i);
            int dayOfWeek = DateKit.getDayOfWeek(incDate);
            if(dayOfWeek == 6 || dayOfWeek == 7){
                Holiday holiday = new Holiday();
                holiday.setHolidayDate(DateKit.getOnlyYMD(incDate));
                holiday.setHolidayType("weekend");
                holiday.setHolidayName("双休日");
                holiday.setNote("双休日");
                list.add(holiday);
            }
        }
        beanCruder.save(list);
    }

    @Override
    public void updateToWorkday(Date date) {
        Date argDate = DateKit.getOnlyYMD(date);
        beanCruder.execute("delete from FNAT_HOLIDAY where HOLIDAY_DATE=:date", MapKit.mapOf("date",argDate));
    }

    @Override
    public void addLegal(List<Holiday> holiday) {
        for (Holiday hd : holiday){
            hd.setHolidayDate(DateKit.getOnlyYMD(hd.getHolidayDate()));
        }
        beanCruder.save(holiday);
    }

    @Override
    public List<Holiday> getAllHolidays(String date) {
        if (StringKit.isBlank(date)) {
            return beanCruder.selectList(Holiday.class,
                    "select HOLIDAY_DATE from FNAT_HOLIDAY");
        } else {
            Integer year = DateKit.getYear(DateKit.parse(date));
            Date start = (DateKit.parse(year + "-1-1 00:00:00"));
            Date end = (DateKit.parse(year + "-12-31 23:59:59"));
            return beanCruder.selectList(Holiday.class,
                    "select * from FNAT_HOLIDAY where HOLIDAY_DATE between :start and :end",
                    MapKit.mapOf("start", start, "end", end));
        }
    }

    /**
     * 判断一个日期是否是节假日
     * @param date 日期字符串
     * @return boolean值
     */
    @Override
    public boolean isHoliday(Date date) {
        String sql = "select * from FNAT_HOLIDAY where HOLIDAY_DATE = :date";
        Holiday holiday = beanCruder.selectOne(Holiday.class, sql, MapKit.mapOf("date", date));
        if (holiday == null)
            return false;
        else
            return true;
    }

    /**
     * 给定日期之前的最后一个工作日，如果本身是工作日，就返回给定的日期
     * @param date 日期字符串
     * @return Date
     */
    @Override
    public Date getPreviousWorkday(Date date) {
        Date minusDate = date;
        for (int i=1; isHoliday(minusDate); i++) {
            minusDate = DateKit.minusDays(date, i);
        }
        return minusDate;
    }

    /**
     * 给定日期之后的第一个工作日，如果本身是工作日，就返回给定的日期
     * @param date 日期字符串
     * @return Date
     */
    @Override
    public Date getNextWorkday(Date date) {
        Date plusDate = date;
        for (int i=1; isHoliday(plusDate); i++) {
            plusDate = DateKit.plusDays(date, i);
        }
        return plusDate;
    }
}
