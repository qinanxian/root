package com.vekai.crops.othApplications.qcfq.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="QCFQ_APPLY_PRODUCT")
public class Apply {
    @Id
    @GeneratedValue
    private String productId;
    private String productName;

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
