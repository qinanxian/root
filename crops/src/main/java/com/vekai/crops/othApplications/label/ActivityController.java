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
        if("3".equals(marketing.getActivityStatus())){
            return 1;
        }
        String[] strArr = targetCustomer.split(",");
        for (int i = 0; i < strArr.length; ++i){
            System.out.println("发送消息给标签为"+strArr[i]+"的客户");//这里输出a b c
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
