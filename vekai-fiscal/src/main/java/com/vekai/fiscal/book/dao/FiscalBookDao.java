package com.vekai.fiscal.book.dao;

import cn.fisok.sqloy.annotation.SqlParam;
import com.vekai.fiscal.book.model.FiscalBook;
import com.vekai.fiscal.book.model.FiscalBookAssit;
import com.vekai.fiscal.book.model.FiscalBookEntry;
import com.vekai.fiscal.book.model.FiscalBookPeriod;
import cn.fisok.sqloy.annotation.SQLDao;

import java.util.List;

@SQLDao
public interface FiscalBookDao {
    /**
     * 取一个账套数据对象
     * @param bookCode
     * @return
     */
    FiscalBook getFiscalBook(@SqlParam("bookCode") String bookCode);

    /**
     * 取账套科目列表
     * @param bookCode
     * @return
     */
    List<FiscalBookEntry> getFiscalBookEntries(@SqlParam("bookCode") String bookCode);

    /**
     * 取账套会计期间
     * @param bookCode
     * @return
     */
    List<FiscalBookPeriod> getFiscalBookPeriods(@SqlParam("bookCode") String bookCode);

    /**
     * 获取整个账套下的科目项
     * @param bookCode
     * @return
     */
    List<FiscalBookAssit> getFiscalBookAssits(@SqlParam("bookCode") String bookCode);


}
