package com.vekai.crops.othApplications.label;

import cn.fisok.sqloy.core.BeanCruder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.vekai.crops.customer.entity.CustomerInfoPO;
import com.vekai.crops.util.JsonUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("Label")
public class LabelController {
    @Autowired
    private BeanCruder beanCruder;

    @PostMapping("insterLabel")
    @ResponseBody
    public int LabelInster(@RequestBody JsonNode jsonNode) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayNode orgIdArray = JsonUtil.getArrayNodeByPath(jsonNode, "orgIdArray");
        for (int i = 0; i < orgIdArray.size(); i++) {
            String s = orgIdArray.get(i).textValue();
            stringBuilder.append("/"+s);
        }
        String id = JsonUtil.getJsonStringValue(jsonNode, "id");
        String sql="select * from CUSTOMER_INFO where id =:id ";
        CustomerInfoPO customerInfoPO1 = beanCruder.selectOne(CustomerInfoPO.class, sql, "id", id);
        customerInfoPO1.setCustomerLabel(stringBuilder.toString());
        int update = beanCruder.update(customerInfoPO1);
        return 0;
    }


}
