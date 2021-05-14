package com.vekai.crops.obiz.lact.invoker;

import com.vekai.lact.type.*;

import java.util.Date;

/**
 * 债权核算参数对象
 */
public class LactLoanCalcParam {
    public Double loanAmt;                                             //债权金额
    public Integer terms;                                              //期限(期,可以是月，季，半年，年)
    public Integer termDay;                                            //期限（零头天数）
    public BaseRateType baseRateType = BaseRateType.PBOCLoan;          //基准利率类型,（默认人行基准利率）
    public RateFloatType rateFloatType;                                //利率浮动类型
    public Double rateFloat;                                           //利率浮动值
    public Date startDate;                                             //开始日期
    public PaymentPeriod paymentPeriod;                                //还款周期
    public RateAppearMode rateAppearMode = RateAppearMode.Indication;  //利率表现形式(默认：明示）
    public PaymentMode paymentMode;                                    //还款方式
    public PayPointTime payPointTime = PayPointTime.Postpay;           //付款时点(默认：后付）
    public InterestCalcMode interestCalcMode;                          //计息方式
}
