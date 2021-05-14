package com.vekai.showcase.lact.handler;

import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.lact.core.LactEngine;
import com.vekai.lact.core.transaction.LactTransaction;
import com.vekai.lact.entity.LactLoanEntity;
import com.vekai.lact.entity.LactLoanSegmentEntity;
import com.vekai.lact.model.LactLoan;
import com.vekai.lact.model.LactLoanSegment;
import com.vekai.lact.service.LactService;
import com.vekai.lact.type.InterestCalcMode;
import com.vekai.lact.type.PaymentMode;
import com.vekai.lact.type.PaymentPeriod;
import com.vekai.lact.type.RateAppearMode;
import com.vekai.showcase.lact.dto.LoanDtoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by luyu on 2018/6/28.
 */
@Component
public class PaymentCalcDBHandler extends MapDataOneHandler {
    @Autowired
    protected LactEngine engine;
    @Autowired
    LactService lactService;
    @Autowired
    BeanCruder beanCruder;


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


        //组装对象,调用交易
        MapObject mapObject = new MapObject();
        LactTransaction lacts = engine.getTransaction("LACTS10");
        lacts.load(loan,params);
        lacts.handle(loan);

        return loan;
    }

    @Transactional
    public LoanDtoBean saveLoadInfo(DataForm dataForm, MapObject object) {
        String loanId = object.getValue("loanId").strValue();
        LactLoanEntity lactLoanEntity = null;
        LoanDtoBean loanDtoBean = new LoanDtoBean();
        if (StringKit.isBlank(loanId)) {
            lactLoanEntity = new LactLoanEntity();
            BeanKit.copyProperties(object,lactLoanEntity);
            beanCruder.save(lactLoanEntity);
        } else {
            lactLoanEntity = lactService.getLactLoanEntity(loanId);
            BeanKit.copyProperties(object,lactLoanEntity);
            lactLoanEntity.setStartDate(object.getValue("startDate").dateValue());
            lactLoanEntity.setExpiryDate(object.getValue("expiryDate").dateValue());
            beanCruder.save(lactLoanEntity);
        }
        loanDtoBean.setLoanId(lactLoanEntity.getLoanId());
        LactLoanSegmentEntity lactLoanSegmentEntity = this.createLactSegment(lactLoanEntity,object);
        loanDtoBean.setLoanSegmentId(lactLoanSegmentEntity.getLoanSegmentId());
        return loanDtoBean;
    }

    private LactLoanSegmentEntity createLactSegment(LactLoanEntity lactLoanEntity, MapObject object) {
        String loadId = lactLoanEntity.getLoanId();
        List<LactLoanSegmentEntity> lactLoanSegmentEntities = lactService.getLactLoanSegmentEntityList(loadId);
        LactLoanSegmentEntity lactLoanSegmentEntity = null;
        if (lactLoanSegmentEntities.size() > 0) {
            lactLoanSegmentEntity = lactLoanSegmentEntities.get(0);
        } else {
            lactLoanSegmentEntity = new LactLoanSegmentEntity();
        }
        BeanKit.copyProperties(object,lactLoanSegmentEntity);
        beanCruder.save(lactLoanSegmentEntity);
        return lactLoanSegmentEntity;
    }
}
