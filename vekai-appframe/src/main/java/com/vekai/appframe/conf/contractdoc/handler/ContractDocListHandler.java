package com.vekai.appframe.conf.contractdoc.handler;

import cn.fisok.sqloy.core.PaginResult;
import com.vekai.appframe.conf.doclist.entity.ConfDocListItemPO;
import com.vekai.appframe.conf.doclist.entity.ConfDocListPO;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.BizException;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ContractDocListHandler extends BeanDataListHandler<ConfDocListPO> {

    @Autowired
    BeanCruder accessor;

    public Integer delete(DataForm dataForm, List<ConfDocListPO> dataList) {
        Integer result = 0;
        for (ConfDocListPO docListPO : dataList) {
            String doclistCode = docListPO.getDoclistCode();
            List<ConfDocListItemPO> itemList = accessor.selectList(ConfDocListItemPO.class, "SELECT * FROM" +
                    " CONF_DOCLIST_ITEM WHERE DOCLIST_CODE=:doclistCode", MapKit.mapOf("doclistCode", doclistCode));

            if (itemList.size()==0 || null == itemList) {
                result = super.delete(dataForm,dataList);
                result += accessor.execute("DELETE FROM CONF_DOCLIST_GROUP WHERE " +
                        "DOCLIST_CODE=:doclistCode", MapKit.mapOf
                        ("doclistCode", doclistCode));
            } else {
                throw new BizException("合同文本有模板，不能删除！");
            }
        }
        return result;
    }

    public PaginResult<ConfDocListPO> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        String classification = StringKit.nvl(queryParameters.get("classification"), "_ALL_");
        if ("_ALL_".equalsIgnoreCase(classification)) {
            dataForm.getQuery().setWhere("DOCLIST_TYPE='TEMPLATE_DOC'");
        }

        return super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
    }
}
