package com.vekai.lact.core;

import cn.fisok.raw.lang.MapObject;
import com.vekai.lact.model.LactLoan;
import com.vekai.lact.model.LactLoanSegment;
import com.vekai.lact.type.InterestCalcMode;
import com.vekai.lact.type.PaymentMode;
import com.vekai.lact.type.PaymentPeriod;
import com.vekai.lact.type.RateAppearMode;
import cn.fisok.raw.kit.DateKit;
import org.junit.Test;

import java.util.Date;

/**
 * 测试案例[明示利率-等额本息]-浦发万用金计算
 */
public class LactTrans11Test extends LactTransBase {

    @Test
    public void testTrans() {

        //把关键变量提取出来
        Double loanAmt = 240000d;                                  //债权金额
        Integer terms = 36;                                         //期数
        PaymentPeriod paymentPeriod = PaymentPeriod.M;        //期数单位，还款周期：按月
        Date startDate = DateKit.parse("2018-07-20");           //开始日期
        RateAppearMode rateAppearMode = RateAppearMode.Indication;  //利率表现形式:明示利率
        String rateFloatType = "Multiply";                          //利率浮动方式:按比例
        Double rateFloat = 4.807;                                   //利率浮动值:上浮0.65个百分点
        PaymentMode paymentMode = PaymentMode.EqualLoan;         //还款方式: 等额本息
        InterestCalcMode interestCalcMode = InterestCalcMode.M;     //计息方式：按月

        //借款基本信息（这部分信息并不参与还款计划的计算）
        LactLoan loan = new LactLoan();
        loan.setTotalAmt(loanAmt);
        loan.setFirstPayAmt(0d);
        loan.setLoanAmt(loan.getTotalAmt()-loan.getFirstPayAmt());
        loan.setTermMonth(terms * paymentPeriod.getMulti());
        loan.setTerms(terms);
//        loan.setTermUnit(paymentPeriod.toString());
        loan.setStartDate(startDate);

        //填充分段信息（这部分信息才参与计算）
        LactLoanSegment segment = new LactLoanSegment(loan);
        loan.setOnlyOneSegment(segment);

        segment.setSegmentLoanAmt(loanAmt);
        segment.setStartDate(startDate);
        segment.setTermMonth(terms * paymentPeriod.getMulti());
        segment.setRateAppearMode(rateAppearMode.toString());
//        segment.setBaseRateType("PBOCBaseRate");
        segment.setRateFloatType(rateFloatType);
        segment.setRateFloat(rateFloat);
        segment.setSegmentTerms(terms);
        segment.setPaymentPeriod(paymentPeriod.toString());
        segment.setPaymentMode(paymentMode.toString());
        segment.setInterestCalcMode(interestCalcMode.toString());


        //组装对象
        MapObject mapObject = new MapObject();
        //调用交易
        invokeTrans("LACTS10", loan, mapObject);
    }
}
