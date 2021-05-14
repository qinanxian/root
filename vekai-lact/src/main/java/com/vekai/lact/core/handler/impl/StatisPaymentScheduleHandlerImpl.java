package com.vekai.lact.core.handler.impl;

import com.vekai.lact.core.handler.DataObjectHandler;
import com.vekai.lact.model.LactLoan;
import com.vekai.lact.model.LactLoanSegment;
import com.vekai.lact.model.PaymentSchedule;
import com.vekai.lact.type.PaymentMode;
import cn.fisok.raw.kit.DateKit;

import java.util.Date;
import java.util.List;

/**
 * 把还款计划中的信息统计到段以及总表上来
 *
 * @author 杨松<syang@amarsoft.com>
 * @date 2018年06月01日
 */
public class StatisPaymentScheduleHandlerImpl implements DataObjectHandler<LactLoan> {
    @Override
    public void handle(LactLoan loan) {
        List<LactLoanSegment> segments = loan.getSegmentList();

        Double loanAmt = 0d;
        Date maxExpireDate = null;
        Double loanTotalPaymentAmt = 0d;
        Double loanTotalInterestAmt = 0d;
        int loanTotalPaymentTimes = 0;
        for(int i =0;i<segments.size();i++){
            LactLoanSegment segment = segments.get(i);
            Double totalPaymentAmt = 0d;
            Double totalInterestAmt = 0d;
            Double perTermPrincipal = null;
            Double perTermPaymentAmt = null;

            loanAmt += segment.getSegmentLoanAmt();

            List<PaymentSchedule> pmtList = segment.getScheduleList();
            for(int j=0;j<pmtList.size();j++){
                PaymentSchedule pmt = pmtList.get(j);
                totalPaymentAmt += pmt.getPaymentAmt();
                totalInterestAmt += pmt.getInterest();
                if(maxExpireDate == null) maxExpireDate = pmt.getSettleDate();
                if(DateKit.isAfter(maxExpireDate,pmt.getSettleDate())){
                    maxExpireDate = pmt.getSettleDate();
                }
            }
            segment.setTotalPaymentAmt(totalPaymentAmt);
            segment.setTotalInterestAmt(totalInterestAmt);
            //等额本金，记录每期还本金
            if(PaymentMode.valueOf(segment.getPaymentMode()) == PaymentMode.EqualPrincipal){
                perTermPrincipal = pmtList.get(0).getPrincipal();
                segment.setPerTermPrincipal(perTermPrincipal);
            }
            //等额本息，记录每期还款总额
            if(PaymentMode.valueOf(segment.getPaymentMode()) == PaymentMode.EqualLoan){
                perTermPaymentAmt = pmtList.get(0).getPaymentAmt();
                segment.setTotalPaymentAmt(perTermPaymentAmt);
            }

            loanTotalPaymentTimes += pmtList.size();
            loanTotalPaymentAmt += totalPaymentAmt;
            loanTotalInterestAmt += totalInterestAmt;
        }
        loan.setLoanAmt(loanAmt);
        loan.setExpiryDate(maxExpireDate);
        loan.setTotalPaymentAmt(loanTotalPaymentAmt);
        loan.setTotalInterestAmt(loanTotalInterestAmt);
        loan.setTotalPaymentTimes(loanTotalPaymentTimes);
    }
}
