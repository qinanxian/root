package com.vekai.appframe.conf.fiscalbook.service;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.base.dict.model.DictItemEntry;
import com.vekai.base.dict.model.DictItemNode;
import com.vekai.fiscal.autoconfigure.FiscalProperties;
import com.vekai.fiscal.entity.ConfFiscGaapAssistPO;
import com.vekai.fiscal.entity.ConfFiscGaapEntryPO;
import com.vekai.fiscal.entity.FiscBookPO;
import com.vekai.fiscal.entity.FiscVoucherPO;
import cn.fisok.raw.holder.MessageHolder;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.lang.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by luyu on 2018/6/5.
 */
@Service
public class FiscalBookConfService {

    @Autowired
    BeanCruder beanCruder;
    @Autowired
    FiscalProperties fiscalProperties;

    public List<ConfFiscGaapEntryPO> getConfFiscGaapEntry(String gaapDef) {
        String sql = "SELECT * FROM CONF_FISC_GAAP_ENTRY WHERE GAAP_DEF=:gaapDef";
        List<ConfFiscGaapEntryPO> confFiscGaapEntryPOList = beanCruder.selectList(ConfFiscGaapEntryPO.class,sql,
                "gaapDef",gaapDef);
        return confFiscGaapEntryPOList;
    }

    public List<FiscVoucherPO> getFiscVoucher(String bookCode) {
        String sql = "SELECT * FROM FISC_VOUCHER WHERE BOOK_CODE=:bookCode";
        List<FiscVoucherPO> fiscVoucherPOList = beanCruder.selectList(FiscVoucherPO.class,sql,
                "bookCode",bookCode);
        return fiscVoucherPOList;
    }

    public FiscBookPO getFiscBook(String bookCode) {
        String sql = "SELECT * FROM FISC_BOOK WHERE BOOK_CODE=:bookCode";
        FiscBookPO fiscBookPO = beanCruder.selectOne(FiscBookPO.class,sql,"bookCode",bookCode);
        return fiscBookPO;
    }

    public List<DictItemNode> getRecentPeriod(String bookCode) {
        FiscBookPO fiscBookPO = this.getFiscBook(bookCode);
        Optional.ofNullable(fiscBookPO).orElseThrow(() ->
                new BizException(MessageHolder.getMessage("","conf.fiscal.book.not.exist")));
        List<DictItemEntry> items = new ArrayList<DictItemEntry>();
        List<DictItemNode> dictItems = null;
        Integer startYear = fiscBookPO.getStartYear();
        Integer endYear = fiscBookPO.getEndYear();
        if (endYear == null || endYear == 0)
            endYear = startYear + fiscalProperties.getBookDefaultInitYear() - 1;
        for(int i = startYear; i <= endYear; i++){
            DictItemEntry entry = new DictItemEntry();
            entry.setCode(Integer.toString(i));
            entry.setName(Integer.toString(i));
            int isInited = this.getPeriodHasInited(bookCode,i);
            entry.setStatus(isInited > 0 ? "Y":"N");
            items.add(entry);
        }
        dictItems = toDictItemNodes(items);
        return dictItems;
    }

    private int getPeriodHasInited(String bookCode, Integer year) {
        String sql = "SELECT * FROM FISC_BOOK_PERIOD WHERE BOOK_CODE=:bookCode AND PERIOD_YEAR=:year";
        Integer result = beanCruder.selectCount(sql, MapKit.mapOf("bookCode",bookCode,"year",year));
        return result;
    }

    private List<DictItemNode> toDictItemNodes(List<DictItemEntry> items){
        List<DictItemNode> dictItems = null;
        dictItems = new ArrayList<>();
        for(DictItemEntry item : items){
            DictItemNode node = new DictItemNode();
            node.setValues(item);
            dictItems.add(node);
        }
        return dictItems;
    }

    public List<ConfFiscGaapAssistPO> getConfFiscGaapAssists(String gaapEntryDef) {
        String sql = "SELECT * FROM CONF_FISC_GAAP_ASSIST WHERE GAAP_ENTRY_DEF=:gaapEntryDef";
        List<ConfFiscGaapAssistPO> confFiscGaapAssistPOS = beanCruder.selectList(ConfFiscGaapAssistPO.class,sql,"gaapEntryDef",gaapEntryDef);
        return confFiscGaapAssistPOS;
    }
}
