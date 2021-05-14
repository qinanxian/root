package com.vekai.lact.core;

import cn.fisok.raw.lang.MapObject;
import com.vekai.lact.model.LactLoan;
import com.vekai.lact.model.LactLoanSegment;
import com.vekai.lact.type.InterestCalcMode;
import com.vekai.lact.type.PayPointTime;
import com.vekai.lact.type.PaymentMode;
import com.vekai.lact.type.PaymentPeriod;
import com.vekai.lact.type.RateAppearMode;
import cn.fisok.raw.kit.DateKit;
import org.junit.Test;

import java.util.Date;

/**
 * 测试案例[(等额本息-先付租金-残值不计息)]
 */
public class LactTrans70Test extends LactTransBase{

    @Test
    public void testTrans() {

        //把关键变量提取出来
        Double loanAmt = 230000d;                           //债权金额
        Integer terms = 12;                                         //期数
        PaymentPeriod paymentPeriod = PaymentPeriod.M;        //期数单位，还款周期：按月
        Date startDate = DateKit.parse("2017-01-10");           //开始日期
        RateAppearMode rateAppearMode = RateAppearMode.Indication;  //利率表现形式:明示利率
        PaymentMode paymentMode = PaymentMode.EqualLoan;    //还款方式: 等额本息
        InterestCalcMode interestCalcMode = InterestCalcMode.M;     //计息方式：按日

        //借款基本信息（这部分信息并不参与还款计划的计算）
        LactLoan loan = new LactLoan();
        loan.setTotalAmt(loanAmt);
        loan.setFirstPayAmt(11500d);

        loan.setTermMonth(terms * paymentPeriod.getMulti());
        loan.setTerms(terms);
        loan.setResidualAmt(63000d); //残值
        loan.setResidualGenInterest("N");
        loan.setStartDate(startDate);
        loan.setLoanAmt(loan.getTotalAmt()-loan.getFirstPayAmt());
        loan.setPayPointTime(PayPointTime.Prepay.toString());

        //填充分段信息（这部分信息才参与计算）
        LactLoanSegment segment = new LactLoanSegment(loan);
        loan.setOnlyOneSegment(segment);

        segment.setSegmentLoanAmt(loan.getTotalAmt()-loan.getFirstPayAmt());
        segment.setStartDate(startDate);
        segment.setTermMonth(terms * paymentPeriod.getMulti());
        segment.setRateAppearMode(rateAppearMode.toString());
        segment.setSegmentTerms(terms);
        segment.setPaymentPeriod(paymentPeriod.toString());
        segment.setPaymentMode(paymentMode.toString());
        segment.setInterestCalcMode(interestCalcMode.toString());
        segment.setInterestRate(0.07d);


        //组装对象
        MapObject mapObject = new MapObject();
        //调用交易
        invokeTrans("LACTS10", loan, mapObject);

    }
}
