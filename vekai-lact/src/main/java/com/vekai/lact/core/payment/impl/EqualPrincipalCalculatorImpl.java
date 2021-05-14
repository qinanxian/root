package com.vekai.lact.core.payment.impl;

import java.util.List;

import com.vekai.lact.core.payment.PaymentCalculator;
import com.vekai.lact.model.LactLoan;
import com.vekai.lact.model.LactLoanSegment;
import com.vekai.lact.model.PaymentSchedule;
import cn.fisok.raw.kit.NumberKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 等额本金（兼容锁期）
 *
 * @author 杨松<syang@amarsoft.com>
 * @date 2016年12月7日
 */
public class EqualPrincipalCalculatorImpl implements PaymentCalculator<LactLoanSegment> {
    private int decimalScale = 2;
    private int interestRateScale = 14;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void calculate(LactLoanSegment segment) {
        logger.trace(">>>>>>等额本金计算<<<<<<");
        LactLoan loan = segment.getLoan();

        Integer terms = segment.getSegmentTerms();
        boolean residualGenInterest = "Y".equals(loan.getResidualGenInterest());                        //余值是否计息
        List<PaymentSchedule> pmtList = segment.getScheduleList();

        Double remainCost = loan.getLoanAmt();// 未还本金
        Double residual = loan.getResidualAmt();
        Integer lockTimes = loan.getPaidTerms();
        if(remainCost == null) remainCost = 0d;
        if(residual == null) residual = 0d;
        if(lockTimes == null) lockTimes = 0;


        for (int i = 0; i < lockTimes; i++) {
            remainCost = NumberKit.round(remainCost - pmtList.get(i).getPrincipal(), decimalScale);
        }
        double everPayCapital = 0d;
        if (remainCost != 0) {
            everPayCapital = NumberKit.round(remainCost / (terms - lockTimes), decimalScale);
        }
        if (residualGenInterest && remainCost != 0) {
            everPayCapital = NumberKit.round((remainCost - residual) / (terms - lockTimes), decimalScale);
        }
//            segment.setEverPayCapital(everPayCapital);
        segment.setPerTermPrincipal(everPayCapital);

        double payInterest;// 每期利息
        for (int i = 0; i < pmtList.size(); i++) {
            PaymentSchedule pmt = pmtList.get(i);
            if (pmt.getTermTimes() <= lockTimes) continue;

            payInterest = NumberKit.round(remainCost * pmt.getInterestRate(), 6); // 每期利息
            //先保留6位 再保留2位  反例：5053.124999999999175，直接保留2位是12 ，先6再2是13
            payInterest = NumberKit.round(payInterest, decimalScale);
            if (i == pmtList.size() - 1) {
                everPayCapital = remainCost;
                if (residualGenInterest)
                    everPayCapital -= residual;
            }
            remainCost = NumberKit.round(remainCost - everPayCapital, decimalScale); // 未还本金
            pmt.setPrincipal(everPayCapital); // 每期本金
            pmt.setRemainCost(remainCost);
            pmt.setInterest(payInterest); // 每期利息
            pmt.setPaymentAmt(NumberKit.round(payInterest + everPayCapital, decimalScale)); // 每期租金
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

    /**
     * 利率小数点保留位数
     *
     * @return
     */
    public int getInterestRateScale() {
        return interestRateScale;
    }

    /**
     * 利率小数点保留位数
     *
     * @param interestRateScale
     */
    public void setInterestRateScale(int interestRateScale) {
        this.interestRateScale = interestRateScale;
    }
}
