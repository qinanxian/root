package com.vekai.lact.core.payment.impl;

import com.vekai.lact.core.payment.PaymentCalculator;
import com.vekai.lact.model.LactLoanSegment;
import com.vekai.lact.model.PaymentSchedule;
import cn.fisok.raw.kit.NumberKit;
import cn.fisok.raw.kit.ValidateKit;

import java.util.List;

/**
 * 按期付息，到期还本的计算方式
 */
public class EachInterestCalculatorImpl implements PaymentCalculator<LactLoanSegment> {
    private int decimalScale = 2;

    @Override
    public void calculate(LactLoanSegment segment) {
        Double segmentLoanAmt = segment.getSegmentLoanAmt();   //本段债权金额
        if(segmentLoanAmt == null) segmentLoanAmt = 0d;
        ValidateKit.isTrue(segmentLoanAmt>0,"分段债权金额必需大于0");

        Double loanAmt = segmentLoanAmt;
        List<PaymentSchedule> pmtList = segment.getScheduleList();
        for(int i=0;i<pmtList.size();i++){
            PaymentSchedule pmtItem = pmtList.get(i);

            double rate = pmtItem.getInterestRate();
            double principal = 0d;
            double interest = NumberKit.round(loanAmt * rate,decimalScale);
            double remainCost = loanAmt;
            //最末一期还本息和
            if(i == pmtList.size()-1){
                principal =  remainCost;
                remainCost = 0d;
            }
            double paymentAmt = principal + interest;

            pmtItem.setPrincipal(principal);
            pmtItem.setInterest(interest);
            pmtItem.setPaymentAmt(paymentAmt);
            pmtItem.setRemainCost(remainCost);
        }

    }
}
