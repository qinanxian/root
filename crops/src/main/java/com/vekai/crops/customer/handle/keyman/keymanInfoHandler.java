package com.vekai.crops.customer.handle.keyman;

import com.vekai.crops.customer.constant.CustomerConst;
import com.vekai.crops.customer.entity.CustRelationPO;
import com.vekai.dataform.handler.impl.BeanDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.holder.MessageHolder;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.BizException;
import org.springframework.stereotype.Component;

@Component
public class keymanInfoHandler extends BeanDataOneHandler<CustRelationPO> {

	@Override
    public int save(DataForm dataForm, CustRelationPO relationPO) {
		String relationPOId = relationPO.getId();
		String relationship = relationPO.getRelationship();
		validateExistLegalRepresentative(relationPOId,relationship,relationPO.getCustId());
        return beanCruder.save(relationPO);
    }

	private void validateExistLegalRepresentative(String relationPOId, String relationship, String custId) {
		if (!CustomerConst.CUSTOMER_RELATIONSHIP_LEGAL_REPRESENTATIVE.equals(relationship))
			return ;
		String sql = null;
		int count = 0;
		if (StringKit.isBlank(relationPOId)) {
			sql = "SELECT * FROM CUST_RELATION where CUST_ID =:custId and RELATIONSHIP='100' and status='VALID'";
			count = beanCruder.selectCount(sql,MapKit.mapOf("custId",custId));
		} else {
			sql = "SELECT * FROM CUST_RELATION where CUST_ID =:custId and RELATIONSHIP='100' and status='VALID' and id <> :relationPOId";
			count = beanCruder.selectCount(sql,MapKit.mapOf("custId",custId,"relationPOId",relationPOId));
		}
		if (count > 0)
			throw new BizException(MessageHolder.getMessage("","customer.relation.info.has.exist.legal.representative"));
	}
}
