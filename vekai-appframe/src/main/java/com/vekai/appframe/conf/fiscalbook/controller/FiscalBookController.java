package com.vekai.appframe.conf.fiscalbook.controller;

import com.vekai.appframe.conf.fiscalbook.service.FiscalBookConfService;
import com.vekai.base.dict.model.DictItemNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by luyu on 2018/6/6.
 */
@RestController
@RequestMapping("/configuration/fiscalBook")
public class FiscalBookController {

    @Autowired
    FiscalBookConfService fiscalBookService;

    @RequestMapping("/peroid/{bookCode}")
    public List<DictItemNode> getRecentPeriod(@PathVariable("bookCode")String bookCode){
        return fiscalBookService.getRecentPeriod(bookCode);
    }
}
