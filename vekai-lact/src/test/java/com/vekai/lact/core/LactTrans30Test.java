package com.vekai.lact.core;

import cn.fisok.raw.lang.MapObject;
import com.vekai.lact.model.LactLoan;
import com.vekai.lact.model.LactLoanSegment;
import com.vekai.lact.model.PaymentSchedule;
import com.vekai.lact.type.InterestCalcMode;
import com.vekai.lact.type.PaymentMode;
import com.vekai.lact.type.RateAppearMode;
import cn.fisok.raw.kit.DateKit;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 测试案例[隐含利率]
 */
public class LactTrans30Test extends LactTransBase{

    @Test
    public void testTrans() {

        //把关键变量提取出来
        Double loanAmt = 10000000d-0d;                           //债权金额
        Integer terms = 24;                                         //期数
        Date startDate = DateKit.parse("2013-12-26");           //开始日期
        RateAppearMode rateAppearMode = RateAppearMode.Implication; //利率表现形式:隐含利率
        PaymentMode paymentMode = PaymentMode.ImplicationCustomize;        //还款方式: 隐含利率自定义
        InterestCalcMode interestCalcMode = InterestCalcMode.D;     //计息方式：按日

        //借款基本信息（这部分信息并不参与还款计划的计算）
        LactLoan loan = new LactLoan();
        loan.setTotalAmt(loanAmt);
        loan.setFirstPayAmt(0d);
        loan.setLoanAmt(loan.getTotalAmt()-loan.getFirstPayAmt());
        loan.setTerms(terms);
        loan.setStartDate(startDate);

        //填充分段信息（这部分信息才参与计算）
        LactLoanSegment segment = new LactLoanSegment(loan);
        loan.setOnlyOneSegment(segment);

        segment.setSegmentLoanAmt(loanAmt);
        segment.setStartDate(startDate);
        segment.setRateAppearMode(rateAppearMode.toString());
        segment.setSegmentTerms(terms);
        segment.setPaymentMode(paymentMode.toString());
        segment.setInterestCalcMode(interestCalcMode.toString());

        fillScheduleList(segment);

        //组装对象
        MapObject mapObject = new MapObject();
        //调用交易
        invokeTrans("LACTS10", loan, mapObject);
    }

    private void fillScheduleList(LactLoanSegment segment){
        List<PaymentSchedule> scheduleList = new ArrayList<PaymentSchedule>();

        Object[][] rows = new Object[][]{
                {"2014-04-26",800000d},
                {"2014-10-26",1342000d},
                {"2015-04-26",800000d},
                {"2015-10-26",1800000d},
                {"2016-04-26",800000d},
                {"2016-10-26",1800000d},
                {"2017-04-26",800000d},
                {"2017-10-26",1800000d},
                {"2018-04-26",800000d},
                {"2018-10-26",1800000d},
        };

        for(int i=0;i<rows.length;i++){
            Object[] row = rows[i];

            PaymentSchedule schedule = new PaymentSchedule(segment);
            schedule.setTermTimes(i+1);
            schedule.setSettleDate(DateKit.parse((String)row[0]));
            schedule.setPaymentAmt((Double)row[1]);
            scheduleList.add(schedule);
        }

        segment.setScheduleList(scheduleList);

    }
}
