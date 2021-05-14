package com.vekai.lact.core.handler.impl;

import com.vekai.lact.core.handler.DataObjectHandler;
import com.vekai.lact.rate.InterestRateConverter;
import com.vekai.lact.model.LactLoan;
import com.vekai.lact.model.LactLoanSegment;
import com.vekai.lact.model.PaymentSchedule;
import com.vekai.lact.rate.impl.InterestRateConverterImpl;
import com.vekai.lact.type.InterestCalcMode;
import com.vekai.lact.type.RateAppearMode;
import cn.fisok.raw.kit.ValidateKit;

import java.util.Date;
import java.util.List;

/**
 * 计算每条还款记录对应的区间实际执行利率
 */
public class PaymentScheduleInterestRateCalcHandler implements DataObjectHandler<LactLoan> {
    @Override
    public void handle(LactLoan dataObject) {
        List<LactLoanSegment> segments = dataObject.getSegmentList();

        Integer lockTimes = dataObject.getPaidTerms();//锁期（一般用于已经有部分还款核销之后，重新计算的情况）
        Integer yearDays = dataObject.getYearDays();
        if(lockTimes == null)lockTimes = 0;
        if(yearDays == null)yearDays = 0;

        ValidateKit.isTrue(yearDays>=360,"[年基础天数]不能小于360");

        for(LactLoanSegment segment : segments){
            //不是明示利率，则不需要计算执行利率
            if (RateAppearMode.valueOf(segment.getRateAppearMode()) != RateAppearMode.Indication) continue;

            String interestCalcMode = segment.getInterestCalcMode();
            Date startDate = segment.getStartDate();        //本段还款开始日期
            Double rate = segment.getInterestRate();        //本段执行利率

            ValidateKit.notBlank(interestCalcMode,"分段[计息方式]不能为空");
            ValidateKit.notNull(startDate,"分段[开始日期]不能为空");
            ValidateKit.isTrue(rate>0,"分段[执行利率]不能为空");

            List<PaymentSchedule> schedules = segment.getScheduleList();

            Date preDate = startDate;
            for (int i = 0; i < schedules.size(); i++) {
                PaymentSchedule schedule = schedules.get(i);
                Date curDate = schedule.getSettleDate();

                ValidateKit.notNull(curDate,"还款明细表[还款日期]不能为空");
                if(schedule.getTermTimes()<=lockTimes)continue; //锁期的跳过

                InterestCalcMode calcMode = InterestCalcMode.valueOf(interestCalcMode);
                InterestRateConverter rateConverter = new InterestRateConverterImpl(calcMode,yearDays);
                Double rangeRate = rateConverter.getRangeRate(rate,preDate,curDate);

                schedule.setInterestRate(rangeRate);    //置入本段实际执行利率

                preDate = curDate;
            }
        }


    }
}
