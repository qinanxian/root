package com.vekai.appframe.auth.handler;

import cn.fisok.sqloy.core.PaginResult;
import com.vekai.auth.entity.User;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by sylu on 2019/1/16.
 */
@Component
public class SimpleUserListHandler  extends BeanDataListHandler<User> {

    @Override
    public PaginResult<User> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        return super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
    }
}
