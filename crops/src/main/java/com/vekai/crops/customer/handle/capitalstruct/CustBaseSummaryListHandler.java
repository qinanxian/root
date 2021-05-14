package com.vekai.crops.customer.handle.capitalstruct;

import cn.fisok.sqloy.core.PaginResult;
import com.vekai.crops.constant.BizConst;
import com.vekai.crops.customer.constant.CustomerConst;
import com.vekai.crops.customer.entity.CustomerPO;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.ValueObject;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by luyu on 2018/5/2.
 */
@Component
public class CustBaseSummaryListHandler extends BeanDataListHandler<CustomerPO> {

    @Override
    public PaginResult<CustomerPO> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        String custType = ValueObject.valueOf(queryParameters.get("custType")).strValue();
        if (!StringKit.isBlank(custType)) {
            String type = CustomerConst.CUSTOMER_STOCKHOLDER_TYPE_ENT.equals(custType) ?
                    BizConst.CUSTOMER_TYPE_ENT : BizConst.CUSTOMER_TYPE_IND;
            dataForm.getQuery().setWhere(dataForm.getQuery().getWhere() + " AND BASE.CUST_TYPE='" + type + "'");
        }
        return super.query(dataForm,queryParameters,filterParameters,sortMap,pageSize,pageIndex);
    }
}
