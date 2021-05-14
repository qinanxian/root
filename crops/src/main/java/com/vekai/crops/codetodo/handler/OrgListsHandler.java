package com.vekai.crops.codetodo.handler;
import cn.fisok.sqloy.core.PaginResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.vekai.crops.util.JsonUtil;
import com.vekai.auth.entity.Org;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.StringKit;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class OrgListsHandler extends BeanDataListHandler<Org> {

    public PaginResult<Org> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        String orgId = StringKit.nvl(queryParameters.get("orgId"), "_ALL_");
        if ("_ALL_".equals(orgId)) {
            dataForm.getQuery().setWhere("1=1");
        }
        PaginResult<Org> result = super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
        List<Org> dataList = result.getDataList();
        dataList.stream().forEach(org -> {
            String district =org.getDistricts();
            try {
                JsonNode districtJson = JsonUtil.getJson(district);
                String address = JsonUtil.getJsonStringValue(districtJson, "address");
                org.setDistricts(address);
            }catch (Exception e){
                e.printStackTrace();
                logger.error("数据转换的handler出错，错误信息："+e.getMessage());
            }

        });

        return result;    }
}
