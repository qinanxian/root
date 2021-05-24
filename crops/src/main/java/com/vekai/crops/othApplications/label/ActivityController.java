package com.vekai.crops.othApplications.label;

import cn.fisok.sqloy.core.BeanCruder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.vekai.crops.customer.entity.CustomerInfoPO;
import com.vekai.crops.othApplications.label.entity.Marketing;
import com.vekai.crops.othApplications.zryJurisDiction.entity.ZryFinancingOrderInfo;
import com.vekai.crops.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/Activity")
public class ActivityController {

    @Autowired
    private BeanCruder beanCruder;

    @PostMapping("/activityStart")
    @ResponseBody
    public int ActivityStart(@RequestBody JsonNode jsonNode) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        String targetCustomer = JsonUtil.getJsonStringValue(jsonNode, "targetCustomer");
        String sql="select * from MARKETING where id =:id ";
        String id = JsonUtil.getJsonStringValue(jsonNode, "id");
        Marketing marketing = beanCruder.selectOne(Marketing.class, sql, "id", id);

        String sqlCust="select * from CUSTOMER_INFO";
        List<CustomerInfoPO> customerInfoPOS = beanCruder.selectList(CustomerInfoPO.class, sqlCust);
        if("3".equals(marketing.getActivityStatus())){
            return 1;
        }else if("0".equals(marketing.getActivityStatus())){
            marketing.setActivityStatus("1");
            int update = beanCruder.update(marketing);
        }else if("1".equals(marketing.getActivityStatus())){
            return 2;
        }
        for (CustomerInfoPO customerInfoPO : customerInfoPOS) {
            String customerLabel = customerInfoPO.getCustomerLabel();
            if(customerLabel!=null&&!"".equals(customerLabel)){
                String[] split = customerLabel.split(",");
                for (int i = 0; i < split.length; i++) {
                    String s = split[i];
                    String[] strArr = targetCustomer.split(",");
                    for (int j = 0; j < strArr.length; ++j){
                        if(split[i].equals(strArr[j])){
                            System.out.println("发送消息给标签为"+strArr[j]+"的客户"+"客户的姓名为"+customerInfoPO.getCertName()+",手机号为"+customerInfoPO.getTel());//这里输出a b c
                        }
                    }
                }
            }
        }
        return 0;
    }


    @PostMapping("/activityEnd")
    @ResponseBody
    public void activityEnd(@RequestBody JsonNode jsonNode) throws Exception {
        String id = JsonUtil.getJsonStringValue(jsonNode, "id");
        String sql="select * from MARKETING where id =:id ";
        Marketing marketing = beanCruder.selectOne(Marketing.class, sql, "id", id);
        marketing.setActivityStatus("3");
        int update = beanCruder.update(marketing);
    }
}
