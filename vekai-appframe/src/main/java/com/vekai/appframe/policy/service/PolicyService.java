package com.vekai.appframe.policy.service;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.appframe.policy.model.PolicyDefinition;
import com.vekai.appframe.policy.model.PolicyParam;
import com.vekai.appframe.policy.model.PolicyParamValue;
import com.vekai.appframe.policy.types.EditorMode;
import com.vekai.base.dict.model.DictEntry;
import com.vekai.base.dict.model.DictItemEntry;
import com.vekai.base.dict.model.DictItemNode;
import com.vekai.base.dict.service.DictService;
import cn.fisok.raw.kit.ListKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.raw.lang.ValueObject;
import cn.fisok.raw.script.ScriptParser;
import cn.fisok.sqloy.core.MapObjectCruder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PolicyService {


    @Autowired
    BeanCruder beanCruder;
    @Autowired
    MapObjectCruder mapObjectCruder;
    @Autowired
    protected DictService dictService;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 取产品的头信息（主记录）
     * @param policyId
     * @return
     */
    public PolicyDefinition getPolicyPolicyDefinitionHeader(String policyId){
        String policyDefSql = "SELECT * FROM POLC_DEFINITION WHERE POLICY_ID =:policyId";
        PolicyDefinition productDefinition = beanCruder.selectOne(PolicyDefinition.class, policyDefSql, "policyId", policyId);
        return productDefinition;
    }
    /**
     * 根据Id查询政策对象以及相应参数
     *
     * @param policyId
     * @return
     */
    public PolicyDefinition getPolicyDefinition(String policyId) {
        PolicyDefinition productDefinition = getPolicyPolicyDefinitionHeader(policyId);
        ValidateKit.notNull(productDefinition, "政策编号[{0}]对应政策不存在", policyId);
        String policyParamSql = "SELECT * FROM POLC_PARAM WHERE POLICY_ID =:policyId";
        List<PolicyParam> params = beanCruder.selectList(PolicyParam.class, policyParamSql, "policyId", policyId);
        productDefinition.setPolicyParams(params);
        return productDefinition;
    }

    public PolicyDefinition getPolicyDefinitionByCode(String policyCode) {
        String policyDefSql = "SELECT * FROM POLC_DEFINITION WHERE POLICY_CODE =:policyCode";
        PolicyDefinition productDefinition = beanCruder.selectOne(PolicyDefinition.class, policyDefSql, "policyCode", policyCode);
        ValidateKit.notNull(productDefinition, "政策代码[{0}]对应政策不存在", policyCode);

        String policyParamSql = "SELECT * FROM POLC_PARAM WHERE POLICY_ID =:policyId";
        List<PolicyParam> params = beanCruder.selectList(PolicyParam.class, policyParamSql, "policyId", productDefinition.getPolicyId());
        productDefinition.setPolicyParams(params);
        return productDefinition;
    }

    public PolicyParam getPolicyParam (String policyId, String paramCode) {
        String sql = "SELECT * FROM POLC_PARAM WHERE POLICY_ID =:policyId AND PARAM_CODE =:paramCode";
        PolicyParam policyParam = beanCruder.selectOne(PolicyParam.class,sql,MapKit.mapOf("policyId",policyId,"paramCode",paramCode));
        ValidateKit.notNull(policyParam,"[policyId：{0},paramCode: {1}]，对应政策参数不存在",policyId,paramCode);
        return policyParam;
    }

    public PolicyParam getPolicyParamByCode (String policyCode, String paramCode) {
        String sql = "SELECT * FROM POLC_PARAM WHERE POLICY_CODE =:policyCode AND PARAM_CODE =:paramCode";
        PolicyParam policyParam = beanCruder.selectOne(PolicyParam.class,sql,MapKit.mapOf("policyCode",policyCode,"paramCode",paramCode));
        ValidateKit.notNull(policyParam,"[policyCode：{0},paramCode: {1}]，对应政策参数不存在",policyCode,paramCode);
        return policyParam;
    }

    public int delPolicyParamValue(String paramId) {
        String sql = "DELETE FROM POLC_PARAM_VALUE WHERE PARAM_ID=:paramId";
        return beanCruder.execute(sql,MapKit.mapOf("paramId",paramId));
    }

    public List<PolicyParamValue> getPolicyParamValues(String paramId) {
        String sql = "SELECT * FROM POLC_PARAM_VALUE WHERE PARAM_ID=:paramId";
        List<PolicyParamValue> paramValues = beanCruder.selectList(PolicyParamValue.class,sql,"paramId",paramId);
        return paramValues;
    }

    /**
     * 取回产品的所有字典项
     *
     * @param policyId
     * @return
     */
    public Map<String, List<DictItemNode>> getPolicyDicts(String policyId) {
        Map<String, List<DictItemNode>> dictMap = MapKit.newLinkedHashMap();

        PolicyDefinition policy = getPolicyDefinition(policyId);
        if (policy == null) return dictMap;
        List<PolicyParam> paramList = policy.getPolicyParams();
        if (paramList == null || paramList.isEmpty()) return dictMap;

        paramList.forEach(param -> {
            if (StringKit.isBlank(param.getEditorMode())) return;
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
            if (!isDictMode) return;

            List<DictItemNode> dictItems = parseDictItems(param);
            if (dictItems == null || dictItems.isEmpty()) return;
            dictMap.put(param.getParamCode(), dictItems);

        });


        return dictMap;
    }


    private List<DictItemNode> toDictItemNodes(List<DictItemEntry> items) {
        if (null == items || items.isEmpty()) return Collections.emptyList();
        List<DictItemNode> dictItems = new ArrayList<>(items.size());
        for (DictItemEntry item : items) {
            DictItemNode node = new DictItemNode();
            node.setValues(item);
            dictItems.add(node);
        }
        return dictItems;
    }

    protected List<DictItemNode> parseDictItems(PolicyParam param) {
        List<DictItemNode> paramList = ListKit.newArrayList();

        String editorSourceMode = param.getEditorSourceMode();
        String editorSourceData = param.getEditorSourceData();
        if (StringKit.isBlank(editorSourceMode) || StringKit.isBlank(editorSourceData)) return paramList;

        if ("Dict".equals(editorSourceMode)) {
            DictEntry dictEntry = dictService.getDict(editorSourceData);
            if (dictEntry == null) return paramList;
            List<DictItemEntry> items = dictEntry.getItems();
            paramList = toDictItemNodes(items);
        } else if ("SQL".equals(editorSourceMode)) {
            List<MapObject> dataList = null;
            try {
                dataList = mapObjectCruder.selectList(editorSourceData);
            } catch (Exception e) {
                logger.error("执行SQL出错", e);
                dataList = null;
            }
            if (dataList == null) return paramList;

            List<DictItemEntry> items = new ArrayList<DictItemEntry>();
            for (MapObject row : dataList) {
                DictItemEntry entry = new DictItemEntry();
                entry.setCode(row.getValue("code").strValue());
                entry.setName(row.getValue("name").strValue());
                entry.setSortCode(row.getValue("sortCode").strValue());
                entry.setHotspot(row.getValue("hotspot").intValue(0));
                items.add(entry);
            }
            paramList = toDictItemNodes(items);

        } else if ("CodeTable".equals(editorSourceMode)) {
            String[] pairArray = editorSourceData.replaceAll("\\s+", "").split(",");
            for (String pair : pairArray) {
                if (StringKit.isBlank(pair)) continue;
                String[] kv = pair.split(":");
                DictItemNode itemNode = new DictItemNode();
                itemNode.setCode(kv[0]);
                itemNode.setName(kv[1]);
                paramList.add(itemNode);
            }
        }

        return paramList;
    }

    /**
     * 根据前端传回的政策对象，对政策进行保存。
     * 政策对象保存时，可更新。参数保存时，需要做全量保存
     *
     * @param policyDefinition
     * @return
     */
    public int savePolicyDefinition(PolicyDefinition policyDefinition) {
        String policyId = policyDefinition.getPolicyId();
        if (StringKit.isBlank(policyId)) {
            policyId = policyDefinition.getPolicyCode();
            policyDefinition.setPolicyId(policyId);
        }
        int r = beanCruder.save(policyDefinition);
        r += savePolicyParamsOnly(policyDefinition);

        return r;
    }

    @Transactional
    public int savePolicyParamsOnly(PolicyDefinition policyDefinition) {
        String policyId = policyDefinition.getPolicyId();
        if (StringKit.isBlank(policyId)) {
            policyId = policyDefinition.getPolicyCode();
            policyDefinition.setPolicyId(policyId);
        }

        List<PolicyParam> policyParams = policyDefinition.getPolicyParams();
        if (!StringKit.isBlank(policyId)) {
            String deleteParamSql = "DELETE FROM POLC_PARAM WHERE POLICY_ID =:policyId";
            beanCruder.execute(deleteParamSql, MapKit.mapOf("policyId", policyId));
        }

        for (PolicyParam policyParam : policyParams) {
            policyParam.setPolicyId(policyId);
            policyParam.setParamId(null);
        }

        int result = beanCruder.save(policyParams);
        String delParamValueSql = "DELETE FROM POLC_PARAM_VALUE WHERE PARAM_ID = :paramId";
        policyParams.stream().filter(param -> "Multi".equals(param.getDataValueMode())).forEach(param -> {
            beanCruder.execute(delParamValueSql,MapKit.mapOf("paramId",param.getParamId()));
            if (StringKit.isBlank(param.getValueExpr())) return ;
            List<String> valueExprList = Arrays.asList(param.getValueExpr().split(","));
            List<PolicyParamValue> paramValues = valueExprList.stream()
                    .map(item -> this.initParamValue(item,param.getParamId()))
                    .collect(Collectors.toList());
            beanCruder.save(paramValues);
        });
        return result;
    }

    /**
     * 根据前端传回的政策对象，只是针对参数值做保存
     *
     * @param policy
     * @return
     */
    @Transactional
    public int savePolicyValues(PolicyDefinition policy) {
        int r = 0;
        String policyId = policy.getPolicyId();
        if (StringKit.isBlank(policyId)) {
            policyId = policy.getPolicyCode();
            policy.setPolicyId(policyId);
        }

        List<PolicyParam> policyParams = policy.getPolicyParams();
        List<PolicyParam> prePolicyParams = policyParams.stream()
                .map(this::fillPreParam).collect(Collectors.toList());
        r = beanCruder.save(prePolicyParams);
        return r;
    }

    private PolicyParam fillPreParam(PolicyParam param) {
        PolicyParam origPolicyParam = this.getPolicyParam(param.getPolicyId(),param.getParamCode());

        origPolicyParam.setApplyEditorMode(param.getApplyEditorMode()); //编辑形式设置
        origPolicyParam.setDefaultValue(param.getDefaultValue()); //默认值设置

        String dataValueMode = param.getDataValueMode();
        //当为多值的时候，数据存放在另一个表中的，因此需要特殊处理
        if("Multi".equals(dataValueMode)){
            this.delPolicyParamValue(param.getParamId());
            origPolicyParam.setValueExpr(param.getValueExpr());
            if (StringKit.isBlank(param.getValueExpr())) return origPolicyParam;
            List<String> valueExprList = Arrays.asList(param.getValueExpr().split(","));
            List<PolicyParamValue> paramValues = valueExprList.stream()
                    .map(item -> this.initParamValue(item,param.getParamId()))
                    .collect(Collectors.toList());
            beanCruder.save(paramValues);
        }else if("Range".equals(dataValueMode)){
            origPolicyParam.setValueMinExpr(param.getValueMinExpr());
            origPolicyParam.setValueMaxExpr(param.getValueMaxExpr());
        } else {
            origPolicyParam.setValueExpr(param.getValueExpr());
        }
        return origPolicyParam;
    }

    private PolicyParamValue initParamValue(String item, String paramId) {
        PolicyParamValue paramValue = new PolicyParamValue();
        paramValue.setParamId(paramId);
        paramValue.setValueCode(item);
        //TODO name的处理
        return paramValue;
    }

    /**
     * 根据产品以及参数，解析运行产品对象，把相应的参数对象计算出来
     *
     * @param prodId
     * @param params
     * @return
     */
    public PolicyDefinition parse(String prodId, Map<String, Object> params) {
        PolicyDefinition policy = getPolicyDefinition(prodId);
        return parse(policy, params);
    }

    /**
     * 根据产品以及参数，解析运行产品对象，把相应的参数对象计算出来
     *
     * @param policy
     * @param params
     * @return
     */
    public PolicyDefinition parse(PolicyDefinition policy, Map<String, Object> params) {
        Map<String, Object> runParams = MapKit.newEmptyMap();
        runParams.putAll(params);

        List<PolicyParam> paramList = policy.getPolicyParams();
        ScriptParser<Object> scriptParser = new ScriptParser<Object>();

        paramList.forEach(param -> {
            String code = param.getParamCode();
            String name = param.getParamName();

            //如果是输入参数，处理下
            String inOut = param.getInOut();
            if (!"Y".equals(param.getIsExpression())) {
                //Object value = param.getDefaultValue(); //模拟值
                Object value = param.getValueExpr(); //模拟值
                if (runParams.containsKey(code)) {        //传入有参数有，则以传入参数优先
                    value = runParams.get(code);
                }
                value = transferDataType(param.getDataType(),value);
                if( value == null) value = param.getDefaultValue();
                param.setExpressionValue(value);
                //名称以及代码都要建立为变量
                runParams.put(code, value);
                runParams.put(name, value);
            } else if ("Out".equals(inOut) && "Y".equals(param.getIsExpression())) {   //如果是输出值，并且公式，则解析他
                String dataValueMode = param.getDataValueMode();
                //当为多值的时候，数据存放在另一个表中的，因此需要特殊处理
                if ("Single".equals(dataValueMode)) {
                    String expr = param.getValueExpr();
                    ValidateKit.notNull(expr,"[{0}]计算公式不能为空",param.getParamName());
                    Object ret = parse(scriptParser,expr,runParams);

                    param.setExpressionValue(ret);
                    runParams.put(code,ret);        //解析完之后，把解析后得到的值，放到变量中去，给后面的用
                    runParams.put(name,ret);
                } else if ("Range".equals(dataValueMode)) {
                    String expr1 = param.getValueMinExpr();
                    String expr2 = param.getValueMaxExpr();
                    Object ret1 = parse(scriptParser,expr1,runParams);
                    Object ret2 = parse(scriptParser,expr2,runParams);
                    Object[] ret = new Object[]{ret1,ret2};

                    param.setExpressionValue(ret);
                    runParams.put(code,ret);
                    runParams.put(name,ret);
                }
            }

        });

        return policy;
    }

    public Object transferDataType(String dataType, Object value) {
        switch (dataType) {
            case "Double":
                value = ValueObject.valueOf(value).doubleValue();
                break;
            case "Integer":
                value = ValueObject.valueOf(value).intValue();
                break;
            case "Date":
                value = ValueObject.valueOf(value).dateValue();
                break;
            default:
                value = ValueObject.valueOf(value).strValue();
                break;
        }
        return value;
    }

    public Object parse(ScriptParser<Object> scriptParser,String expr,Map<String,Object> param){
        Object object = null;
        try {
            object = scriptParser.parse(expr,param);
        } catch (ScriptException e) {
            logger.warn("解析脚本出错,["+expr+"]",e);
            object = e.getMessage();
        }
        return object;
    }

    /**
     * 根据产品政策，执行克隆
     *
     * @param policyId   原对象主键
     * @param policyCode 新的policyCode
     * @return
     */
    public Integer copyPolicy(String policyId, String policyCode) {
        String sql = "SELECT * FROM POLC_DEFINITION WHERE policy_Code =:policyCode";
        List<PolicyDefinition> policyDefinitions = beanCruder.selectList(PolicyDefinition.class, sql,
                "policyCode", policyCode);
        ValidateKit.isTrue(policyDefinitions.size() == 0, "[{0}系统已存在，请重新命名]", policyCode);
        PolicyDefinition policyDefinition = this.getPolicyDefinition(policyId);
        policyDefinition.setPolicyCode(policyCode);
        policyDefinition.setPolicyId(null);
        return this.savePolicyDefinition(policyDefinition);
    }
}
