package com.vekai.fiscal.book.service;

import com.vekai.fiscal.book.dao.FiscalBookDao;
import com.vekai.fiscal.book.model.FiscalBook;
import com.vekai.fiscal.book.model.FiscalBookAssit;
import com.vekai.fiscal.book.model.FiscalBookEntry;
import com.vekai.fiscal.book.model.FiscalBookPeriod;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FiscalBookService {
    @Autowired
    FiscalBookDao fiscalBookDao;

    /**
     * 获取一个账套下+科目明细|科目辅助+会计期间
     * @param bookCode
     * @return
     */
    public FiscalBook getFiscalBook(String bookCode){
        FiscalBook fiscalBook = fiscalBookDao.getFiscalBook(bookCode);
        if(fiscalBook==null)return null;
        List<FiscalBookEntry> entries = fiscalBookDao.getFiscalBookEntries(bookCode);
        List<FiscalBookPeriod> periods = fiscalBookDao.getFiscalBookPeriods(bookCode);
        List<FiscalBookAssit> assits = fiscalBookDao.getFiscalBookAssits(bookCode);

        //科目明细
        Map<String,FiscalBookEntry> entryMap = MapKit.newLinkedHashMap();
        Map<String,FiscalBookEntry> entryByIdMap = MapKit.newLinkedHashMap();
        entries.forEach(entry->{
            String entryCode = entry.getEntryCode();
            String entryId = entry.getBookEntryId();
            if(StringKit.isNotBlank(entryCode)){
                entryMap.put(entryCode,entry);
            }
            if(StringKit.isNotBlank(entryId)){
                entryByIdMap.put(entryId,entry);
            }
        });
        fiscalBook.setEntryMap(entryMap);

        //会计期间
        Map<Integer,FiscalBookPeriod> periodMap = MapKit.newLinkedHashMap();
        periods.forEach(period->{
            periodMap.put(period.getPeriodTerm(),period);
        });
        fiscalBook.setPeriodMap(periodMap);

        //科目辅助
        assits.forEach(assit->{
            FiscalBookEntry entry = entryByIdMap.get(assit.getBookEntryId());
            if(entry==null)return;
            entry.getAssitList().add(assit);
        });

        return fiscalBook;
    }


}
