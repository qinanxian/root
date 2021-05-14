package com.vekai.crops.customer.handle.enterprise;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.crops.constant.BizConst;
import com.vekai.crops.customer.entity.CustEnterprisePO;
import com.vekai.crops.customer.entity.CustPermitPO;
import com.vekai.crops.customer.entity.CustomerPO;
import com.vekai.crops.customer.service.CustomerService;
import com.vekai.auth.holder.AuthHolder;
import com.vekai.dataform.handler.impl.BeanDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.holder.MessageHolder;
import cn.fisok.raw.lang.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by luyu on 2018/1/19.
 */
@Component
public class EnterpriseSummaryHandler extends BeanDataOneHandler<CustomerPO> {

    @Autowired
    BeanCruder accessor;
    @Autowired
    CustomerService customerService;

    @Override
    @Transactional
    public int save(DataForm dataForm, CustomerPO customer) {
        String custName = customer.getCustName();
        String cretId = customer.getCertId();
        String certType = customer.getCertType();
        validateCustomerHasExisted(custName,cretId,certType);

        customer.setCertType(BizConst.UNIFORM_CREDIT_CODE);// 统一社会信用代码
        customer.setCustType(BizConst.CUSTOMER_TYPE_ENT);
        customer.setCustName(custName);
        customer.setCertId(cretId);

        Integer result = super.save(dataForm, customer);
        if (result > 0) {
            createEntObject(customer);
            createPermit(customer.getCustId());
            return result;
        }

//        docListService.initDocList(DocListCode.LOGI,customer.getCustId(),
//                ObjectType.LOGI.toString());

        return result;
    }

    private void createPermit(String custId) {
        CustPermitPO custPermitPO = new CustPermitPO();
        custPermitPO.setAllowView(BizConst.YES_NO_Y);
        custPermitPO.setAllowHold(BizConst.YES_NO_Y);
        custPermitPO.setAllowEdit(BizConst.YES_NO_Y);
        custPermitPO.setAllowBusiness(BizConst.YES_NO_Y);
        custPermitPO.setUserId(AuthHolder.getUser().getId());
        custPermitPO.setCustId(custId);
        beanCruder.save(custPermitPO);
    }

    private CustEnterprisePO createEntObject(CustomerPO customer) {
        CustEnterprisePO enterprise = new CustEnterprisePO();
        if (BizConst.UNIFORM_CREDIT_CODE.equals(customer.getCertType())) {
            enterprise.setUnifiedCreditCode(customer.getCertId());
        } else if (BizConst.ORGANIZATION_CODE_CERTIFICATE.equals(customer.getCertType())) {
            enterprise.setLicenseCode(customer.getCertId());
        }
        enterprise.setCustId(customer.getCustId());
        enterprise.setCustName(customer.getCustName());
        accessor.save(enterprise);
        return enterprise;
    }

    private void validateCustomerHasExisted(String custName,String certId,String certType) {
        CustEnterprisePO enterprisePO = customerService.getEnterpriseByCustName(custName);
        if(null != enterprisePO)
            throw new BizException(MessageHolder.getMessage("", "customer.enterprise.already.exist"));

        CustomerPO customerPO = customerService.getCustByCertIdAndcertType(certId,certType);
        if(null != customerPO)
            throw new BizException(MessageHolder.getMessage("", "customer.certificate.already.exist"));
    }
}
