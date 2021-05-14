package com.vekai.appframe.policy;

import com.google.common.collect.Lists;
import com.vekai.appframe.policy.model.PolicyDefinition;
import com.vekai.appframe.policy.model.PolicyParam;
import cn.fisok.raw.kit.JSONKit;
import org.junit.Test;

import java.util.List;

public class PolcDefinitionTest {

    @Test
    public void demoData(){
        PolicyDefinition policy = new PolicyDefinition();

        policy.setPolicyName("汽车贷款一");
        policy.setPolicyCode("CD1");
        policy.setPolicyParams(getParams());

        System.out.println(JSONKit.toJsonString(policy,true));

    }

    public List<PolicyParam> getParams(){
        PolicyParam[] params = new PolicyParam[]{
            new PolicyParam("businessMode","适用业务模式","10:适用于"),
            new PolicyParam("suitForArea","适用区域","10:适用于"),
            new PolicyParam("suitForFirm","适用厂商","20:车辆"),
            new PolicyParam("suitForBrand","适用品牌","20:车辆"),
            new PolicyParam("gpsFirm","GPS厂商","20:车辆"),
            new PolicyParam("gpsFirmInstalCount","GPS数量","20:车辆"),
            new PolicyParam("LoanAmtLimit","融资上限","30:报价核算"),
            new PolicyParam("LoanCreditLimit","融资额度","30:报价核算"),
            new PolicyParam("LoanTermLimit","融资期限","30:报价核算"),
            new PolicyParam("LoanInterestRate","融资利率","30:报价核算"),
            new PolicyParam("PaymentCycle","付款频率","30:报价核算"),
            new PolicyParam("FirstPaymentRatio","首付款比例","30:报价核算"),
            new PolicyParam("depositRatio","保证金比例","30:报价核算"),
            new PolicyParam("residualAmt","残值","30:报价核算"),
            new PolicyParam("gpsFeeAmt","GPS费用","30:报价核算"),
            new PolicyParam("stayPurchasePrice","留购价","30:报价核算"),
            new PolicyParam("perTermPayAmt","租金","30:报价核算"),
            new PolicyParam("isStayPurchase","是否留购","40：结清规则"),
            new PolicyParam("PreSettleRule","提前结清计算规则","40：结清规则"),
            new PolicyParam("settleRule","正常结清计算规则","40：结清规则"),
            new PolicyParam("contractTemplate","合同模板","50：业务参数"),
        };
        return Lists.newArrayList(params);
    }
}
