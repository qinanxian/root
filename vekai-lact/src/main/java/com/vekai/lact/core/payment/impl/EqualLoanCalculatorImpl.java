package com.vekai.lact.core.payment.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.List;

import com.vekai.fnat.math.MultiplePrimaryEquationGaussLiminanImpl;
import com.vekai.fnat.math.MultiplePrimaryEquationSolver;
import com.vekai.lact.core.payment.PaymentCalculator;
import com.vekai.lact.exception.LactException;
import com.vekai.lact.model.LactLoan;
import com.vekai.lact.model.LactLoanSegment;
import com.vekai.lact.model.PaymentSchedule;
import com.vekai.lact.type.LactConst;
import com.vekai.lact.type.PayPointTime;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.ValidateKit;
import org.la4j.Matrix;
import org.la4j.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 等额本息
 *
 * @author 杨松<syang@amarsoft.com>
 * @date 2016年12月7日
 */
public class EqualLoanCalculatorImpl implements PaymentCalculator<LactLoanSegment> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private MultiplePrimaryEquationSolver multiplePrimaryEquationSolver = new MultiplePrimaryEquationGaussLiminanImpl();

    private int decimalScale = 2;
    private int interestRateScale = 14;


    public void calculate(LactLoanSegment segment) {
        logger.trace(">>>>>>等额本息计算<<<<<<");

        LactLoan loan = segment.getLoan();

        Double residualAmt = loan.getResidualAmt();                                                     //余值
        boolean residualGenInterest = "Y".equals(loan.getResidualGenInterest());                        //余值是否计息
        String strPayPointTime = StringKit.nvl(loan.getPayPointTime(), PayPointTime.Postpay.toString());   //付款时间点[先付/后付]
        PayPointTime payPointTime = PayPointTime.valueOf(strPayPointTime);
        if(residualAmt == null)residualAmt = 0d;

        Double loanAmt = segment.getSegmentLoanAmt();   //本段债权金额
        Integer terms = segment.getSegmentTerms();      //本段部期数
        ValidateKit.isTrue(loanAmt>0,"[本段债权金额]值不能为空");
        ValidateKit.isTrue(terms>=1,"[本段总期数]不能小于1");

        List<PaymentSchedule> pmtList = segment.getScheduleList();
        ValidateKit.isTrue(terms==pmtList.size(),"[总期数]和[还款计划]数不匹配,总期数:{0},还款计划数:{1}",terms,pmtList.size());

        // 使用矩阵法进行求解，解出每期还款金额，以及每期还本金额
        Matrix coefficientMatrix = buildCoefficientMatrix(terms, pmtList); // 算出来的是利息矩阵
        Vector valueVector = buildValueVector(residualGenInterest ? loanAmt :loanAmt - residualAmt, terms, pmtList); // 总本金乘以当期利息的矩阵
        if (residualGenInterest) {
            valueVector.set(terms, loanAmt - residualAmt);
        }
        if (payPointTime == PayPointTime.Prepay) {      //先付
            pmtList.get(0).setInterestRate(0d);
            valueVector.set(0, 0d);
        }
        Vector retVector = multiplePrimaryEquationSolver.solver(
                coefficientMatrix, valueVector, LactConst.CALC_SCALE,
                LactConst.RUNDING_MODE);
        double evenTotalAmt = scaleRemain(retVector.get(0));// 第0个为每期还款额
        segment.setPerTermPaymentAmt(evenTotalAmt);
        // 设置每期还款额
        for (PaymentSchedule paymentSchedule : pmtList) {
            paymentSchedule.setPaymentAmt(evenTotalAmt);
        }        // 进行本利拆分
        PrincipalAndInterestSpliter spliter = new PrincipalAndInterestSpliter();
        spliter.setDecimalScale(decimalScale);
        spliter.setInterestRateScale(interestRateScale);
        spliter.setResidualGenInterest(residualGenInterest);
        spliter.setResidual(residualAmt);
        spliter.split(pmtList, loanAmt, 0);

    }


    /**
     * 构建多元一次方程组的系数矩阵
     * @param terms 期数
     * @param pmtScheduleList
     * @return
     */
    public Matrix buildCoefficientMatrix(Integer terms,List<PaymentSchedule> pmtScheduleList){
        Matrix matrix = Matrix.zero(terms+1, terms+1);

        //拼三角矩阵
        for(int i=0;i<terms;i++){
            for(int j=0;j<terms;j++){
                if(j==0){
                    matrix.set(i, j, 1d);
                }else if(j==i+1){
                    matrix.set(i, j, -1d);
                }else if(j>0&&j<i+1){
                    //这里把最后一元也增加上去了，主要用来计算最后一个-1
                    //所以要多出来的那一行，不能取他的利率，否则会找不到利率数据的
                    double rateValue = pmtScheduleList.get(i).getInterestRate();
                    matrix.set(i,j,rateValue);
                }else{
                    matrix.set(i, j, 0d);
                }
            }
        }

        //最右下解的值，为-1(实际上为倒数第2行）
        matrix.set(terms-1, terms, -1d);
        //增加最后一列为0(最后一行最后一列【实际上是倒数第2行】的值被上一行设置为-1，不能被修改
        for(int i=0;i<terms-1;i++){
            matrix.set(i, terms, 0d);
        }
        //增加最后一行，第一个为0，其余为1
        for(int i=0;i<=terms;i++){
            if(i==0)matrix.set(terms,0,0d);
            else matrix.set(terms,i,1d);
        }
        return matrix;
    }

    private Vector buildValueVector(Double loanAmt,Integer terms,List<PaymentSchedule> pmtScheduleList){
        Vector vector = Vector.zero(terms+1);
        Vector rateVector = Vector.zero(terms);
        for(int i=0;i<terms;i++){
            //本金*当期利率
            double rateValue = pmtScheduleList.get(i).getInterestRate();
            rateVector.set(i, rateValue);
            if(rateValue<=0 || rateValue-0.0 <= 0.0000001){
                throw new LactException("还款计划表数据错误，计算到的实际执行利率{0,number,#.##########}太小，不合法",rateValue);
            }
            logger.trace(MessageFormat.format("第{0}期，实际执行利率{1,number,#.####}", i+1,rateValue));
            double kp = BigDecimal.valueOf(loanAmt).multiply(BigDecimal.valueOf(rateValue)).doubleValue();
            vector.set(i, kp);
        }
        vector.set(terms, loanAmt);

        return vector;
    }

    public EqualLoanCalculatorImpl() {
    }

    public MultiplePrimaryEquationSolver getMultiplePrimaryEquationSolver() {
        return multiplePrimaryEquationSolver;
    }

    public void setMultiplePrimaryEquationSolver(MultiplePrimaryEquationSolver multiplePrimaryEquationSolver) {
        this.multiplePrimaryEquationSolver = multiplePrimaryEquationSolver;
    }

    /**
     * 小数位数
     *
     * @return
     */
    public int getDecimalScale() {
        return decimalScale;
    }

    /**
     * 小数位数
     *
     * @param decimalScale
     */
    public void setDecimalScale(int decimalScale) {
        this.decimalScale = decimalScale;
    }

    /**
     * 利率保留小数位数
     *
     * @return
     */
    public int getInterestRateScale() {
        return interestRateScale;
    }

    /**
     * 利率保留小数位数
     *
     * @param interestRateScale
     */
    public void setInterestRateScale(int interestRateScale) {
        this.interestRateScale = interestRateScale;
    }

    private double scaleRemain(double v) {
        return BigDecimal.valueOf(v).setScale(decimalScale, RoundingMode.HALF_UP).doubleValue();
    }

}
