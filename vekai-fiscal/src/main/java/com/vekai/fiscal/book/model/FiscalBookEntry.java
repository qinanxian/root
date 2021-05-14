package com.vekai.fiscal.book.model;

import com.vekai.fiscal.entity.FiscBookEntryPO;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 账套科目
 */
public class FiscalBookEntry extends FiscBookEntryPO implements Serializable,Cloneable {
    @Transient
    private List<FiscalBookAssit> assitList = new ArrayList<>();

    public List<FiscalBookAssit> getAssitList() {
        return assitList;
    }

    public void setAssitList(List<FiscalBookAssit> assitList) {
        this.assitList = assitList;
    }
}
