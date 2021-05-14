package com.vekai.lact.core.handler.impl;

import com.vekai.lact.core.payment.PaymentCalculator;
import com.vekai.lact.core.handler.DataObjectHandler;
import com.vekai.lact.exception.LactException;
import com.vekai.lact.model.LactLoan;
import com.vekai.lact.model.LactLoanSegment;
import com.vekai.lact.model.PaymentSchedule;
import com.vekai.lact.type.RateAppearMode;
import cn.fisok.raw.kit.NumberKit;
import cn.fisok.raw.kit.ValidateKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 还款计划表核算
 */
public class PaymentScheduleCalcHandlerImpl implements DataObjectHandler<LactLoan> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected Map<String, PaymentCalculator> calculators = null;
    protected int decimalScale = 2;

    public Map<String, PaymentCalculator> getCalculators() {
        return calculators;
    }

    public void setCalculators(Map<String, PaymentCalculator> calculators) {
        this.calculators = calculators;
    }

    public int getDecimalScale() {
        return decimalScale;
    }

    public void setDecimalScale(int decimalScale) {
        this.decimalScale = decimalScale;
    }

    public PaymentCalculator getRepaymentCalculator(String paymentCalcMethod) {
        if(calculators == null){
            throw new LactException("系统，还款方式计算器无具体实现逻辑");
        }

        PaymentCalculator calculator = calculators.get(paymentCalcMethod);
        if (calculator == null) {
            throw new LactException("数据错误，还款方式[{0}]没有对应的实现", paymentCalcMethod);
        }
        return calculator;
    }

    @Override
    public void handle(LactLoan dataObject) {
        logger.trace("---------计算还款计划----------");
        List<LactLoanSegment> segments = dataObject.getSegmentList();
        for (LactLoanSegment segment : segments) {
            String rpmtMethod = segment.getPaymentMode();
            ValidateKit.notBlank(rpmtMethod, "数据错误，还款段[还款方式]为空。{0}/{1}", dataObject.getLoanId(), segment.getLoanSegmentId());

            if (RateAppearMode.valueOf(segment.getRateAppearMode()) == RateAppearMode.Implication) {  //隐含利率
                rpmtMethod = "ImplicationCustomize";
            } else if (rpmtMethod.equals("EvenCapitalNotEqual")) {     //不等额本金
                rpmtMethod = "CapitalKnown";//确定本金
            }

            PaymentCalculator calculator = getRepaymentCalculator(rpmtMethod);
            ValidateKit.notNull(calculator, "数据错误，还款方式:{0}没有应的处理逻辑", rpmtMethod);

//            checkLoanData(dataObject);
            calculator.calculate(segment);

        }
    }

    private void checkLoanData(LactLoan dataObject) {
        List<LactLoanSegment> segments = dataObject.getSegmentList();
        double residual = dataObject.getResidualAmt();

        for (LactLoanSegment segment : segments) {
            List<PaymentSchedule> pmtList = segment.getScheduleList();

            double remainCost = segment.getSegmentLoanAmt();// 未还本金
            Integer terms = segment.getSegmentTerms();
            int lockTimes = dataObject.getPaidTerms();
            for (int i = 0; i < lockTimes; i++) {
                remainCost = NumberKit.round(remainCost - pmtList.get(i).getPrincipal(), decimalScale);
                pmtList.get(lockTimes - 1).setRemainCost(remainCost);
            }
            if (remainCost < residual) {
                throw new LactException("非法的测算表数据,未还本金{0}，资产余值{1}", remainCost, residual);
            }
            if (remainCost - residual != 0d && terms == lockTimes) {
                throw new LactException("非法的测算表数据,未还本金{0}，资产余值{1}，剩余期数{2}", remainCost, residual, terms - lockTimes);
            }
            if (remainCost - residual == 0d && terms != lockTimes) {
                throw new LactException("非法的测算表数据,未还本金{0}，资产余值{1}，剩余期数{2}", remainCost, residual, terms - lockTimes);
            }
        }
    }
}
