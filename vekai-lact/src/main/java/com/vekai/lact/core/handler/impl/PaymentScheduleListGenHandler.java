package com.vekai.lact.core.handler.impl;

import com.vekai.lact.core.handler.DataObjectHandler;
import com.vekai.lact.model.LactLoan;
import com.vekai.lact.model.LactLoanSegment;
import com.vekai.lact.model.PaymentSchedule;
import com.vekai.lact.type.PaymentPeriod;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.ValidateKit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 根据期数把相应的还款计划表记录构建出来
 */
public class PaymentScheduleListGenHandler implements DataObjectHandler<LactLoan> {

    @Override
    public void handle(LactLoan loan) {
        List<LactLoanSegment> segments = loan.getSegmentList();
        for (LactLoanSegment segment : segments) {
//            //不是明示利率，则不需要计算执行利率
//            if (RateAppearMode.valueOf(segment.getRateAppearMode()) != RateAppearMode.Indication) continue;
            //如果传入参数中已经有还款计划表了，也不需要重新构建还款计划表
            boolean needBuild = (segment.getScheduleList() == null || segment.getScheduleList().size() == 0);
            if(!needBuild)continue;

            Date startDate = segment.getStartDate();        //本段还款开始日期
            Integer terms = segment.getSegmentTerms();      //本段部期数
            String repaymentPeriod = segment.getPaymentPeriod();
            ValidateKit.isTrue(terms>=1,"[本段总期数]不能小于1");
            ValidateKit.notNull(startDate,"分段[开始日期]不能为空");
            ValidateKit.notBlank(repaymentPeriod,"分段[还款周期]不能为空");

            PaymentPeriod rpmtPeriod = PaymentPeriod.valueOf(repaymentPeriod);

            List<PaymentSchedule> rpmtList = new ArrayList<PaymentSchedule>();

            for(int i=0;i<terms;i++){
                PaymentSchedule schedule = new PaymentSchedule(segment);

                Date settleDate = DateKit.plusMonths(startDate,rpmtPeriod.getMulti()*(i+1));
                schedule.setSettleDate(settleDate);
                //TODO 还款日需要修改
                schedule.setPaymentDate(settleDate);
                rpmtList.add(schedule);
            }
            //放入本段还款计划数据
            segment.setScheduleList(rpmtList);
        }
        //刷新期次序号
        refreshScheduleTimes(loan);


    }

    protected void refreshScheduleTimes(LactLoan loan){
        List<PaymentSchedule> totalScheduleList = new ArrayList<>();

        List<LactLoanSegment> segments = loan.getSegmentList();
        for (LactLoanSegment segment : segments) {
            totalScheduleList.addAll(segment.getScheduleList());
        }
        //重新计算期次，从1开始
        for(int i=0;i<totalScheduleList.size();i++){
            PaymentSchedule schedule = totalScheduleList.get(i);
            schedule.setTermTimes(i+1);
        }
    }
}
