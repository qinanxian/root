package com.vekai.fiscal.book.model;

import com.vekai.fiscal.entity.FiscVoucherEntryPO;
import cn.fisok.raw.kit.ListKit;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * 凭证科目明细
 */
public class FiscalVoucherEntry extends FiscVoucherEntryPO implements Serializable,Cloneable{
    @Transient
    public List<FiscalVoucherAssit> assitList = ListKit.newArrayList();

    public List<FiscalVoucherAssit> getAssitList() {
        return assitList;
    }

    public void setAssitList(List<FiscalVoucherAssit> assitList) {
        this.assitList = assitList;
    }
}
