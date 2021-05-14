package com.vekai.appframe.auth.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.PaginResult;
import com.vekai.appframe.auth.dto.UserOrgDTO;
import com.vekai.auth.service.UserService;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.StringKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserListHandler extends BeanDataListHandler<UserOrgDTO> {

    @Autowired
    private UserService userService;

    public PaginResult<UserOrgDTO> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        String orgId = StringKit.nvl(queryParameters.get("orgId"), "_ALL_");
        if ("_ALL_".equals(orgId)) {
            dataForm.getQuery().setWhere("1=1");
        }

        return super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
    }

    public int enableUser(DataForm dataForm, MapObject param) {
        String userId = param.getValue("userId").strValue("");
        return userService.enableUser(userId);
    }

    public int disableUser(DataForm dataForm, MapObject param) {
        String userId = param.getValue("userId").strValue("");
        return userService.disableUser(userId);
    }

    public int initUserPassword(DataForm dataForm, MapObject param) {
        String userId = param.getValue("userId").strValue("");
        return userService.initUserPassword(userId);
    }
}
