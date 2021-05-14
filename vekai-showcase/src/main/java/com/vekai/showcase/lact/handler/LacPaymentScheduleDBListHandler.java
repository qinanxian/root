package com.vekai.showcase.lact.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.lact.constant.LactConst;
import com.vekai.lact.entity.PaymentScheduleEntity;
import cn.fisok.raw.kit.MapKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by luyu on 2018/6/28.
 */
@Component
public class LacPaymentScheduleDBListHandler extends BeanDataListHandler<PaymentScheduleEntity> {


    @Autowired
    BeanCruder beanCruder;

    @Override
    @Transactional
    public int save(DataForm dataForm, List<PaymentScheduleEntity> dataList) {
        if (dataList.size() > 0) {
            PaymentScheduleEntity paymentScheduleEntity = dataList.get(0);
            String sql = "DELETE FROM LACT_PAYMENT_SCHEDULE WHERE LOAN_ID=:loanId";
            beanCruder.execute(sql, MapKit.mapOf("loanId",paymentScheduleEntity.getLoanId()));
        }
        dataList = dataList.stream().map(this::initPaymentScheduleEntity).collect(Collectors.toList());
        return beanCruder.save(dataList);
    }

    private PaymentScheduleEntity initPaymentScheduleEntity(PaymentScheduleEntity paymentScheduleEntity) {
        paymentScheduleEntity.setInOrOut(LactConst.IN);
        paymentScheduleEntity.setPaymentType(LactConst.Repayment);
        paymentScheduleEntity.setIsProfit("Y");
        paymentScheduleEntity.setTotalAmt(paymentScheduleEntity.getPaymentAmt());
        return paymentScheduleEntity;
    }

    @Transactional
    public Integer clearPaymentSchedule(DataForm dataForm, MapObject object) {
        String loanId = object.getValue("loanId").strValue();
        String sql = "DELETE FROM LACT_PAYMENT_SCHEDULE WHERE LOAN_ID=:loanId";
        return beanCruder.execute(sql, MapKit.mapOf("loanId",loanId));
    }
}
