package com.vekai.appframe.policy.controller;

import com.vekai.appframe.policy.service.PolicyService;
import com.vekai.appframe.policy.model.PolicyDefinition;
import com.vekai.appframe.policy.model.PolicyParam;
import com.vekai.base.dict.model.DictItemNode;
import cn.fisok.raw.kit.MapKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/policy")
public class PolicyController {

    @Autowired
    PolicyService policyService;

//    @GetMapping("/delete/{policyId}")
//    public int deletePolicy(@PathVariable("policyId")String policyId) {
//        return 0;
//    }
    @GetMapping("/get/{policyId}")
    public PolicyDefinition getPolicyDefinition(@PathVariable("policyId")String policyId) {
        return policyService.getPolicyDefinition(policyId);
    }

    @GetMapping("/get/{policyId}/dicts")
    public Map<String,List<DictItemNode>> getPolicyDicts(@PathVariable("policyId")String policyId) {
        return policyService.getPolicyDicts(policyId);
    }

    @PostMapping("/save")
    public Integer savePolicyParams(@RequestBody PolicyDefinition policyDefinition) {
        return policyService.savePolicyParamsOnly(policyDefinition);
    }

    @PostMapping("/saveValues")
    public Integer savePolicyValues(@RequestBody PolicyDefinition policyDefinition) {
        return policyService.savePolicyValues(policyDefinition);
    }

    @PostMapping("/mockTest")
    public List<PolicyParam> mockTest(@RequestBody PolicyDefinition policyDefinition) {
        PolicyDefinition policy = policyService.parse(policyDefinition, MapKit.newEmptyMap());
        List<PolicyParam> paramList = policy.getPolicyParams();
        return paramList
                .stream()
                .filter(param->"Y".equals(param.getIsExpression()))
                .collect(Collectors.toList());
    }

    @PostMapping("/copy/{policyId}/{newPolicyCode}")
    public Integer copyPolicy(@PathVariable("policyId")String policyId,@PathVariable("newPolicyCode")String policyCode) {
        return policyService.copyPolicy(policyId,policyCode);
    }


}
