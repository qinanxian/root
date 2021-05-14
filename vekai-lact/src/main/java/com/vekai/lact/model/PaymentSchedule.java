package com.vekai.lact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vekai.lact.entity.PaymentScheduleEntity;

import java.io.Serializable;

public class PaymentSchedule extends PaymentScheduleEntity implements Serializable,Cloneable {
    @JsonIgnore
    protected LactLoanSegment segment = null;

    public PaymentSchedule(LactLoanSegment segment) {
        this.segment = segment;
    }

    public LactLoanSegment getSegment() {
        return segment;
    }

    public void setSegment(LactLoanSegment segment) {
        this.segment = segment;
    }
}
