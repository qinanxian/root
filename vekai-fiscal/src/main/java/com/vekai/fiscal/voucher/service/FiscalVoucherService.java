package com.vekai.fiscal.voucher.service;

import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.fiscal.book.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 保证凭证
 */
@Service
public class FiscalVoucherService {

    @Autowired
    BeanCruder beanCruder;
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    public int saveFiscalVoucher(FiscalVoucher voucher){
        beanCruder.save(voucher);
        String voucherId = voucher.getVoucherId();
        List<FiscalVoucherEntry> entries = voucher.getEntryList();
        for (FiscalVoucherEntry fiscalVoucherEntry: entries) {
            fiscalVoucherEntry.setVoucherId(voucherId);
            beanCruder.save(fiscalVoucherEntry);
            List<FiscalVoucherAssit> assitList = fiscalVoucherEntry.getAssitList();
            assitList = assitList.stream()
                    .map(assit -> this.initRelated(assit,fiscalVoucherEntry.getVoucherEntryId()))
                    .collect(Collectors.toList());
            beanCruder.save(assitList);
        }
        return 1;
    }

    private FiscalVoucherAssit initRelated(FiscalVoucherAssit assit, String voucherEntryId) {
        assit.setVoucherEntryId(voucherEntryId);
        return assit;
    }

    /**
     * 计算凭证字号的字
     * @param fiscalBook
     * @return
     */
    public String getVoucherWord(FiscalBook fiscalBook){
        String voucherWord = fiscalBook.getVoucherWord();
        String voucherWordName = "记";
        if("20".equalsIgnoreCase(voucherWord)){ //收、付、转
            voucherWordName = "收付";
        }else if("30".equalsIgnoreCase(voucherWord)){ //现收、现付、银收、银付
            voucherWordName = "现收付";
        }else if("40".equalsIgnoreCase(voucherWord)){ //转
            voucherWordName = "转";
        }
        return voucherWordName;
    }
    /**
     * 计算凭证字号的号
     * @return
     */
    public int getVoucherWordSeq(FiscalBook fiscalBook){
        String voucherWordName = getVoucherWord(fiscalBook);

        FiscalBookPeriod curPeriod = fiscalBook.getActivePeriod();
        lockCurencPeriod(curPeriod);//锁定会计期间
        String maxSql = "select max(VOUCHER_NO) from FISC_VOUCHER where BOOK_CODE=:bookCode and PERIOD_ID=:periodId and VOUCHER_WORD=:voucherWord";
        Integer maxNo = jdbcTemplate.queryForObject(maxSql
                ,MapKit.mapOf("bookCode",fiscalBook.getBookCode(),
                        "periodId",curPeriod.getPeriodId(),
                        "voucherWord",voucherWordName
                ),
                Integer.class
        );

        if(maxNo == null){
            maxNo = 0;
        }
        maxNo += 1;

        return maxNo;
    }

    /**
     * 为了计算凭证字，需要锁定当前记账期间
     */
    private void lockCurencPeriod(FiscalBookPeriod period){
        //在数据库上锁定会计期间，防止凭证字号重复
        String lockSql = "update FISC_BOOK_PERIOD set REVISION=REVISION where PERIOD_ID=:periodId";
        beanCruder.execute(lockSql, MapKit.mapOf("periodId",period.getPeriodId()));
    }

}
