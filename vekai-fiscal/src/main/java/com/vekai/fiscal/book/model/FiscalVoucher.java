package com.vekai.fiscal.book.model;

import com.vekai.fiscal.entity.FiscVoucherPO;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 凭证
 */
public class FiscalVoucher extends FiscVoucherPO implements Serializable,Cloneable{
    @Transient
    private List<FiscalVoucherEntry> entryList = new ArrayList();

    @Transient
    public List<FiscalVoucherEntry> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<FiscalVoucherEntry> entryList) {
        this.entryList = entryList;
    }
}
