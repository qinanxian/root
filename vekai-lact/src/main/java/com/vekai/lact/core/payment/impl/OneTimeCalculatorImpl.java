package com.vekai.lact.core.payment.impl;

import com.vekai.lact.core.payment.PaymentCalculator;
import com.vekai.lact.model.LactLoanSegment;
import com.vekai.lact.model.PaymentSchedule;
import cn.fisok.raw.kit.ValidateKit;

import java.util.List;

/**
 * 一次性还款（利随本清）
 */
public class OneTimeCalculatorImpl implements PaymentCalculator<LactLoanSegment> {
    @Override
    public void calculate(LactLoanSegment segment) {
        List<PaymentSchedule> pmtList = segment.getScheduleList();
        ValidateKit.notEmpty(pmtList,"还款计划不能为空");
        ValidateKit.isTrue(pmtList.size()==1,"一次性还本付息方式下，还款计划数只能是1");

        EachInterestCalculatorImpl other = new EachInterestCalculatorImpl();
        other.calculate(segment);
    }
}
