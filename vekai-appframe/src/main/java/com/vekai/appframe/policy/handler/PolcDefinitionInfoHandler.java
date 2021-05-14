package com.vekai.appframe.policy.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.appframe.policy.entity.PolcDefinitionEntity;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.ValueObject;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by luyu on 2018/9/29.
 */
@Component
public class PolcDefinitionInfoHandler extends MapDataOneHandler {

    private final static Integer DIGIT_CAPACITY = 4;
    @Autowired
    private BeanCruder beanCruder;

    @Override
    @Transactional
    public int save(DataForm dataForm, MapObject mapObject) {
        String parentPolicyCode = mapObject.getValue("parentPolicyCode").strValue();
        Integer count = 0;
        String sortCode = null;
        if (StringKit.isBlank(parentPolicyCode)) {
            String sql = "SELECT * FROM POLC_DEFINITION WHERE SORT_CODE not like '%-%' and SORT_CODE IS NOT NULL";
            count = beanCruder.selectCount(sql, MapKit.newHashMap());
            sortCode = this.generateSortCode("",count + 1);
        } else {
            String policySql = "SELECT * FROM POLC_DEFINITION WHERE POLICY_CODE = :policyCode";
            PolcDefinitionEntity polcDefinition = beanCruder.selectOne(
                    PolcDefinitionEntity.class,policySql,"policyCode",parentPolicyCode);
            String parentSortCode = polcDefinition.getSortCode();
            String existCountSql = "SELECT * FROM POLC_DEFINITION WHERE SORT_CODE LIKE '" + parentSortCode + "-%'";
            count = beanCruder.selectCount(existCountSql, MapKit.newHashMap());
            sortCode = this.generateSortCode(parentSortCode + "-",count + 1);
        }
        mapObject.put("sortCode",sortCode);
        return super.save(dataForm, mapObject);
    }

    @Override
    public MapObject query(DataForm dataForm, Map<String, ?> queryParameters) {
        String policyId = ValueObject.valueOf(queryParameters.get("policyId")).strValue();
        if (!StringKit.isBlank(policyId)) {
            dataForm.getElements().stream().forEach(dataFormElement -> {
                if ("policyCode".equals(dataFormElement.getCode())) {
                    DataFormElement.FormElementUIHint formElementUIHint = dataFormElement.getElementUIHint();
                    formElementUIHint.setReadonly(Boolean.TRUE);
                    dataFormElement.setElementUIHint(formElementUIHint);
                }
            });
        }
        return super.query(dataForm,queryParameters);

    }

    private String generateSortCode(String prefix, Integer count) {
        String sortCode = prefix + StringKit.leftPad(count.toString(),DIGIT_CAPACITY,"0");
        return sortCode;
    }

}
