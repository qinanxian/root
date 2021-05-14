package com.vekai.appframe.cmon.doclist.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.appframe.cmon.file.handler.BaseFileHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.lang.ValueObject;
import cn.fisok.sqloy.core.PaginResult;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CmonDocListItemHandler extends BaseFileHandler {


    public PaginResult<MapObject> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        if (null != queryParameters.get("groupCode")) {
            String groupCode = ValueObject.valueOf(queryParameters.get("groupCode")).strValue();
            dataForm.getQuery().setWhere("DL.OBJECT_ID=:objectId AND DL.OBJECT_TYPE=:objectType and DG.GROUP_CODE='" + groupCode + "' ");
        }
        return super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
    }

}
