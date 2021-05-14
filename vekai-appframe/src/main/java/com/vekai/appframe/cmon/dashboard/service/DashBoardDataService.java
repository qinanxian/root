package com.vekai.appframe.cmon.dashboard.service;


import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.appframe.cmon.dashboard.model.ProjectDynamicDataModel;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.loadmd.SQLCollecter;
import cn.fisok.sqloy.loadmd.SQLTextLoader;
import com.vekai.workflow.model.enums.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: qyyao
 * @Description: dashBoard获取投前项目以及项目近年动态报告的相关数据
 * @Date: Created in 14:28 04/07/2018
 */

@Service
public class DashBoardDataService {

    @Autowired
    private SQLTextLoader sqlTextLoader;
    @Autowired
    private BeanCruder beanCruder;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static String res =
        "classpath:com/vekai/appframe/cmon/dashboard/dao/DashBoardDao.sql.md";


    public ProjectDynamicDataModel getNumOfPlan() {
        SQLCollecter collecter = sqlTextLoader.parse(res);
        ProjectDynamicDataModel dataModel = null;

        Date nowDate =DateKit.now();
        Date beginDayOfMonth = DateKit.getFirstDayOfMonth(nowDate);
        Date endDayOfMonth = DateKit.getLastDayOfMonth(nowDate);
        Date beginDayOfYear = DateKit.getBeginDayOfYear(nowDate);
        Date endDayOfYear = DateKit.getEndDayOfYear(nowDate);
        Integer number = 0;


        //本月会议数量
        String getNumOfMeetingSql = collecter.sql("getNumOfMeeting");
        number = beanCruder.selectCount(getNumOfMeetingSql,
            MapKit.mapOf("startTime", beginDayOfMonth, "endTime", endDayOfMonth));
        dataModel.setMeetingPM(number.toString());

        //本年会议数量
        number = beanCruder.selectCount(getNumOfMeetingSql,
            MapKit.mapOf("startTime", beginDayOfYear, "endTime", endDayOfYear));

        dataModel.setMeetingPY(number.toString());

        //本周立项通过审核的项目数量
        String ivstPlanSql = collecter.sql("getNumOfIvstPlan");
        number = beanCruder
            .selectCount(ivstPlanSql, MapKit.mapOf("procDekKey", "ivstPlanEstablishment", "status",
                ProcStatusEnum.FINISHED.getName(), "startTime", beginDayOfMonth, "endTime",
                endDayOfMonth));
        dataModel.setIvstPlanLoanPM(number.toString());

        //本年立项通过审核的项目数量
        number = beanCruder
            .selectCount(ivstPlanSql, MapKit.mapOf("procDekKey", "ivstPlanEstablishment", "status",
                ProcStatusEnum.FINISHED.getName(), "startTime", beginDayOfYear, "endTime",
                endDayOfYear));
        dataModel.setIvstPlanPY(number.toString());

        //本月尽调审核通过项目数量
        //同一项目下的尽调可能有多条数据
        String dueDiligenceSql = collecter.sql("getNumOfDueDiligence");

        number = beanCruder.selectCount(dueDiligenceSql,
            MapKit.mapOf("procDekKey", "dueDiligenceAndDecisionFlow", "status",
                ProcStatusEnum.FINISHED.getName(), "startTime", beginDayOfMonth, "endTime",
                endDayOfMonth));
        dataModel.setDueDiligencePM(number.toString());

        //本年尽调审核通过项目数量
        //同一项目下的尽调可能有多条数据
        number = beanCruder.selectCount(dueDiligenceSql,
            MapKit.mapOf("procDekKey", "dueDiligenceAndDecisionFlow", "status",
                ProcStatusEnum.FINISHED.getName(), "startTime", beginDayOfYear, "endTime",
                endDayOfYear));
        dataModel.setDueDiligencePM(number.toString());


        //本月项目下签约的主合同
        //本年项目下签约的主合同

        //本月项目下首次出资审核通过的数量
        String ivstPlanLoanSql = collecter.sql("getNumOfIvstPlanLoan");
        number = beanCruder
            .selectCount(ivstPlanLoanSql, MapKit.mapOf("procDekKey", "ivstPlanLoanFlow", "status",
                ProcStatusEnum.FINISHED.getName(), "startTime", beginDayOfMonth, "endTime",
                endDayOfMonth));
        dataModel.setIvstPlanLoanPM(number.toString());


        //本年项目下首次出资审核通过的数量
        number = beanCruder
            .selectCount(ivstPlanLoanSql, MapKit.mapOf("procDekKey", "ivstPlanLoanFlow", "status",
                ProcStatusEnum.FINISHED.getName(), "startTime", beginDayOfYear, "endTime",
                endDayOfYear));
        dataModel.setIvstPlanLoanPY(number.toString());

        return dataModel;
    }

    //转化成JSON格式的数据存储
    public MapObject getIvstPlanLoanNumber() throws JSONException {
        SQLCollecter collecter = sqlTextLoader.parse(res);

        MapObject map = new MapObject();

        String planPhaseSql = collecter.sql("getNumOfIvstPlanForPlanPhase");
        int currentYear = DateKit.getYear(new Date());
        //近十年已出资项目数量 10
        for (int i = 0; i < 10; i++) {
            currentYear -= i;
            Date startYearDate = DateKit.getStartMonthDate(currentYear, 1);
            Date endYearDate = DateKit.getEndMonthDate(currentYear, 12);
            int countOfPlan = beanCruder.selectCount(planPhaseSql,
                MapKit.mapOf("startTime", startYearDate, "endTime", endYearDate));
            map.putValue("numOfIvstPlan."+String.valueOf(currentYear),countOfPlan);
        }
        //近十年的出资项目数量

        //近十年已完成项目数量 10
        //已完成项目尚未完全确认(项目完全结束字段没有指定) 待开发

        //本年12个月每月的出资项目数量 12
        int needMonth = 1;
        for (int i = 0; i < 12; i++) {
            needMonth += i;
            Date startMonthDate = DateKit.getStartMonthDate(currentYear, needMonth);
            Date endMonthDate = DateKit.getEndMonthDate(currentYear, needMonth);
            int countOfPlan = beanCruder.selectCount(planPhaseSql,
                MapKit.mapOf("startTime", startMonthDate, "endTime", endMonthDate));
            map.putValue("numOfIvstplanPerMonth."+String.valueOf(needMonth),countOfPlan);
        }


        //已完成项目尚未完全确认(项目完全结束字段没有指定) 待开发
        //本年12个月每月的已完成项目数量 12

        //近十年已出资项目总金额 10
        //getSumMoneyOfIvstForPlanPhase
        planPhaseSql = collecter.sql("getSumMoneyOfIvstForPlanPhase");
        for (int i = 0; i < 10; i++) {
            currentYear -= i;
            Date startYearDate = DateKit.getStartMonthDate(currentYear, 1);
            Date endYearDate = DateKit.getEndMonthDate(currentYear, 12);
            int sumOfPlanMoney = jdbcTemplate
                .queryForObject(planPhaseSql, new Object[] {startYearDate, endYearDate},
                    Integer.class);
            map.putValue("sumOfPlanMoney."+String.valueOf(currentYear),sumOfPlanMoney);
        }
        //已完成项目尚未完全确认(项目完全结束字段没有指定) 待开发
        //近十年已完成项目总金额 10


        //本年12个月每月的出资项目总金额 12
        for (int i = 0; i < 12; i++) {
            needMonth += i;
            Date startMonthDate = DateKit.getStartMonthDate(currentYear, needMonth);
            Date endMonthDate = DateKit.getEndMonthDate(currentYear, needMonth);
            int sumOfPerMonthInPlan = jdbcTemplate
                .queryForObject(planPhaseSql, new Object[] {startMonthDate, endMonthDate},
                    Integer.class);
            map.putValue("sumOfPerMonthInPlan."+String.valueOf(needMonth),sumOfPerMonthInPlan);
        }
        //已完成项目尚未完全确认(项目完全结束字段没有指定) 待开发
        //本年12个月每月的已完成项目总金额 12

        return map;
    }
}