package com.vekai.lact.core.payment;

import com.vekai.lact.model.LactLoanSegment;

/**
 * 还款方式计算器，针对不同的还款方式，使用不同的还款方式计算器来实现
 * 
 * @author 杨松<syang@amarsoft.com>
 * @date 2016年12月7日
 */
public interface PaymentCalculator<T extends LactLoanSegment> {

    /**
     * 计算每一段的还款计划
     * @param segment
     * @return
     */
    void calculate(T segment);

}
