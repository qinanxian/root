package com.vekai.lact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vekai.lact.entity.LactLoanSegmentEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LactLoanSegment extends LactLoanSegmentEntity implements Serializable,Cloneable{

    @JsonIgnore
    protected LactLoan loan = null;
    protected List<PaymentSchedule> scheduleList = new ArrayList<PaymentSchedule>();

    public LactLoanSegment(LactLoan loan) {
        this.loan = loan;
    }

    public LactLoan getLoan() {
        return loan;
    }

    public void setLoan(LactLoan loan) {
        this.loan = loan;
    }

    public List<PaymentSchedule> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<PaymentSchedule> scheduleList) {
        this.scheduleList = scheduleList;
    }
}
