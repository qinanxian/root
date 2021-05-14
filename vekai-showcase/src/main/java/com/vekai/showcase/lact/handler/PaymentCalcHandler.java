package com.vekai.showcase.lact.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.lact.core.LactEngine;
import com.vekai.lact.core.transaction.LactTransaction;
import com.vekai.lact.model.LactLoan;
import com.vekai.lact.model.LactLoanSegment;
import com.vekai.lact.model.PaymentSchedule;
import com.vekai.lact.type.InterestCalcMode;
import com.vekai.lact.type.PaymentMode;
import com.vekai.lact.type.PaymentPeriod;
import com.vekai.lact.type.RateAppearMode;
import cn.fisok.raw.kit.DateKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Component
/**
 * 还款核算Handler
 */
public class PaymentCalcHandler extends MapDataOneHandler {
    @Autowired
    protected LactEngine engine;


    public LactLoan exeCalc(DataForm dataForm, MapObject dto){
        //把关键变量提取出来
        Double totalAmt = dto.getValue("totalAmt").doubleValue();                                           //债权金额
        Double firstPayAmt = dto.getValue("firstPayAmt").doubleValue();                                     //首付款
        Integer terms = dto.getValue("terms").intValue();                                                   //期数
        Integer termDay = dto.getValue("termDay").intValue();                                                   //期数
        PaymentPeriod paymentPeriod = PaymentPeriod.valueOf(dto.getValue("paymentPeriod").strValue());      //期数单位，还款周期：按月
        Date startDate = dto.getValue("startDate").dateValue();                                             //开始日期
        RateAppearMode rateAppearMode = RateAppearMode.valueOf(dto.getValue("rateAppearMode").strValue());  //利率表现形式:明示利率
        String rateFloatType = dto.getValue("rateFloatType").strValue();                                    //利率浮动方式:按比例
        Double rateFloat = dto.getValue("rateFloat").doubleValue();                                         //利率浮动值:上浮0.65个百分点
        PaymentMode paymentMode = PaymentMode.valueOf(dto.getValue("paymentMode").strValue());              //还款方式: 等额本息
        InterestCalcMode interestCalcMode = InterestCalcMode.valueOf(dto.getValue("interestCalcMode").strValue());  //计息方式：按月

        Double loanAmt = totalAmt - firstPayAmt;
        int termMonth = terms * paymentPeriod.getMulti();
        Date expiryDate = DateKit.plusMonths(startDate,termMonth);
        if(termDay != null && termDay>0){
            expiryDate = DateKit.plusDays(expiryDate,termDay);
        }
        //借款基本信息（这部分信息并不参与还款计划的计算）
        LactLoan loan = new LactLoan();
        LactLoanSegment segment = new LactLoanSegment(loan);

        loan.setTotalAmt(loanAmt);
        loan.setFirstPayAmt(0d);
        loan.setLoanAmt(loan.getTotalAmt()-loan.getFirstPayAmt());
        loan.setTermMonth(termMonth);
        loan.setTerms(terms);
        loan.setStartDate(startDate);
        loan.setExpiryDate(expiryDate);
        loan.setOnlyOneSegment(segment);//填充分段信息（这部分信息才参与计算）

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

        fillScheduleList(segment,dto);


        //组装对象,调用交易
        MapObject mapObject = new MapObject();
        LactTransaction lacts = engine.getTransaction("LACTS10");
        lacts.load(loan,params);
        lacts.handle(loan);

        return loan;
    }

    private void fillScheduleList(LactLoanSegment segment, MapObject dto){
        if (dto.get("scheduleList") == null)
            return;
        List<LinkedHashMap> paymentScheduleList = (ArrayList)dto.get("scheduleList");
        List<PaymentSchedule> scheduleList = new ArrayList<PaymentSchedule>();
        for(int i=0;i<paymentScheduleList.size();i++){
            LinkedHashMap paymentSchedule = paymentScheduleList.get(i);
            PaymentSchedule schedule = new PaymentSchedule(segment);
            schedule.setTermTimes((Integer) paymentSchedule.get("termTimes"));
            schedule.setSettleDate(DateKit.parse((String)paymentSchedule.get("paymentDate")));
            schedule.setPaymentDate(DateKit.parse((String)paymentSchedule.get("paymentDate")));
            schedule.setPaymentAmt(Double.parseDouble(paymentSchedule.getOrDefault("paymentAmt",0).toString()));
            schedule.setPrincipal(Double.parseDouble(paymentSchedule.getOrDefault("principal",0).toString()));
            scheduleList.add(schedule);
        }
        segment.setScheduleList(scheduleList);
    }
}
