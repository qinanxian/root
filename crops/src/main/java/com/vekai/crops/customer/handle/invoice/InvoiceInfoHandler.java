package com.vekai.crops.customer.handle.invoice;


import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.crops.constant.BizConst;
import com.vekai.crops.customer.entity.CustInvoicePO;
import com.vekai.dataform.handler.impl.BeanDataOneHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by luyu on 2018/1/24.
 */
@Component
public class InvoiceInfoHandler extends BeanDataOneHandler<CustInvoicePO> {

    @Autowired
    private BeanCruder beanCruder;

    @Override
    public int save(DataForm dataForm, CustInvoicePO invoicePO) {
        String isDefault = invoicePO.getIsDefault();
        if ("Y".equals(isDefault)) {
            String custId = invoicePO.getCustId();
            String id = invoicePO.getId();
            String sql = null;
            List<CustInvoicePO> invoicePOList = null;
            Map<String,String> paramMap = new HashMap();
            paramMap.put("custId",custId);
            paramMap.put("id",id);
            if (id == null) {
                sql = "select * from CUST_INVOICE where CUST_ID = :custId and IS_DEFAULT = 'Y'";
                invoicePOList = beanCruder.selectList(CustInvoicePO.class,sql,paramMap);
            } else {
                sql = "select * from CUST_INVOICE where CUST_ID = :custId and IS_DEFAULT = 'Y' "
                    + "and ID != :id";
                invoicePOList = beanCruder.selectList(CustInvoicePO.class,sql,paramMap);
            }
            invoicePOList.forEach(item -> {
                item.setIsDefault(BizConst.YES_NO_N);
                beanCruder.save(item);
            });
        }
        return beanCruder.save(invoicePO);
    }
}
