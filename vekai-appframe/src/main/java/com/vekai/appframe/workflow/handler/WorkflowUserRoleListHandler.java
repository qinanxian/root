package com.vekai.appframe.workflow.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.core.PaginResult;
import com.vekai.auth.entity.Role;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class WorkflowUserRoleListHandler extends MapDataListHandler {
    @Autowired
    private BeanCruder beanCruder;

    @Override
    public PaginResult<MapObject> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        PaginResult<MapObject> ret = super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
        List<MapObject> dataList = ret.getDataList();
        dataList.forEach(this::accept);
        return ret;
    }

    private void accept(MapObject mapObject) {
        String id = (String) mapObject.get("id");
        List<Role> roleList = beanCruder.selectList(Role.class, "SELECT R.NAME FROM AUTH_USER_ROLE UR,AUTH_ROLE R, AUTH_USER USR    WHERE USR.ID = UR.USER_ID  AND UR.ROLE_ID = R.ID AND USR.ID =:ID", MapKit.mapOf("ID", id));
        if (null != roleList && !roleList.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < roleList.size(); i++) {
                if (i < roleList.size() - 1) {
                    builder.append(roleList.get(i).getName());
                    builder.append(",");
                } else
                    builder.append(roleList.get(i).getName());
            }
            mapObject.put("roleName", builder);
        }
    }
}

