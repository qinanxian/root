package com.vekai.lact.core.loader.impl;

import com.vekai.lact.core.loader.DataObjectLoader;
import com.vekai.lact.entity.LactLoanSegmentEntity;
import com.vekai.lact.model.LactLoan;
import com.vekai.lact.model.LactLoanSegment;
import com.vekai.lact.type.BaseRateType;
import com.vekai.lact.type.RateAppearMode;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.MapObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * 默认值填充
 */
public class DefaultValueFillLoader implements DataObjectLoader<LactLoan> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void load(LactLoan loan, MapObject parameter) {
        logger.trace("--加载利率档次--");
        List<LactLoanSegment> segments = loan.getSegmentList();

        this.fillPrincipalAndInterestParam(loan);

        for(LactLoanSegmentEntity segment : segments){
            //不是明示利率，则不需要计算执行利率
            if (RateAppearMode.valueOf(segment.getRateAppearMode()) != RateAppearMode.Indication) continue;

            String baseRateType = segment.getBaseRateType();    //基准利率类型
            baseRateType = StringKit.nvl(baseRateType, BaseRateType.PBOCLoan.toString());
            segment.setBaseRateType(baseRateType);
        }
    }

    private void fillPrincipalAndInterestParam(LactLoan loan) {
        //获取还款周期的默认值
        List<LactLoanSegment> lactLoanSegments = loan.getSegmentList();
        String defaultPayPeriod = null;
        if (lactLoanSegments.size() > 0) {
            defaultPayPeriod = lactLoanSegments.get(0).getPaymentPeriod();
        }
        //获取还款日的默认值
        Date startDate = loan.getStartDate();
        Integer defaultDay = DateKit.getDay(startDate);

        //设置还息的默认值
        String payInterestPeriod = StringKit.defaultIfBlank(loan.getPayInterestPeriod(),defaultPayPeriod);
        loan.setPayInterestPeriod(payInterestPeriod);
        String payInterestPoint = StringKit.defaultIfBlank(loan.getPayInterestPoint(),"FIXED_DAY");
        loan.setPayInterestPoint(payInterestPoint);
        if (loan.getPayInterestDay() == null || loan.getPayInterestDay() == 0) {
            loan.setPayInterestDay(defaultDay);
        }

        //设置还本的默认值
        String payPrincipalPeriod = StringKit.defaultIfBlank(loan.getPayPrincipalPeriod(),defaultPayPeriod);
        loan.setPayPrincipalPeriod(payPrincipalPeriod);
        String payPrincipalPoint = StringKit.defaultIfBlank(loan.getPayPrincipalPoint(),"FIXED_DAY");
        loan.setPayPrincipalPoint(payPrincipalPoint);
        if (loan.getPayPrincipalDay() == null || loan.getPayPrincipalDay() == 0) {
            loan.setPayPrincipalDay(defaultDay);
        }
        loan.setPayPrincipalInterestSync("N");
        if (payInterestPeriod.equals(payPrincipalPeriod) && payInterestPoint.equals(payPrincipalPoint)
                && loan.getPayPrincipalDay() == loan.getPayInterestDay()) {
            loan.setPayPrincipalInterestSync("Y");
            loan.setPayPoint(loan.getPayPoint());
            loan.setPayFixedDay(loan.getPayPrincipalDay());
        }
    }

}
