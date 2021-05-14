package com.vekai.lact.core.payment.impl;

import com.vekai.lact.core.payment.PaymentCalculator;
import com.vekai.lact.model.LactLoanSegment;

/**
 * 确定每期还款金额(明示利率),每期租金和还款日都已知
 */
public class DeterminateLoanCalculatorImpl implements PaymentCalculator<LactLoanSegment> {
    @Override
    public void calculate(LactLoanSegment segment) {
    }
}
