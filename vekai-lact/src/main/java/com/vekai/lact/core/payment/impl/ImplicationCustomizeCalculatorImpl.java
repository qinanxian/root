package com.vekai.lact.core.payment.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import com.vekai.lact.core.payment.PaymentCalculator;
import com.vekai.lact.exception.LactException;
import com.vekai.lact.model.LactLoan;
import com.vekai.lact.model.LactLoanSegment;
import com.vekai.lact.model.PaymentSchedule;
import com.vekai.lact.rate.InterestRateConverter;
import com.vekai.lact.rate.impl.InterestRateConverterImpl;
import com.vekai.lact.type.InterestCalcMode;
import cn.fisok.raw.kit.NumberKit;
import cn.fisok.raw.kit.ValidateKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 隐含利率计算处理
 *
 * @author 杨松<syang@amarsoft.com>
 * @date 2016年12月13日
 */
public class ImplicationCustomizeCalculatorImpl implements PaymentCalculator<LactLoanSegment> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private int decimalScale = 2;
    private int interestRateScale = 14;


    public void calculate(LactLoanSegment segment) {
        logger.trace(">>>>>>隐含利率还款计算<<<<<<");

        LactLoan loan = segment.getLoan();
        int yearDays = loan.getYearDays();
        ValidateKit.isTrue(yearDays >= 360, "[年基础天数]不能小于360");

//        List<LactLoanSegment> segments = loan.getSegmentList();
//        for (LactLoanSegment segment : segments) {

        String interestCalcMode = segment.getInterestCalcMode();
        Integer lockTimes = segment.getLoan().getPaidTerms();
        Double residual = segment.getLoan().getResidualAmt();
        Double loanAmt = segment.getSegmentLoanAmt();
        if (lockTimes == null) lockTimes = 0;
        if (residual == null) residual = 0d;
        if (loanAmt == null) loanAmt = 0d;

        ValidateKit.notBlank(interestCalcMode, "分段[计息方式]不能为空");

        InterestCalcMode calcMode = InterestCalcMode.valueOf(interestCalcMode);
        InterestRateConverter rateConverter = new InterestRateConverterImpl(calcMode, yearDays);

        List<PaymentSchedule> pmtList = segment.getScheduleList();

        double yearRate = getYearRate(rateConverter, segment, pmtList);
        yearRate = NumberKit.round(yearRate, 10);
        segment.setInterestRate(yearRate);
        setPmtRate(rateConverter, segment, pmtList, yearRate);

        PrincipalAndInterestSpliter spliter = new PrincipalAndInterestSpliter();
        spliter.setDecimalScale(decimalScale);
        spliter.setResidual(residual);
        spliter.setInterestRateScale(interestRateScale);
        spliter.split(pmtList, loanAmt, lockTimes);


    }


    private void setPmtRate(InterestRateConverter rateConverter, LactLoanSegment segment, List<PaymentSchedule> pmtList, double yearRate) {
        Date curDate = segment.getStartDate();
        Integer lockTimes = segment.getLoan().getPaidTerms();
        if (lockTimes == null) lockTimes = 0;

        if (lockTimes > 0) {
            curDate = pmtList.get(lockTimes - 1).getSettleDate();
        }
        for (int i = lockTimes; i < pmtList.size(); i++) {
            PaymentSchedule pmt = pmtList.get(i);
            double execRate = rateConverter.getRangeRate(yearRate, curDate, pmt.getSettleDate());
            pmt.setInterestRate(execRate);
            curDate = pmt.getSettleDate();
        }
    }


    private double getYearRate(InterestRateConverter rateConverter, LactLoanSegment segment, List<PaymentSchedule> pmtList) {
        double yearRate = 5d;
        double guessMax = 10d;
        double guessMin = 0d;
        double guessCount = 100;
        for (int i = 0; i < guessCount; i++) {
            int r = lookupRate(rateConverter, segment, pmtList, yearRate);
            if (yearRate == guessMax || yearRate == guessMin) {
                double a = Math.abs(getRemaionCost(rateConverter, segment, pmtList, guessMax));
                double b = Math.abs(getRemaionCost(rateConverter, segment, pmtList, guessMin));
                yearRate = guessMax;
                if (b <= a) {
                    yearRate = guessMin;
                }
                r = 0;
            }
            if (r == 1) {
                guessMin = NumberKit.round(yearRate, interestRateScale);
                yearRate = NumberKit.round((guessMax + yearRate) / 2d, interestRateScale);
                logger.trace(MessageFormat.format("逼近值过小,值:{0,number,#.################},区间[{1,number,#.################},{2,number,#.################}]", yearRate, guessMin, guessMax));
            } else if (r == -1) {
                guessMax = NumberKit.round(yearRate, interestRateScale);
                yearRate = NumberKit.round((yearRate + guessMin) / 2d, interestRateScale);
                logger.trace(MessageFormat.format("逼近值过大,值:{0,number,#.################},区间[{1,number,#.################},{2,number,#.################}]", yearRate, guessMin, guessMax));
            } else if (r == 0) {
                logger.trace(MessageFormat.format("找到结果:{0,number,#.################}，逼近次数:{1}", yearRate, i + 1));
                break;
            }
            if (i == guessCount - 1) {
                logger.error(MessageFormat.format("隐含利率逼近法求解，无解。最后值：{0,number,#.################}", yearRate));
                throw new LactException(MessageFormat.format("隐含利率逼近法求解，无解。最后值：{0,number,#.################}", yearRate));
            }
        }
        return yearRate;
    }


    private int lookupRate(InterestRateConverter rateConverter, LactLoanSegment segment, List<PaymentSchedule> pmtList, double yearRate) {
        Date curDate = segment.getStartDate();
        Integer lockTimes = segment.getLoan().getPaidTerms();
        Double remainCost = segment.getSegmentLoanAmt();
        Double residual = segment.getLoan().getResidualAmt();
        if (lockTimes == null) lockTimes = 0;
        if (remainCost == null) remainCost = 0d;
        if (residual == null) residual = 0d;

        boolean residualGenInterest = "Y".equals(segment.getLoan().getResidualGenInterest());

        if (lockTimes > 0) {
            curDate = pmtList.get(lockTimes - 1).getSettleDate();
            remainCost = pmtList.get(lockTimes - 1).getRemainCost();
        }
        for (int i = 0; i < pmtList.size(); i++) {
            PaymentSchedule pmt = pmtList.get(i);
            if (pmt.getTermTimes() <= lockTimes) continue;

            Date pmtDate = pmt.getSettleDate();
            double totalAmt = scaleRemain(pmt.getPaymentAmt());
            double myRate = rateConverter.getRangeRate(yearRate, curDate, pmtDate);
            myRate = BigDecimal.valueOf(myRate).setScale(interestRateScale, RoundingMode.HALF_UP).doubleValue();
            double interest = 0d;
            if (residualGenInterest) {
                interest = scaleRemain(remainCost * myRate);
            } else {
                interest = scaleRemain((remainCost - residual) * myRate);
            }
            double capital = scaleRemain(totalAmt - interest);
            remainCost = scaleRemain(remainCost - capital);
            curDate = pmtDate;
        }
        double calcRemainCost = remainCost;
        if (residualGenInterest) {
            calcRemainCost = remainCost - residual;
        }
        if (calcRemainCost == 0d) {
            logger.trace("remainCost:" + remainCost);
            return 0;
        }
        if (calcRemainCost < 0d)
            return 1; // 如果所还本金小于0或剩余本金小于0，则直接结束
        return -1;
    }

    private double getRemaionCost(InterestRateConverter rateConverter, LactLoanSegment segment, List<PaymentSchedule> pmtList, double yearRate) {
        Date curDate = segment.getStartDate();
        Double remainCost = segment.getSegmentLoanAmt();
        Integer lockTimes = segment.getLoan().getPaidTerms();
        Double residual = segment.getLoan().getResidualAmt();
        if(remainCost == null) remainCost = 0d;
        if(lockTimes == null) lockTimes = 0;
        if(residual == null) residual = 0d;

        boolean residualGenInterest = "Y".equals(segment.getLoan().getResidualGenInterest());


        if (lockTimes > 0) {
            curDate = pmtList.get(lockTimes - 1).getSettleDate();
            remainCost = pmtList.get(lockTimes - 1).getRemainCost();
        }
        for (int i = 0; i < pmtList.size(); i++) {
            PaymentSchedule pmt = pmtList.get(i);
            if (pmt.getTermTimes() <= lockTimes) continue;

            Date pmtDate = pmt.getSettleDate();
            double totalAmt = scaleRemain(pmt.getPaymentAmt());
            double myRate = rateConverter.getRangeRate(yearRate, curDate, pmtDate);
            myRate = BigDecimal.valueOf(myRate).setScale(interestRateScale, RoundingMode.HALF_UP).doubleValue();
            double interest = 0d;
            if (residualGenInterest) {
                interest = scaleRemain(remainCost * myRate);
            } else {
                interest = scaleRemain((remainCost - residual) * myRate);
            }
            double capital = scaleRemain(totalAmt - interest);
            remainCost = scaleRemain(remainCost - capital);
            curDate = pmtDate;
        }
        return remainCost;
    }


    /**
     * 小数位数
     *
     * @return
     */
    public int getDecimalScale() {
        return decimalScale;
    }

    /**
     * 小数位数
     *
     * @param decimalScale
     */
    public void setDecimalScale(int decimalScale) {
        this.decimalScale = decimalScale;
    }

    /**
     * 利率保留小数位数
     *
     * @return
     */
    public int getInterestRateScale() {
        return interestRateScale;
    }

    /**
     * 利率保留小数位数
     *
     * @param interestRateScale
     */
    public void setInterestRateScale(int interestRateScale) {
        this.interestRateScale = interestRateScale;
    }

    protected double scaleRemain(double v) {
        return NumberKit.round(v, decimalScale);
    }
}
