package com.vekai.lact.service;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.lact.entity.LactLoanEntity;
import com.vekai.lact.entity.LactLoanSegmentEntity;
import com.vekai.lact.entity.PaymentScheduleEntity;
import com.vekai.lact.model.LactLoan;
import com.vekai.lact.model.LactLoanSegment;
import com.vekai.lact.model.PaymentSchedule;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.ValidateKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luyu on 2018/6/28.
 */
@Component
public class LactService {

    @Autowired
    BeanCruder beanCruder;


    public LactLoanEntity getLactLoanEntity(String loanId) {
        String sql = "SELECT * FROM LACT_LOAN WHERE LOAN_ID=:loanId";
        LactLoanEntity lactLoanEntity = beanCruder.selectOne(LactLoanEntity.class,sql,"loanId",loanId);
        return lactLoanEntity;
    }

    public List<LactLoanSegmentEntity> getLactLoanSegmentEntityList(String loanId) {
        String sql = "SELECT * FROM LACT_LOAN_SEGMENT WHERE LOAN_ID=:loanId";
        List<LactLoanSegmentEntity> lactLoanSegmentEntities = beanCruder
                .selectList(LactLoanSegmentEntity.class,sql,"loanId",loanId);
        return lactLoanSegmentEntities;
    }

    public LactLoanSegmentEntity getLactLoanSegmentEntity(String loanSegmentId) {
        String sql = "SELECT * FROM LACT_PAYMENT_SCHEDULE WHERE LOAN_SEGMENT_ID=:loanSegmentId";
        LactLoanSegmentEntity lactLoanSegmentEntity = beanCruder
                .selectOne(LactLoanSegmentEntity.class,sql,"loadId",loanSegmentId);
        return lactLoanSegmentEntity;
    }


    @Transactional
    public LactLoanEntity saveLoan(LactLoan loan) {
        String loanId = loan.getLoanId();
        LactLoanEntity lactLoanEntity = null;
        if (StringKit.isBlank(loanId)) {
            lactLoanEntity = new LactLoanEntity();
            BeanKit.copyProperties(loan,lactLoanEntity);
            beanCruder.save(lactLoanEntity);
        } else {
            lactLoanEntity = this.getLactLoanEntity(loanId);
            BeanKit.copyProperties(loan,lactLoanEntity);
            lactLoanEntity.setStartDate(loan.getStartDate());
            lactLoanEntity.setExpiryDate(loan.getExpiryDate());
            beanCruder.save(lactLoanEntity);
        }
        this.saveSegmentAndPaymentSchedule(loan, lactLoanEntity);

        return lactLoanEntity;
    }

    /**
     * 保存测算分段+收付款计划表
     * @param lactLoan
     * @param lactLoanEntity
     */
    private void saveSegmentAndPaymentSchedule(LactLoan lactLoan, LactLoanEntity lactLoanEntity) {
        String loanId = lactLoanEntity.getLoanId();
        ValidateKit.notBlank(loanId, "LoanId:{}获取失败", loanId);
        List<LactLoanSegment> segmentList = lactLoan.getSegmentList();

        if(segmentList==null||segmentList.isEmpty()){
            throw new RuntimeException("无法获取测算分段,LoanId:"+loanId);
        }
        /**
         * 目前只考虑测算分段一个情况
         */
        LactLoanSegment lactLoanSegment = segmentList.get(0);

        List<LactLoanSegmentEntity> lactLoanSegmentEntities = this.getLactLoanSegmentEntityList(loanId);
        LactLoanSegmentEntity lactLoanSegmentEntity = null;
        if (lactLoanSegmentEntities.size() > 0) {
            lactLoanSegmentEntity = lactLoanSegmentEntities.get(0);
        } else {
            lactLoanSegmentEntity = new LactLoanSegmentEntity();
        }
        String loanSegmentId = lactLoanSegmentEntity.getLoanSegmentId();


        BeanKit.copyProperties(lactLoanSegment,lactLoanSegmentEntity);
        lactLoanSegmentEntity.setLoanId(loanId);
        lactLoanSegmentEntity.setLoanSegmentId(loanSegmentId);
        beanCruder.save(lactLoanSegmentEntity);

        loanSegmentId = lactLoanSegmentEntity.getLoanSegmentId();

        ValidateKit.notBlank(loanSegmentId, "LoanId:{}测算分段Id获取失败", loanId);
        Map<String, Object> whereMap = new HashMap<>();
        whereMap.put("loanId", loanId);
        whereMap.put("loanSegmentId", loanSegmentId);
        beanCruder.execute("delete from LACT_PAYMENT_SCHEDULE where LOAN_ID=:loanId and LOAN_SEGMENT_ID=:loanSegmentId", whereMap);

        List<PaymentScheduleEntity> paymentScheduleEntityList=new ArrayList<>();
        List<PaymentSchedule> scheduleList = lactLoanSegment.getScheduleList();
        for (PaymentSchedule paymentSchedule:scheduleList) {
            PaymentScheduleEntity paymentScheduleEntity = new PaymentScheduleEntity();
            BeanKit.copyProperties(paymentSchedule, paymentScheduleEntity);
            paymentScheduleEntity.setLoanId(loanId);
            paymentScheduleEntity.setLoanSegmentId(loanSegmentId);
            paymentScheduleEntityList.add(paymentScheduleEntity);
        }
        beanCruder.save(paymentScheduleEntityList);

    }
}
