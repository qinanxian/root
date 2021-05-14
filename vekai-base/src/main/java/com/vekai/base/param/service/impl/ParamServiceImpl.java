package com.vekai.base.param.service.impl;

import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.lang.TreeNode;
import cn.fisok.raw.lang.TreeNodeWrapper;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.base.param.service.impl.po.ParamPO;
import com.vekai.base.param.model.ParamEntry;
import com.vekai.base.param.model.ParamItemEntry;
import com.vekai.base.param.service.ParamService;
import com.vekai.base.param.service.impl.po.ParamItemPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ParamServiceImpl implements ParamService {

    @Autowired
    BeanCruder accessor;

    public ParamEntry getParam(String paramCode) {
        ParamPO paramPO = getParamPO(paramCode);
        ParamEntry param = new ParamEntry();
        if (null != paramPO) {
            BeanKit.copyProperties(paramPO, param);
            Map<String, ParamItemEntry> itemMap = getParamItemMap(paramCode);
            param.setItemMap(itemMap);
        }

        return param;
    }

    public List<ParamItemEntry> getParamItemsByFilter(String paramCode, String startSort) {
        List<ParamItemPO> paramItemPOList = getParamItemPOListBySortNo(paramCode, startSort);
        List<ParamItemEntry> result = new ArrayList<ParamItemEntry>();
        if (null != paramItemPOList && !paramItemPOList.isEmpty()) {
            ParamItemEntry paramItem;
            for (ParamItemPO paramItemPO : paramItemPOList) {
                paramItem = new ParamItemEntry();
                BeanKit.copyProperties(paramItemPO, paramItem);
                result.add(paramItem);
            }
        }
        return result;
    }

    public List<TreeNodeWrapper<ParamItemEntry>> getParamItemsAsTreeByFilter(String paramCode, String startSort) {
        List<ParamItemEntry> paramItems = getParamItemsByFilter(paramCode, startSort);

        return getParamItemsAsTree(paramItems);
    }


    @Override
    public Boolean getParamItemIsExist(String paramCode, String code) {
        ParamItemPO paramItemPO = accessor.selectOne(ParamItemPO.class
                , "select * from FOWK_PARAM_ITEM where PARAM_CODE=:paramCode AND code=:code"
                , "paramCode", paramCode, "code", code);
        if (null != paramItemPO) {
            return true;
        }
        return false;
    }

    @Override
    public List<String> getCodesByParamCode(String paramCode) {
        return accessor.selectList(String.class, "select code from FOWK_PARAM_ITEM where PARAM_CODE=:paramCode", "paramCode", paramCode);
    }

    public List<TreeNodeWrapper<ParamItemEntry>> getParamItemsAsTree(String paramCode) {
        List<ParamItemPO> paramItemPOList = getParamItemPOList(paramCode);
        List<ParamItemEntry> paramItems = new ArrayList<>();
        if (null != paramItemPOList && !paramItemPOList.isEmpty()) {
            ParamItemEntry paramItem;
            for (ParamItemPO po : paramItemPOList) {
                paramItem = new ParamItemEntry();
                BeanKit.copyProperties(po, paramItem);
                paramItems.add(paramItem);
            }
        }

        return getParamItemsAsTree(paramItems);
    }

    public List<TreeNodeWrapper<ParamItemEntry>> getParamItemsAsTree(List<ParamItemEntry> paramItems) {
        List<TreeNodeWrapper<ParamItemEntry>> paramItemTrees = new ArrayList<TreeNodeWrapper<ParamItemEntry>>();
        List<String> usedSortCodes = new ArrayList<>();
        TreeNodeWrapper<ParamItemEntry> paramItemTree;
        for (int i = 0; i < paramItems.size(); i++) {
            ParamItemEntry curParamItem = paramItems.get(i);
            String curSortCode = curParamItem.getSortCode();

            if (usedSortCodes.contains(curSortCode)) {
                continue;
            }
            usedSortCodes.add(curSortCode);

            paramItemTree = new TreeNodeWrapper<ParamItemEntry>();
            paramItemTree.setValue(curParamItem);

            List<TreeNodeWrapper<ParamItemEntry>> childrenParamItemTrees =
                    getChildrenParamItemTree(paramItems, usedSortCodes, paramItemTree, curSortCode);
            if (!childrenParamItemTrees.isEmpty()) {
                paramItemTree.setChildren(childrenParamItemTrees);
            }

            paramItemTrees.add(paramItemTree);
        }

        return paramItemTrees;
    }


    private Map<String, ParamItemEntry> getParamItemMap(String paramCode) {
        Map<String, ParamItemEntry> itemPOMap = new HashMap<>();
        List<ParamItemPO> paramItemPOList = getParamItemPOList(paramCode);
        if (null != paramItemPOList && !paramItemPOList.isEmpty()) {
            ParamItemEntry paramItem;
            for (ParamItemPO paramItemPO : paramItemPOList) {
                paramItem = new ParamItemEntry();
                BeanKit.copyProperties(paramItemPO, paramItem);
                itemPOMap.put(paramItemPO.getCode(), paramItem);
            }
        }

        return itemPOMap;
    }

    private ParamPO getParamPO(String paramCode) {
        ParamPO paramPO = accessor.selectOneById(ParamPO.class, paramCode);
        return paramPO;
    }

    private List<ParamItemPO> getParamItemPOList(String paramCode) {
        List<ParamItemPO> paramItemPOList = accessor
                .selectList(ParamItemPO.class, "select * from FOWK_PARAM_ITEM where PARAM_CODE=:paramCode ORDER BY SORT_CODE ASC",
                        "paramCode", paramCode);
        return paramItemPOList;
    }

    private List<ParamItemPO> getParamItemPOListBySortNo(String paramCode, String sortNo) {
        List<ParamItemPO> paramItemPOList = accessor
                .selectList(ParamItemPO.class, "select * from FOWK_PARAM_ITEM where PARAM_CODE=:paramCode and SORT_CODE like CONCAT(:sortNo, '%') ORDER BY SORT_CODE ASC",
                        "paramCode", paramCode, "sortNo", sortNo);
        return paramItemPOList;
    }

    private List<TreeNodeWrapper<ParamItemEntry>> getChildrenParamItemTree(List<ParamItemEntry> paramItems, List<String> usedSortCodes,
                                                                           TreeNodeWrapper<ParamItemEntry> parent, String sortCode) {
        List<TreeNodeWrapper<ParamItemEntry>> result = new ArrayList<TreeNodeWrapper<ParamItemEntry>>();
        TreeNodeWrapper<ParamItemEntry> paramItemTree;
        for (int i = 0; i < paramItems.size(); i++) {
            ParamItemEntry curParamItem = paramItems.get(i);
            String curSortCode = curParamItem.getSortCode();

            if (curParamItem.getSortCode().equals(sortCode) || !curSortCode.startsWith(sortCode)) {
                continue;
            }

            if (usedSortCodes.contains(curSortCode)) {
                continue;
            }
            usedSortCodes.add(curSortCode);

            paramItemTree = new TreeNodeWrapper<ParamItemEntry>();
            paramItemTree.setValue(curParamItem);

            List<TreeNodeWrapper<ParamItemEntry>> childrenParamItemTrees =
                    getChildrenParamItemTree(paramItems, usedSortCodes, paramItemTree, curSortCode);
            if (!childrenParamItemTrees.isEmpty()) {
                paramItemTree.setChildren(childrenParamItemTrees);
            }

            result.add(paramItemTree);
        }

        return result;
    }


}
