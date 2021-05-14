package com.vekai.lact.core.handler.impl;

import java.text.MessageFormat;
import java.util.List;

import com.vekai.lact.core.handler.DataObjectHandler;
import com.vekai.lact.model.LactLoan;
import com.vekai.lact.model.LactLoanSegment;
import com.vekai.lact.model.PaymentSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 核算对象数据输出到控制台实现
 * 
 * @author 杨松<syang@amarsoft.com>
 * @date 2016年12月6日
 */
public class ConsolePrintHandlerImpl implements DataObjectHandler<LactLoan> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public ConsolePrintHandlerImpl() {
    }

    @Override
    public void handle(LactLoan loan) {
        logger.debug("---------还款计划输出到控制台----------");
        logger.debug(MessageFormat.format("项目总金额:{0,number,###,###.00}，债权金额:{1,number,###,###.00}", loan.getTotalAmt(),loan.getLoanAmt()));
        List<LactLoanSegment> segments = loan.getSegmentList();
        for(int i=0;i<segments.size();i++){
            LactLoanSegment segment = segments.get(i);
            List<PaymentSchedule> scheduleList = segment.getScheduleList();

            String tpl = "前{1}个分段，债权金额{2,number,###,##0.000000}";
            String text = MessageFormat.format(tpl,i+1,segment.getSegmentLoanAmt());
            logger.debug(text);

            for(int j=0;j<scheduleList.size();j++){
                PaymentSchedule row = scheduleList.get(j);

                String msgTpl = "[{0,date,yyyy-MM-dd}]-> 当前第({1,number,##})期，{2,number,###,###,##0.000}(期供金额)={3,number,###,##0.00}(还本)+{5,number,###,##0.00}(还息)，剩余本金:{6,number,###,##0.00},{4,number,#0.000000}(利率)";
                String msgText = MessageFormat.format(msgTpl,row.getSettleDate(),row.getTermTimes(),row.getPaymentAmt(),row.getPrincipal(),row.getInterestRate(),row.getInterest(),row.getRemainCost());

                logger.debug(msgText);

            }
        }
    }
}
