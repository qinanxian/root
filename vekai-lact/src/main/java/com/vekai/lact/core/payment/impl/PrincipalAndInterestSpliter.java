package com.vekai.lact.core.payment.impl;

import com.vekai.lact.model.PaymentSchedule;
import cn.fisok.raw.kit.NumberKit;

import java.util.List;



/**
 * 本金，利率拆分通用计算
 * 
 * @author 杨松<syang@amarsoft.com>
 * @date 2016年12月20日
 */
public class PrincipalAndInterestSpliter {
    
    private int decimalScale = 2;           //本金利息小数点保留位数
    private int interestRateScale = 16;     //利率小数点保留位数
    private boolean residualGenInterest = true;//余值是否计息
    private double residual = 0d;           //资产余值
    

	public void split(List<PaymentSchedule> pmtList, double loanAmt, int lockTime) {
		double preRemainCost = loanAmt;
		if(lockTime > 0){
			preRemainCost = pmtList.get(lockTime-1).getRemainCost();
		}
		for (int i = lockTime; i < pmtList.size(); i++) {
			PaymentSchedule pmt = pmtList.get(i);
			boolean isLast = (i == pmtList.size() - 1);
			if (!isLast) {
				splitCalcCapitalAndInterest(pmt, preRemainCost);
				preRemainCost = pmt.getRemainCost();
			}
		}
		makeLastTermBalance(pmtList, loanAmt);
	}
    
    /**
     * 末期挤差平衡
     * @param schedules
     */
    public void makeLastTermBalance(List<PaymentSchedule> schedules, double capitalAmt){
        double preRemainCost = capitalAmt;
        double sumCapitalToLast2 = 0.0;         //直到倒数第二个的本金总额
        
        for(int i=0;i<schedules.size();i++){
            PaymentSchedule schedule = schedules.get(i);
            boolean isLast = (i == schedules.size()-1 );
            //@todo 明示利率，调息情况下，要考虑同一周期几，从次调息产生的多段利率问题
            if(!isLast){
                preRemainCost = schedule.getRemainCost();
                sumCapitalToLast2 += schedule.getPrincipal();
            }else{
                //最后一期,挤差
            	sumCapitalToLast2= NumberKit.round(sumCapitalToLast2,decimalScale);
                double lastCapital = NumberKit.round(capitalAmt - sumCapitalToLast2 - residual,decimalScale);
                double lastInterest = NumberKit.round(schedule.getPaymentAmt() - lastCapital,decimalScale);
                double lastRemainCost = preRemainCost-lastCapital   ;//上期未还成本 - 本期本金（实际上它是等于0的）
                schedule.setPrincipal(lastCapital);
                schedule.setInterest(lastInterest);
                schedule.setRemainCost(lastRemainCost);
            }
        }
    }
    
    /**
     * 根据每期还款金额，重新计算本金利息部分
     * @param pmt
     * @param preRemainCost
     */
    protected void splitCalcCapitalAndInterest(PaymentSchedule pmt, double preRemainCost){
        double cost = preRemainCost;
        if(!residualGenInterest) cost = preRemainCost - residual;   //余值不计息
        //利率，先要进行精度处理，然后才能使用
        double rate = NumberKit.round(pmt.getInterestRate(),interestRateScale);
        double interest = NumberKit.round(cost * rate,decimalScale);             //利息 = 未还本金  * 利率
        double capital =  NumberKit.round(pmt.getPaymentAmt() - interest,decimalScale); //本金 = 还款总额 - 利息
        double remainCost =  NumberKit.round(preRemainCost - capital,decimalScale);         //剩余本金 = 上期剩余本金 - 本期已还金额
        pmt.setInterest(interest);
        pmt.setPrincipal(capital);
        pmt.setRemainCost(remainCost);
    }
    
    /**
     * 本金利息小数点保留位数
     * @return
     */
    public int getDecimalScale() {
        return decimalScale;
    }
    /**
     * 本金利息小数点保留位数
     * @param decimalScale
     */
    public void setDecimalScale(int decimalScale) {
        this.decimalScale = decimalScale;
    }
    /**
     * 利率小数点保留位数
     * @return
     */
    public int getInterestRateScale() {
        return interestRateScale;
    }
    /**
     * 利率小数点保留位数
     * @param interestRateScale
     */
    public void setInterestRateScale(int interestRateScale) {
        this.interestRateScale = interestRateScale;
    }
    /**
     * 余值是否计息
     * @return
     */
    public boolean isResidualGenInterest() {
        return residualGenInterest;
    }
    /**
     * 余值是否计息
     * @param residualGenInterest
     */
    public void setResidualGenInterest(boolean residualGenInterest) {
        this.residualGenInterest = residualGenInterest;
    }
    /**
     * 余值
     * @return
     */
    public double getResidual() {
        return residual;
    }
    /**
     * 余值
     * @param residual
     */
    public void setResidual(double residual) {
        this.residual = residual;
    }
    
}
