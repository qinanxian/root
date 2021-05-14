package com.vekai.lact.core.loader.impl;

import cn.fisok.raw.lang.MapObject;
import com.vekai.lact.core.loader.DataObjectLoader;
import com.vekai.lact.entity.LactLoanSegmentEntity;
import com.vekai.lact.exception.LactException;
import com.vekai.lact.model.LactLoan;
import com.vekai.lact.model.LactLoanSegment;
import com.vekai.lact.type.LactConst;
import com.vekai.lact.type.RateAppearMode;
import com.vekai.lact.type.RateFloatType;
import cn.fisok.raw.kit.NumberKit;
import cn.fisok.raw.kit.ValidateKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.List;

/**
 * 实际执行利率计算
 */
public class ExecInterestRateCalcLoader implements DataObjectLoader<LactLoan> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void load(LactLoan loan, MapObject parameter) {
        logger.trace("--加载利率档次--");
//        List<LactLoanSegmentEntity> segments = loan.getSegments();
        List<LactLoanSegment> segments = loan.getSegmentList();
        for (LactLoanSegmentEntity segment : segments) {
            //不是明示利率，则不需要计算执行利率
            if (RateAppearMode.valueOf(segment.getRateAppearMode()) != RateAppearMode.Indication) continue;
            //用于利率不是由基准利率计算出来的情况
            if (segment.getInterestRate() != null) continue;

            String rateFloatType = segment.getRateFloatType();  //利率浮动方式
            Double floatValue = segment.getRateFloat();         //利率浮动值

            ValidateKit.notBlank(rateFloatType, "[利率浮动类型]不能为空");

            RateFloatType vRateFloatType = RateFloatType.valueOf(segment.getRateFloatType());  //利率浮动方式
            Double execRate = 0d;
            if(vRateFloatType == RateFloatType.Fixed){      //固定利率不处理
                execRate = floatValue;
            }else{
                Double baseRate = segment.getBaseRate();            //取基准利率
                ValidateKit.isTrue(baseRate>0d, "[基准利率]必需大于0");

                //判断利率浮动类型是加(基点)还是乘(比例)
                if(vRateFloatType == RateFloatType.Plus){
                    execRate = baseRate + floatValue;
                }else if(vRateFloatType == RateFloatType.Multiply){
                    execRate = baseRate * floatValue;
                }else{
                    throw new LactException("数据异常，找不到利率浮动类型={0}",rateFloatType);
                }
            }

            execRate = NumberKit.round(execRate, LactConst.INTEREST_RATE_SCALE);
            logger.debug(MessageFormat.format("实际执行利率:{0,number,##.000000##}",execRate));
            segment.setInterestRate(execRate);
        }
    }
}
