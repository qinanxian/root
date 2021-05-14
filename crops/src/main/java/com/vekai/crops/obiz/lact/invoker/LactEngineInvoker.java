package com.vekai.crops.obiz.lact.invoker;

import cn.fisok.raw.lang.MapObject;
import com.vekai.lact.core.LactEngine;
import com.vekai.lact.core.transaction.LactTransaction;
import com.vekai.lact.model.LactLoan;
import com.vekai.lact.model.LactLoanSegment;
import com.vekai.lact.type.*;
import cn.fisok.raw.kit.DateKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 核算引擎调用器
 */
@Service
public class LactEngineInvoker {
    @Autowired
    protected LactEngine engine;

    /**
     * 还款计划计算交易号
     */
    public static final String CALC_PAYMENT_SCHEDULE_LIST_TRANS_CODE = "LACTS10";

    public LactLoan invokeCalcPaymentScheduleList(LactLoanCalcParam param) {

        //*************** 1. 取参数 ***************
        //金额
        Double loanAmt = param.loanAmt;
        //期限
        Integer terms = param.terms;
        Integer termDay = param.termDay;
        Date startDate = param.startDate;
        //利率
        RateAppearMode rateAppearMode = param.rateAppearMode;
        String rateFloatType = param.rateFloatType.toString();
        Double rateFloat = param.rateFloat;
        BaseRateType baseRateType = param.baseRateType;
        //还款及计息
        PaymentMode paymentMode = param.paymentMode;
        PaymentPeriod paymentPeriod = param.paymentPeriod;
        InterestCalcMode interestCalcMode = param.interestCalcMode;
        PayPointTime payPointTime = param.payPointTime;

        //*************** 2. 原始数据加工处理 ***************
        int termMonth = terms * paymentPeriod.getMulti();
        Date expiryDate = DateKit.plusMonths(startDate, termMonth);
        if (termDay != null && termDay > 0) {
            expiryDate = DateKit.plusDays(expiryDate, termDay);
        }

        //*************** 3. 组装对象 ***************
        LactLoan loan = new LactLoan();
        LactLoanSegment segment = new LactLoanSegment(loan);

        loan.setTotalAmt(loanAmt);
        loan.setFirstPayAmt(0d);
        loan.setLoanAmt(loan.getTotalAmt() - loan.getFirstPayAmt());
        loan.setTermMonth(termMonth);
        loan.setTerms(terms);
        loan.setStartDate(startDate);
        loan.setExpiryDate(expiryDate);
        loan.setPayPointTime(payPointTime.toString());
        loan.setOnlyOneSegment(segment);//填充分段信息（这部分信息才参与计算）

        segment.setSegmentLoanAmt(loanAmt);
        segment.setStartDate(startDate);
        segment.setTermMonth(terms * paymentPeriod.getMulti());
        segment.setRateAppearMode(rateAppearMode.toString());
        segment.setBaseRateType(baseRateType.toString());
        segment.setRateFloatType(rateFloatType);
        segment.setRateFloat(rateFloat);
        segment.setSegmentTerms(terms);
        segment.setPaymentPeriod(paymentPeriod.toString());
        segment.setPaymentMode(paymentMode.toString());
        segment.setInterestCalcMode(interestCalcMode.toString());

        //*************** 4. 调用交易开始计算 ***************
        LactTransaction lacts = engine.getTransaction(CALC_PAYMENT_SCHEDULE_LIST_TRANS_CODE);

        MapObject invokeParam = new MapObject();
        lacts.load(loan,invokeParam);
        lacts.handle(loan);

        return loan;
    }
}
