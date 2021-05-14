package com.vekai.appframe.auth.handler;

import cn.fisok.sqloy.core.PaginResult;
import com.vekai.auth.entity.User;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserListForRoleHandler extends BeanDataListHandler<User> {

    public PaginResult<User> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
//        String sql = "SELECT AU.* FROM AUTH_USER AU,AUTH_USER_ROLE UR,AUTH_ROLE AR WHERE AR.ID = UR.ROLE_ID AND UR.USER_ID = AU.ID AND AR.ID = :roleId ORDER BY AU.ID ASC";
//
//        List<User> userList = beanCruder.selectList(User.class,sql,queryParameters);
//
//        if(userList.size()>0) {
//            String notIn = userList.stream().map(user -> user.getId()).map(id -> "'"+id+"'").reduce((a,b)->a+","+b).orElseGet(()->null);
//            String where = "STATUS='Using' and ID NOT IN ("+ notIn +")";
//            dataForm.getQuery().setWhere(where);
//        }

        return super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
    }
}
