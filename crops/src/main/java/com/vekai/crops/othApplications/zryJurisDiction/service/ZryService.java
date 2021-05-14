package com.vekai.crops.othApplications.zryJurisDiction.service;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.crops.othApplications.zryJurisDiction.entity.ZryFinancingOrderInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ZryService {
    protected static Logger logger = LoggerFactory.getLogger(ZryService.class);

    @Autowired
    private BeanCruder beanCruder;

    public String getPreQuota(String loanId) {
        Double preQuota=0.00;
        String sql = "SELECT * FROM ZRY_FINANCING_ORDER_INFO WHERE LOAN_ID = :loanId";
        ZryFinancingOrderInfo zryFinancingOrderInfo = beanCruder.selectOne(ZryFinancingOrderInfo.class, sql, "loanId", loanId);
        if(zryFinancingOrderInfo!=null){
            Double bidAmt = Double.valueOf(zryFinancingOrderInfo.getBidAmt());
            Double paidAmt = Double.valueOf(zryFinancingOrderInfo.getPaidAmt());
            Double advanceAmt = Double.valueOf(zryFinancingOrderInfo.getAdvanceAmt());
            preQuota=bidAmt-paidAmt-advanceAmt;
        }
        return preQuota.toString();
    }
}
