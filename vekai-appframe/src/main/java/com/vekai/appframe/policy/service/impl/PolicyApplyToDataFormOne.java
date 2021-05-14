package com.vekai.appframe.policy.service.impl;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.appframe.constant.AppframeConst;
import com.vekai.appframe.policy.entity.PolcParamValueEntity;
import com.vekai.appframe.policy.model.PolicyDefinition;
import com.vekai.appframe.policy.model.PolicyParam;
import com.vekai.appframe.policy.model.PolicyParamValue;
import com.vekai.appframe.policy.service.PolicyApplyTo;
import com.vekai.appframe.policy.service.PolicyService;
import com.vekai.appframe.policy.types.DataValueMode;
import com.vekai.appframe.policy.types.EditorMode;
import com.vekai.appframe.policy.types.InOutType;
import com.vekai.base.dict.model.DictItemNode;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.model.types.ElementDataDictCodeMode;
import cn.fisok.raw.holder.ApplicationContextHolder;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.JSONKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 把产品应用到显示模板只有一条数据的情况
 */
public class PolicyApplyToDataFormOne implements PolicyApplyTo<DataForm,Object> {
    private Map<String,Object> properties = MapKit.newHashMap();

    private final static Double EPSILON = 0.0000001d;


    public void applyTo(PolicyDefinition policy, DataForm dataForm, Object object) {
        applyToDataForm(policy,dataForm);
        applyToObject(policy,object);
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    /**
     * 把产品设置应用到显示模板上
     * @param policy
     * @param dataForm
     */
    protected void applyToDataForm(PolicyDefinition policy, DataForm dataForm){
        List<DataFormElement> elements = dataForm.getElements();

        PolicyService policyService = ApplicationContextHolder.getBean(PolicyService.class);
        Map<String,List<DictItemNode>> policyDictMap = policyService.getPolicyDicts(policy.getPolicyId());
        this.filterDictItemNode(policyDictMap,policy);
        elements.forEach(element->{
            String paramCode = (String)properties.get(element.getCode());    //先到映射关系上去找字段和参数的映射关系
            if(StringKit.isBlank(paramCode))paramCode = element.getCode();  //否则直接使用字段作为参数代码
            PolicyParam param = policy.lookupPolicyParam(paramCode);
            if(param == null)return;

            //根据参数设置，修改显示模板
            String inOut = param.getInOut();
            if(InOutType.In.toString().equals(inOut)) return;//如果是输入参数，则不需要处理
            //处理显示层信息
            String applyEditMode = param.getApplyEditorMode();
            DataFormElement.FormElementUIHint uiHint = element.getElementUIHint();
            if("Label".equals(applyEditMode)){
                uiHint.setReading(true);
            }else if("Readonly".equals(applyEditMode)){
                uiHint.setReadonly(true);
            }else if("Editable".equals(applyEditMode)){
                uiHint.setReadonly(false);
            }else if("Hidden".equals(applyEditMode)){
                uiHint.setVisible(false);
            }
            //处理代码表信息,转成JSON来处理,把原来的下拉代码表处理为空
            List<DictItemNode> dictItemList = policyDictMap.get(paramCode);
            String jsonCode = JSONKit.toJsonString(dictItemList);
            uiHint.setDictCodeMode(ElementDataDictCodeMode.JSON)
                    .setDictCodeExpr(jsonCode);

            //处理默认值
            if(StringKit.isNotBlank(param.getDefaultValue())){
                element.setDefaultValue(param.getDefaultValue());
            }

        });
    }

    private void filterDictItemNode(Map<String, List<DictItemNode>> policyDictMap, PolicyDefinition policy) {
        List<PolicyParam> policyParams = policy.getPolicyParams();
        PolicyService policyService = ApplicationContextHolder.getBean(PolicyService.class);
        BeanCruder beanCruder = ApplicationContextHolder.getBean(BeanCruder.class);
        if (policyParams == null || policyParams.size() ==0) {
            PolicyDefinition policyDefinition = policyService.getPolicyDefinition(policy.getPolicyId());
            policyParams = policyDefinition.getPolicyParams();
        }
        for (PolicyParam param : policyParams) {
            if (StringKit.isBlank(param.getEditorMode())) continue;
            EditorMode editorMode = EditorMode.valueOf(param.getEditorMode());
            boolean isDictMode = false;
            switch (editorMode) {
                case Select:
                case RadioBox:
                case CheckBox:
                    isDictMode = true;
                    break;
                default:
                    isDictMode = false;
            }
            if (!isDictMode) continue;
            List<DictItemNode> dictItems = policyDictMap.get(param.getParamCode());
            String sql = "SELECT * FROM POLC_PARAM_VALUE WHERE PARAM_ID=:paramId";
            List<PolicyParamValue> paramValues = beanCruder.selectList(PolicyParamValue.class,sql,MapKit.mapOf("paramId",param.getParamId()));
            List<String> valueCodes = paramValues.stream().map(PolcParamValueEntity::getValueCode).collect(Collectors.toList());
            dictItems = dictItems.stream().filter(item -> valueCodes.contains(item.getCode())).collect(Collectors.toList());
            policyDictMap.put(param.getParamCode(),dictItems);
        }
    }

    /**
     * 把产品设置应用到业务参数上
     * @param policy
     * @param object
     */
    protected void applyToObject(PolicyDefinition policy,Object object){
        List<PolicyParam> policyParams = policy.getPolicyParams();
        policyParams.forEach(param -> {
            if (null != BeanKit.getPropertyValue(object,param.getParamCode()) && !this.isDefaultValue(param,object))
                return;
            if (AppframeConst.YES_NO_Y.equals(param.getIsExpression())) {
                BeanKit.setPropertyValue(object,param.getParamCode(),param.getExpressionValue());
            } else {
                String dataValueMode = param.getDataValueMode();
                if (DataValueMode.Single.toString().equals(dataValueMode)) {
                    BeanKit.setPropertyValue(object,param.getParamCode(),param.getValueExpr());
                } else {
                    BeanKit.setPropertyValue(object,param.getParamCode(),param.getDefaultValue());
                }
            }
        });
    }

    private boolean isDefaultValue(PolicyParam param, Object object) {
        PolicyService policyService = ApplicationContextHolder.getBean(PolicyService.class);
        Object bizObjectValue = policyService.transferDataType(param.getDataType(),
                BeanKit.getPropertyValue(object, StringKit.nvl(this.properties.get(param.getParamCode()),param.getParamCode())));
        if ("Double".equals(param.getDataType())) {
            if (Math.abs((Double) bizObjectValue - 0.0d) < EPSILON) {
                return Boolean.TRUE;
            }
        } else if ("Integer".equals(param.getDataType())) {
            if ((Integer)bizObjectValue == 0) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 取字段对应的参数设置
     * @param policyCode
     * @param fieldCode
     * @return
     */
    public PolicyParam getPolicyParam(String policyCode,String fieldCode){
        String paramCode = (String)properties.get(fieldCode);
        if(StringKit.isBlank(paramCode))paramCode = fieldCode;
        PolicyService policyService = ApplicationContextHolder.getBean(PolicyService.class);
        PolicyParam policyParam = policyService.getPolicyParamByCode(policyCode,fieldCode);
        return policyParam;
    }

}
