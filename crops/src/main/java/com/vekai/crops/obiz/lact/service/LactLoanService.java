package com.vekai.crops.obiz.lact.service;

import com.vekai.crops.base.SerialNoGeneratorFiller;
import com.vekai.lact.entity.LactLoanEntity;
import com.vekai.lact.entity.LactLoanSegmentEntity;
import com.vekai.lact.entity.PaymentScheduleEntity;
import com.vekai.lact.model.LactLoan;
import com.vekai.lact.model.PaymentSchedule;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LactLoanService {
    @Autowired
    SerialNoGeneratorFiller serialNoGeneratorFiller;
    @Autowired
    BeanCruder beanCruder;

    @Transactional
    public int deleteLactLoan(String lactLoanId){
        //1.先删除数据
        String[] sqlList = new String[]{
                "DELETE FROM LACT_LOAN             WHERE LOAN_ID=:loanId",
                "DELETE FROM LACT_LOAN_SEGMENT     WHERE LOAN_ID=:loanId",
                "DELETE FROM LACT_PAYMENT_SCHEDULE WHERE LOAN_ID=:loanId"
        };
        Map<String,Object> deleteParam = MapKit.mapOf("loanId",lactLoanId);

        int ret = 0;
        for(String sql : sqlList){
            ret += beanCruder.execute(sql,deleteParam);
        }
        return ret;
    }

    @Transactional
    public int saveLactLoan(LactLoan lactLoan){
        //1.先删除数据
        deleteLactLoan(lactLoan.getLoanId());

        //2.分别保存
        LactLoanEntity lactLoanEntity = new LactLoanEntity();
        BeanKit.copyProperties(lactLoan,lactLoanEntity);

        List<LactLoanSegmentEntity> segmentEntityList = new ArrayList<>(lactLoan.getSegmentList().size());
        List<PaymentScheduleEntity> scheduleEntitiesList = new ArrayList<>();
        lactLoan.getSegmentList().forEach(segment->{
            LactLoanSegmentEntity segmentBean = new LactLoanSegmentEntity();
            BeanKit.copyProperties(segment,segmentBean);
            segmentEntityList.add(segmentBean);
            segment.getScheduleList().forEach(schedule -> {
                PaymentScheduleEntity scheduleEntity = new PaymentScheduleEntity();
                BeanKit.copyProperties(schedule,scheduleEntity);
                scheduleEntitiesList.add(scheduleEntity);
            });
        });

        int ret =0;
        ret += beanCruder.save(lactLoanEntity);
        ret += beanCruder.save(segmentEntityList);
        ret += beanCruder.save(scheduleEntitiesList);

        return ret;

    }

    /**
     * 填充序列号
     * @param lactLoan
     */
    public void fillSerialNo(LactLoan lactLoan){
        serialNoGeneratorFiller.execFill(lactLoan);
        DecimalFormat numberFormat = new DecimalFormat("000");

        lactLoan.getSegmentList().forEach(segment->{
            segment.setLoanId(lactLoan.getLoanId());

            serialNoGeneratorFiller.execFill(segment);
            List<PaymentSchedule> scheduleList = segment.getScheduleList();
            for(int i=0;i<scheduleList.size();i++){
                PaymentSchedule schedule = scheduleList.get(i);
                schedule.setLoanId(lactLoan.getLoanId());
                schedule.setLoanSegmentId(segment.getLoanSegmentId());

                String rowId = segment.getLoanSegmentId()+numberFormat.format(i+1);
                schedule.setScheduleId(rowId);
            }
        });
    }
}
