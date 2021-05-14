package com.vekai.appframe.conf.contractdoc.handler;

import com.vekai.appframe.conf.doclist.entity.ConfDocListGroupPO;
import com.vekai.appframe.conf.doclist.entity.ConfDocListItemPO;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.lang.BizException;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContractDocListGroupHandler extends BeanDataListHandler<ConfDocListGroupPO> {

    @Autowired
    BeanCruder accessor;

    public Integer delete (DataForm dataForm, List<ConfDocListGroupPO> dataList){
        Integer result = 0;
        for (ConfDocListGroupPO group : dataList) {
            String doclistCode = group.getDoclistCode();
            String groupCode = group.getGroupCode();
            List<ConfDocListItemPO> itemList = accessor.selectList(ConfDocListItemPO.class, "SELECT * FROM" +
                    " CONF_DOCLIST_ITEM WHERE DOCLIST_CODE=:doclistCode and GROUP_CODE=:groupCode", MapKit.mapOf
                    ("doclistCode", doclistCode,"groupCode",groupCode));

            if (itemList.size()==0 || null == itemList) {
                result = super.delete(dataForm,dataList);
            } else {
                throw new BizException("合同文本分组已被引用，不能删除！");
            }
        }
        return result;
    }
}
