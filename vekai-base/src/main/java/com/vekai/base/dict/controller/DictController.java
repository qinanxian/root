package com.vekai.base.dict.controller;

import com.vekai.base.dict.model.DictEntry;
import com.vekai.base.dict.model.DictItemNode;
import com.vekai.base.dict.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/base")
public class DictController {
    @Autowired
    private DictService dictService;

    @GetMapping("/dicts")
    public List<DictEntry> getDictList() {
        return dictService.getDictList();
    }

    @GetMapping("/dicts/{dictCode}")
    public DictEntry getDict(@PathVariable("dictCode") String dictCode) {
        return dictService.getDict(dictCode);
    }

    @GetMapping("/dicts/{dictCode}/sort-filter/{startSort}")
    public DictEntry getDictByFilter(@PathVariable("dictCode") String dictCode,
                                     @PathVariable("startSort") String startSort) {
        return dictService.getDictByFilter(dictCode, startSort);
    }

    @GetMapping("/dicts/{dictCode}/tree")
    public List<DictItemNode> getDictTree(@PathVariable("dictCode") String dictCode) {
        return dictService.getDictTree(dictCode);
    }


}