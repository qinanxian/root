package com.vekai.lact.core.payment.impl;

import com.vekai.lact.core.payment.PaymentCalculator;
import com.vekai.lact.model.LactLoan;
import com.vekai.lact.model.LactLoanSegment;
import com.vekai.lact.model.PaymentSchedule;
import cn.fisok.raw.kit.NumberKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 确定本金。<br/>
 * 等期或者不等期,每期本金和还款日都已知
 */
public class DeterminatePrincipalCalculatorImpl implements PaymentCalculator<LactLoanSegment> {


    private int decimalScale = 2;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public void calculate(LactLoanSegment segment) {
        logger.trace(">>>>>>确定本金计算<<<<<<");

        LactLoan loan = segment.getLoan();

        Double remainCost = loan.getLoanAmt();// 未还本金
        Integer lockTimes = loan.getPaidTerms();
        if(remainCost == null) remainCost = 0d;
        if(lockTimes == null) lockTimes = 0;

        //把锁期部分的未还本金重新计算
        List<PaymentSchedule> scheduleList = segment.getScheduleList();
        for(PaymentSchedule schedule : scheduleList){
            if(schedule.getTermTimes()<=lockTimes){
                remainCost = NumberKit.round(remainCost-schedule.getPrincipal(),decimalScale);
            }
        }

        for(PaymentSchedule pmt : scheduleList){
            //如果剩余本金为0了，则后面的再了不需要还了
            if(remainCost <= 0){
                pmt.setPrincipal(0d);
                pmt.setRemainCost(0d);
                pmt.setInterest(0d); // 每期利息
                pmt.setPaymentAmt(0d); // 每期金额
                continue;
            }
            double principal = pmt.getPrincipal();// 每期本金已知
            //如果剩余本金小于还本金额，则本次结清掉
            if(principal >= remainCost){
                principal = remainCost;
            }
            double interest = NumberKit.round(remainCost * pmt.getInterestRate(),decimalScale); // 每期利息
            remainCost = NumberKit.round(remainCost-principal,decimalScale);
//            remainCost = NumberKit.round(remainCost - everPayCapital,decimalScale); // 未还本金

            pmt.setPrincipal(principal);
            pmt.setInterest(interest); // 每期利息
            pmt.setRemainCost(remainCost);
            pmt.setPaymentAmt(NumberKit.round(interest + principal,decimalScale)); // 每期金额
        }
    }
    /**
     * 本金利息小数点保留位数
     *
     * @return
     */
    public int getDecimalScale() {
        return decimalScale;
    }

    /**
     * 本金利息小数点保留位数
     *
     * @param decimalScale
     */
    public void setDecimalScale(int decimalScale) {
        this.decimalScale = decimalScale;
    }
}
