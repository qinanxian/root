package com.vekai.appframe.base.dict.handler;

import cn.fisok.sqloy.core.PaginResult;
import com.vekai.base.dict.service.impl.po.DictPO;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.mapper.DataFormMapper;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DictListHandler extends BeanDataListHandler<DictPO> {

    @Autowired
    BeanCruder accessor;
    @Autowired
    DataFormMapper dataFormMapper;

    public Integer delete(DataForm dataForm, List<DictPO> dataList) {
        Integer result = super.delete(dataForm, dataList);
        if (result > 0) {
            for (DictPO dict : dataList) {
                String dictCode = dict.getCode();
                result = result + accessor.execute("DELETE FROM FOWK_DICT_ITEM WHERE DICT_CODE=:dictCode", MapKit.mapOf("dictCode", dictCode));
            }
        }
        return result;
    }

    public PaginResult<DictPO> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        String dictCategory = StringKit.nvl(queryParameters.get("dictCategory"), "_ALL_");
        String originWhere = dataForm.getQuery().getWhere();
        if ("_ALL_".equalsIgnoreCase(dictCategory)) {
            dataForm.getQuery().setWhere("1=1");
        }
        PaginResult<DictPO> result = super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
        dataForm.getQuery().setWhere(originWhere);
        return result;
    }
}
