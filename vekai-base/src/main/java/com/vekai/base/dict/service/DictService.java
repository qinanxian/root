package com.vekai.base.dict.service;

import com.vekai.base.dict.model.DictEntry;
import com.vekai.base.dict.model.DictItemEntry;
import com.vekai.base.dict.model.DictItemNode;

import java.util.List;

public interface DictService {

    DictEntry getDict(String dictCode);

    DictEntry getDictByFilter(String dictCode, String startSort);

    List<DictItemNode> getDictTree(String dictCode);

    List<DictEntry> getDictList();

    List<DictItemEntry> getDictItemHotspot(String dictCode, int hotspot);

    int save(DictEntry dictEntry);

    int save(String dictCode, DictItemEntry dictItemEntry);

    int delete(String dictCode);

    int delete(String dictCode, String dictItemCode);

    int deleteAll();

    void clearCacheAll();

    void clearCacheItem(String dictCode);
}
