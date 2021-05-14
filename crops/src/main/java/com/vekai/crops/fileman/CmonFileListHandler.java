package com.vekai.crops.fileman;

import com.vekai.common.fileman.entity.FileEntity;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CmonFileListHandler extends BeanDataListHandler<FileEntity>{

    public Integer delete(DataForm dataForm, List<FileEntity> dataList) {
        Integer result = 0;


        return result;
    }
}
