package com.vekai.base.dictionary.service;

import com.vekai.base.dict.model.DictEntry;
import com.vekai.base.dict.model.DictItemEntry;
import com.vekai.base.dict.service.DictService;
import com.vekai.base.BaseTest;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhulifeng on 17-12-21.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DictServiceTest extends BaseTest {

    @Autowired
    DictService dictService;

    @Test
    public void test01GetDict() {
        DictEntry dictionary = dictService.getDict("Gender");
        Assert.assertNotNull(dictionary);
    }

    @Test
    public void test02GetDictByFilter() {
        DictEntry dictionary = dictService.getDictByFilter("Gender", "2");
        Assert.assertNotNull(dictionary);
    }

    @Test
    public void test03GetDictList() {
        List<DictEntry> dictionaries = dictService.getDictList();
        Assert.assertNotNull(dictionaries);
    }

    @Test
    @Transactional
    @Rollback
    public void test04SaveDictEntry() {
        DictEntry dictEntry = new DictEntry();
        dictEntry.setCode("YesOrNo");
        dictEntry.setName("是否");
        dictEntry.setIntro("是否的码表111");
        dictEntry.setSortCode("0002");

        Map<String, DictItemEntry> itemMap = new HashMap<>();
        DictItemEntry dictItemEntry = new DictItemEntry();
        dictItemEntry.setCode("Y");
        dictItemEntry.setName("是");
        dictItemEntry.setSortCode("1");
        dictItemEntry.setHotspot(1);
        dictItemEntry.setCorrelation("1=1");
        dictItemEntry.setStatus("1");
        dictItemEntry.setNameI18nCode("Y");
        dictItemEntry.setSummary("是");
        itemMap.put("Y", dictItemEntry);

        dictItemEntry = new DictItemEntry();
        dictItemEntry.setCode("N");
        dictItemEntry.setName("否");
        dictItemEntry.setSortCode("2");
        dictItemEntry.setHotspot(2);
        dictItemEntry.setCorrelation("2=2");
        dictItemEntry.setStatus("1");
        dictItemEntry.setNameI18nCode("N");
        dictItemEntry.setSummary("否");
        itemMap.put("N", dictItemEntry);

        dictEntry.setItemMap(itemMap);

        dictService.save(dictEntry);
    }

    @Test
    @Transactional
    @Rollback
    public void test05SaveDictItemEntry() {
        DictItemEntry dictItemEntry = new DictItemEntry();
        dictItemEntry.setCode("YY");
        dictItemEntry.setName("是是");
        dictItemEntry.setSortCode("3");
        dictItemEntry.setHotspot(3);
        dictItemEntry.setCorrelation("3=3");
        dictItemEntry.setStatus("1");
        dictItemEntry.setNameI18nCode("YY");
        dictItemEntry.setSummary("是是");
        dictService.save("YesOrNo", dictItemEntry);
    }

    @Test
    @Transactional
    @Rollback
    public void test06DeleteDictItemEntry() {
        dictService.delete("YesOrNo", "YY");
    }

    @Test
    @Transactional
    @Rollback
    public void test07DeleteDictEntry() {
        dictService.delete("YesOrNo");
    }



}
