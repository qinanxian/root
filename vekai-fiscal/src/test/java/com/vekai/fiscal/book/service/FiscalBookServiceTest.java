package com.vekai.fiscal.book.service;

import com.vekai.fiscal.book.model.FiscalBook;
import com.vekai.fiscal.BaseTest;
import cn.fisok.raw.kit.JSONKit;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FiscalBookServiceTest extends BaseTest {
    @Autowired
    FiscalBookService fiscalBookService;

    @Test
    public void testGetBook(){
        FiscalBook fiscalBook = fiscalBookService.getFiscalBook("FB0003");
        System.out.print(JSONKit.toJsonString(fiscalBook,true));
    }
}
