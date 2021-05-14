//package com.vekai.crops.lact;
//
//import com.vekai.crops.BaseTest;
//import com.vekai.crops.obiz.lact.invoker.LactEngineInvoker;
//import com.vekai.crops.obiz.lact.invoker.LactLoanCalcParam;
//import com.vekai.appframe.conf.dossier.dao.ConfDossierDao;
//import com.vekai.appframe.conf.dossier.model.ConfDossier;
//import com.vekai.lact.type.*;
//import com.vekai.runtime.kit.DateKit;
//import org.junit.Assert;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Date;
//
//public class LactInvokerTest extends BaseTest {
//    @Autowired
//    private LactEngineInvoker invoker;
//
//
//    @Test
//    public void testInvoker(){
//        LactLoanCalcParam param = new LactLoanCalcParam();
//        param.loanAmt = 5000000d;                           //债权金额
//        param.terms = 24;                                   //期数
//        param.paymentPeriod = PaymentPeriod.M;              //期数单位，还款周期：按月
//        param.startDate = DateKit.xparse("2014-07-20"); //开始日期
//        param.rateAppearMode = RateAppearMode.Indication;   //利率表现形式:明示利率
//        param.rateFloatType = RateFloatType.Multiply;       //利率浮动方式:按比例
//        param.rateFloat = 1.065d;                           //利率浮动值:上浮0.65个百分点
//        param.paymentMode = PaymentMode.EqualLoan;          //还款方式: 等额本息
//        param.interestCalcMode = InterestCalcMode.M;        //计息方式：按月
//
//        invoker.invokeCalcPaymentScheduleList(param);
//    }
//}
