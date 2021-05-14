package com.vekai.lact.rate;

import java.util.Date;

/**
 * 利率处理
 * 
 * @author 杨松<syang@amarsoft.com>
 * @date 2016年12月8日
 */
public interface InterestRateConverter {
    /**
     * 给出年利率，计算出区间利率
     * @param yearRate
     * @param startDate
     * @param endDate
     * @return
     */
    Double getRangeRate(double yearRate, Date startDate, Date endDate) ;

}
