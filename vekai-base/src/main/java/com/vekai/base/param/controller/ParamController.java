package com.vekai.base.param.controller;

import cn.fisok.raw.lang.TreeNodeWrapper;
import com.vekai.base.param.service.ParamService;
import com.vekai.base.param.model.ParamEntry;
import com.vekai.base.param.model.ParamItemEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/base")
public class ParamController {
    @Autowired
    private ParamService paramService;

    @GetMapping("/params/{paramCode}")
    public ParamEntry getParam(@PathVariable("paramCode") String paramCode) {
        return paramService.getParam(paramCode);
    }

    @GetMapping("/params/itemsTree/{paramCode}")
    public List<TreeNodeWrapper<ParamItemEntry>> getParamItemsAsTree(@PathVariable("paramCode") String paramCode) {
        return paramService.getParamItemsAsTree(paramCode);
    }

    @GetMapping("/params/{paramCode}/items/sort-filter/{startSort}")
    public List<ParamItemEntry> getParamItemsByFilter(@PathVariable("paramCode") String paramCode,
                                                      @PathVariable("startSort") String startSort) {
        return paramService.getParamItemsByFilter(paramCode, startSort);
    }

    @GetMapping("/params/{paramCode}/itemsTree/sort-filter/{startSort}")
    public List<TreeNodeWrapper<ParamItemEntry>> getParamItemsAsTreeByFilter(@PathVariable("paramCode") String paramCode,
                                                            @PathVariable("startSort") String startSort) {
        return paramService.getParamItemsAsTreeByFilter(paramCode, startSort);
    }

    @GetMapping("/param/{paramCode}/{code}/is/exist")
    public Boolean getParamItemIsExist(@PathVariable("paramCode") String paramCode,
                                       @PathVariable("code") String code) {
        return paramService.getParamItemIsExist(paramCode, code);
    }

    @GetMapping("/params/codes/{paramCode}")
    public List<String> getCodesByParamCode(@PathVariable("paramCode") String paramCode) {
        return paramService.getCodesByParamCode(paramCode);
    }
}