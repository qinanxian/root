package com.vekai.appframe.policy.handler;

import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.core.PaginResult;
import com.vekai.appframe.policy.entity.PolcDefinitionEntity;
import com.vekai.appframe.policy.service.PolicyService;
import com.vekai.appframe.policy.model.PolicyDefinition;
import com.vekai.appframe.policy.model.PolicyParam;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PolicyDefinitionListHandler extends BeanDataListHandler<PolcDefinitionEntity> {

    @Autowired
    BeanCruder beanCruder;
    @Autowired
    PolicyService policyService;

    @Transactional
    public Integer delete(DataForm dataForm, List<PolcDefinitionEntity> dataList) {
        for (PolcDefinitionEntity polcDefinitionEntity : dataList) {
            deleteWithChildPolicy(polcDefinitionEntity);
        }
        return 1;
    }

    public void deleteWithChildPolicy(PolcDefinitionEntity polcDefinitionEntity) {
        String policyCode = polcDefinitionEntity.getPolicyCode();
        deleteCompletePolicy(polcDefinitionEntity);
        String childSql = "SELECT * FROM POLC_DEFINITION WHERE PARENT_POLICY_CODE = :policyCode";
        List<PolcDefinitionEntity> dataList = beanCruder.selectList(PolcDefinitionEntity.class,
                childSql, MapKit.mapOf("policyCode",policyCode));
        if (dataList.size() > 0) {
            dataList.forEach(item -> deleteWithChildPolicy(item));
        }
    }

    public void deleteCompletePolicy(PolcDefinitionEntity polcDefinitionEntity) {
        String policyId = polcDefinitionEntity.getPolicyId();
        PolicyDefinition policy = policyService.getPolicyDefinition(policyId);
        List<PolicyParam> policyParams = policy.getPolicyParams();
        beanCruder.delete(polcDefinitionEntity);
        if (policyParams == null || policyParams.size() == 0)
            return;
        String paramIds = policyParams.stream()
                .map(PolicyParam::getParamId)
                .map(paramId -> "\'" + paramId + "\'")
                .collect(Collectors.joining(","));
        String deleteParamvalueSql = "DELETE FROM POLC_PARAM_VALUE WHERE PARAM_ID IN (" + paramIds + ")";
        beanCruder.execute(deleteParamvalueSql);
        beanCruder.delete(policyParams);

    }

    public PaginResult<PolcDefinitionEntity> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        PaginResult<PolcDefinitionEntity> ret = super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
        List<PolcDefinitionEntity> dataList = ret.getDataList();
        return ret;
    }
}
