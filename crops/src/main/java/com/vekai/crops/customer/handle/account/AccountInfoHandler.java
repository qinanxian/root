package com.vekai.crops.customer.handle.account;

import com.vekai.crops.constant.BizConst;
import com.vekai.crops.customer.entity.CustAccountPO;
import com.vekai.dataform.handler.impl.BeanDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.StringKit;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luyu on 2018/1/24.
 */
@Component
public class AccountInfoHandler extends BeanDataOneHandler<CustAccountPO> {


    @Override
    public int save(DataForm dataForm, CustAccountPO accountPO) {
        String isDefault = accountPO.getIsDefault();
        if (BizConst.YES_NO_Y.equals(isDefault)) {
            String custId = accountPO.getCustId();
            String id = accountPO.getId();
            String sql = null;
            List<CustAccountPO> accountPOList = null;
            Map<String,String> paramMap = new HashMap();
            paramMap.put("custId",custId);
            paramMap.put("id",id);
            if (id == null) {
                sql = "select * from CUST_ACCOUNT where CUST_ID = :custId and IS_DEFAULT = 'Y'";
                accountPOList = beanCruder.selectList(CustAccountPO.class,sql,paramMap);
            } else {
                sql = "select * from CUST_ACCOUNT where CUST_ID = :custId and IS_DEFAULT = 'Y' "
                    + "and ID != :id";
                accountPOList = beanCruder.selectList(CustAccountPO.class,sql,paramMap);
            }
            accountPOList.forEach(item -> {
                String accountType = accountPO.getAccountType();
                if(StringKit.equals(item.getAccountType(),accountType)){
                    Map<String,String> updateParamMap = new HashMap();
                    updateParamMap.put("id",item.getId());
                    String updateSql = "update CUST_ACCOUNT set IS_DEFAULT = 'N' where ID = :id";
                    beanCruder.execute(updateSql,updateParamMap);
                }
            });
        }
        return beanCruder.save(accountPO);
    }
}
