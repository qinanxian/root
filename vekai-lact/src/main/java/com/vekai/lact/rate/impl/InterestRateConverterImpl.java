package com.vekai.lact.rate.impl;

import com.vekai.lact.exception.LactException;
import com.vekai.lact.rate.InterestRateConverter;
import com.vekai.lact.type.InterestCalcMode;
import cn.fisok.raw.kit.DateKit;

import java.util.Date;

public class InterestRateConverterImpl implements InterestRateConverter {
    protected InterestCalcMode interestCalcMode = InterestCalcMode.M;
    protected int yearDays = 360;

    public InterestRateConverterImpl(){
    }
    public InterestRateConverterImpl(InterestCalcMode interestCalcMode, int yearDays) {
        this.interestCalcMode = interestCalcMode;
        this.yearDays = yearDays;
    }

    public InterestCalcMode getInterestCalcMode() {
        return interestCalcMode;
    }
    public void setInterestCalcMode(InterestCalcMode interestCalcMode) {
        this.interestCalcMode = interestCalcMode;
    }
    public int getYearDays() {
        return yearDays;
    }
    public void setYearDays(int yearDays) {
        this.yearDays = yearDays;
    }

    /**
     * 把年利率转换为指定区间的利率
     * @param yearRate
     * @param startDate
     * @param endDate
     * @return
     */
    public Double getRangeRate(double yearRate, Date startDate, Date endDate) {
        if(interestCalcMode == InterestCalcMode.M){
            return getRangeRateByMonth(yearRate, startDate, endDate);
        }else if(interestCalcMode == InterestCalcMode.D){
            return getRangeRateByDay(yearRate, startDate, endDate);
        }else{
            throw new LactException("数据异常，计息模式既不是月(M),也不是日(D)");
        }
    }

    /**
     * 在月利率模式下，计算区间利率
     * @param yearRate
     * @param startDate
     * @param endDate
     * @return
     */
    protected Double getRangeRateByMonth(double yearRate, Date startDate, Date endDate){
        int monthCount = getMonths(startDate, endDate);
        return yearRate/12d * monthCount;
    }

    /**
     * 在日利率模式下，计算区间利率
     * @param yearRate
     * @param startDate
     * @param endDate
     * @return
     */
    protected Double getRangeRateByDay(double yearRate, Date startDate, Date endDate){
        int dayCount = DateKit.getRangeDays(startDate, endDate);
        //日利率 = 年利率 / 年计天数 * 天数
        return yearRate/(double)yearDays*dayCount ;
    }



    /**
     * 月份四舍五入<br/>
     * 1~44天 算1个月，45~74算2个月，75~104算三个月。。。。
     * @param startDate
     * @param endDate
     * @return
     */
    private static int getMonths(Date startDate, Date endDate) {
        int days = DateKit.getRangeDays(startDate, endDate);
        int monthCount = 0;
        if(days != 0){
            int a = days / 30;
            int b = days % 30;
            if(b > 0 && b < 15){
                b = 0;
            }
            if(b >= 15 && b < 30){
                b = 1;
            }
            monthCount = a + b;
            if(monthCount == 0){
                monthCount ++ ;
            }
        }
        return monthCount;
    }
}
