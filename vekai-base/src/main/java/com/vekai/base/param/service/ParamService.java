package com.vekai.base.param.service;

import cn.fisok.raw.lang.TreeNodeWrapper;
import com.vekai.base.param.model.ParamEntry;
import com.vekai.base.param.model.ParamItemEntry;

import java.util.List;

public interface ParamService {

    ParamEntry getParam(String paramCode);

    List<TreeNodeWrapper<ParamItemEntry>> getParamItemsAsTree(String paramCode);

    List<ParamItemEntry> getParamItemsByFilter(String paramCode, String startSort);

    List<TreeNodeWrapper<ParamItemEntry>> getParamItemsAsTreeByFilter(String paramCode, String startSort);

    Boolean getParamItemIsExist(String paramCode, String code);

    List<String> getCodesByParamCode(String paramCode);
}
