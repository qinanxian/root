package com.vekai.base.dict.service.impl;

import com.vekai.base.dict.model.DictEntry;
import com.vekai.base.dict.model.DictItemEntry;
import com.vekai.base.dict.service.impl.po.DictItemPO;
import com.vekai.base.dict.service.impl.po.DictPO;
import com.vekai.base.dict.model.DictItemNode;
import com.vekai.base.dict.service.DictService;
import com.vekai.base.kit.PinyinKit;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.ListKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.TreeNodeKit;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("dictService")
public class DictServiceImpl implements DictService {
    public static final String CACHE_KEY = "DictCache";

    @Autowired
    protected BeanCruder accessor;

    @Lazy
    @Autowired
    protected DictServiceImpl self;

    /**
     * 清空缓存
     */
    @CacheEvict(value=CACHE_KEY,allEntries=true,beforeInvocation=true)
    public void clearCacheAll(){

    }

    /**
     * 清空缓存
     */
    @CacheEvict(value=CACHE_KEY,key="#dictCode",beforeInvocation=true)
    public void clearCacheItem(String dictCode){

    }

    @Cacheable(value=CACHE_KEY,key="#dictCode")
    public DictEntry getDict(String dictCode) {
        DictPO dictPO = getDictPO(dictCode);
        DictEntry dictionary = new DictEntry();
        if (null != dictPO) {
            BeanKit.copyProperties(dictPO, dictionary);
            Map<String, DictItemEntry> itemMap = getDictItemMap(dictCode);
            dictionary.setItemMap(itemMap);
        }

        return dictionary;
    }

    //attention: use java8 with 1.8+JDK, if use 1.7JDK, should update this code
    public DictEntry getDictByFilter(String dictCode, String startSort) {
        DictEntry dictEntry = self.getDict(dictCode);    //这样调用，才能让缓存生效
        List<DictItemEntry> dictItems = new ArrayList<>(dictEntry.getItems());
        Map<String, DictItemEntry> map = dictItems.stream()
            .filter(item -> item.getSortCode().startsWith(startSort))
            .collect(Collectors.toMap(DictItemEntry::getCode, c -> c));
        dictEntry.setItemMap(map);

        return dictEntry;
    }

    public List<DictItemNode> getDictTree(String dictCode) {
        DictEntry entry = self.getDict(dictCode);
        List<DictItemEntry> items = entry.getItems();
        if (null == items || items.isEmpty()) return Collections.emptyList();
        List<DictItemNode> nodeList = items.stream().map(dictItemEntry -> {
            DictItemNode dictItemNode = new DictItemNode();
            dictItemNode.setValues(dictItemEntry);
            return dictItemNode;
        }).collect(Collectors.toList());
        return TreeNodeKit.buildTree(nodeList, DictItemNode::getSortCode);
    }

    public List<DictEntry> getDictList() {
        List<DictEntry> dictionaries = new ArrayList<>();
        List<DictPO> dictPOList = accessor.selectList(DictPO.class, "select * from FOWK_DICT ORDER BY SORT_CODE ASC,CODE ASC");
        List<DictItemPO> dictItemPOList = accessor.selectList(DictItemPO.class, "select * from FOWK_DICT_ITEM ORDER BY DICT_CODE ASC,SORT_CODE ASC,CODE ASC");


        if (null != dictPOList && !dictPOList.isEmpty()) {
            for (DictPO dictPO : dictPOList) {
                DictEntry dictionary = new DictEntry();
                BeanKit.copyProperties(dictPO, dictionary);
//                itemMap = getDictItemMap(dictionary.getCode());
                List<DictItemEntry> items = ListKit.newArrayList();
                Map<String, DictItemEntry> itemMap = getDictItemMap(dictionary.getCode(),dictItemPOList);
                items.addAll(itemMap.values());

//                dictionary.setItems(items);
                dictionary.setItemMap(itemMap);
                dictionaries.add(dictionary);
            }
        }

        return dictionaries;
    }

    public List<DictItemEntry> getDictItemHotspot(String dictCode, int hotspot) {
        List<DictItemEntry> dictionaries = new ArrayList<>();
        List<DictItemPO> dictItemPOList = getDictItemHotspotPOList(dictCode, hotspot);
        if (null != dictItemPOList && !dictItemPOList.isEmpty()) {
            DictItemEntry dictItemEntry;
            for (DictItemPO dictItemPO : dictItemPOList) {
                dictItemEntry = new DictItemEntry();
                BeanKit.copyProperties(dictItemPO, dictItemEntry);
                dictionaries.add(dictItemEntry);
            }
        }

        return dictionaries;
    }

    public int save(DictEntry dictEntry) {
        int result = 0;
        DictPO dictPO = new DictPO();
        BeanKit.copyProperties(dictEntry, dictPO);
        result = accessor.save(dictPO);

        String dictCode = dictEntry.getCode();
        List<DictItemEntry> dictItems = dictEntry.getItems();
        if (null != dictItems && !dictItems.isEmpty()) {
            List<DictItemPO> dictItemPOList = new ArrayList<>();
            DictItemPO dictItemPO;
            for (DictItemEntry dictItem : dictItems) {
                dictItemPO = new DictItemPO();
                BeanKit.copyProperties(dictItem, dictItemPO);
                dictItemPO.setDictCode(dictCode);
                dictItemPOList.add(dictItemPO);
            }
            result = accessor.save(dictItemPOList);
        }

        return result;
    }

    public int save(String dictCode, DictItemEntry dictItemEntry) {
        self.clearCacheItem(dictCode);//保存时，把缓存清了

        DictItemPO dictItemPO = new DictItemPO();
        BeanKit.copyProperties(dictItemEntry, dictItemPO);
        dictItemPO.setDictCode(dictCode);
        return accessor.save(dictItemPO);

    }

    public int delete(String dictCode) {
        self.clearCacheItem(dictCode);//删除时，把缓存清了

        int result = 0;
        DictPO dictPO = getDictPO(dictCode);
        if (null != dictPO) {
            result = accessor.delete(dictPO);
        }

        List<DictItemPO> dictItemPOList = getDictItemPOList(dictCode);
        if (null != dictItemPOList && !dictItemPOList.isEmpty()) {
            result = accessor.delete(dictItemPOList);
        }

        return result;
    }

    @Override
    public int deleteAll() {
        self.clearCacheAll();
        int r = accessor.delete(DictEntry.class);
        accessor.delete(DictItemEntry.class);
        return r;
    }

    public int delete(String dictCode, String dictItemCode) {
        self.clearCacheItem(dictCode);//删除时，把缓存清了

        int result = 0;
        DictItemPO dictItemPO = getDictItemPO(dictCode, dictItemCode);
        if (null != dictItemPO) {
            result = accessor.delete(dictItemPO);
        }

        return result;
    }

    private Map<String, DictItemEntry> getDictItemMap(List<DictItemPO> dictItemPOList){
        Map<String, DictItemEntry> itemPOMap = new LinkedHashMap<>();
        if (null != dictItemPOList && !dictItemPOList.isEmpty()) {
            DictItemEntry dictItem;
            for (DictItemPO dictItemPO : dictItemPOList) {
                dictItem = new DictItemEntry();
                BeanKit.copyProperties(dictItemPO, dictItem);
                if(StringKit.isNotBlank(dictItem.getName())){
                    dictItem.setNamePinyin(PinyinKit.hanziToPinyin(dictItem.getName()));
                }
                itemPOMap.put(dictItemPO.getCode(), dictItem);
            }
        }
        return itemPOMap;
    }


    public Map<String, DictItemEntry> getDictItemMap(String dictCode,List<DictItemPO> allDictItemPOList) {
        Map<String, DictItemEntry> itemPOMap = new LinkedHashMap<>();
//        List<DictItemPO> dictItemPOList = getDictItemPOList(dictCode);
        List<DictItemPO> dictItemPOList = allDictItemPOList.stream()
                .filter(itemPo->dictCode.equals(itemPo.getDictCode()))
                .collect(Collectors.toList());
        return getDictItemMap(dictItemPOList);
    }


    public Map<String, DictItemEntry> getDictItemMap(String dictCode) {
        Map<String, DictItemEntry> itemPOMap = new LinkedHashMap<>();
        List<DictItemPO> dictItemPOList = getDictItemPOList(dictCode);
        return getDictItemMap(dictItemPOList);
    }

    private DictPO getDictPO(String dictCode) {
        DictPO dictPO = accessor.selectOneById(DictPO.class, dictCode);
        return dictPO;
    }

    private List<DictItemPO> getDictItemPOList(String dictCode) {
        List<DictItemPO> dictItemPOList = accessor
            .selectList(DictItemPO.class, "select * from FOWK_DICT_ITEM where DICT_CODE=:dictCode AND STATUS='1' ORDER BY SORT_CODE ASC",
                "dictCode", dictCode);
        return dictItemPOList;
    }

    private List<DictItemPO> getDictItemHotspotPOList(String dictCode, int hotspot) {
        List<DictItemPO> dictItemPOList = accessor.selectList(DictItemPO.class,
            "select * from FOWK_DICT_ITEM where DICT_CODE=:dictCode and HOTSPOT=:hotspot AND STATUS='1' ORDER BY SORT_CODE ASC",
            "dictCode", dictCode, "HOTSPOT", hotspot);
        return dictItemPOList;
    }

    private DictItemPO getDictItemPO(String dictCode, String dictItemCode) {
        DictItemPO dictItemPO = accessor.selectOne(DictItemPO.class,
            "select * from FOWK_DICT_ITEM where DICT_CODE=:dictCode and CODE=:code",
            "dictCode", dictCode, "code", dictItemCode);
        return dictItemPO;
    }
}
