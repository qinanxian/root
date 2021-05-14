package com.vekai.appframe.conf.dashboard.handler;

import com.vekai.appframe.cmon.dashboard.entity.ConfDashBoardPO;
import com.vekai.dataform.handler.impl.BeanDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.ValueObject;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by luyu on 2018/9/17.
 */
@Component
public class HomeBlockInfoHandler extends BeanDataOneHandler<ConfDashBoardPO> {

    @Override
    public ConfDashBoardPO query(DataForm dataForm, Map<String, ?> queryParameters) {

        String boardKey = ValueObject.valueOf(queryParameters.get("boardKey")).strValue();
        if (!StringKit.isBlank(boardKey) && !"null".equals(boardKey)) {
            dataForm.getElements().stream().filter(item -> "boardKey".equals(item.getCode())).forEach(item -> {
                item.getElementUIHint().setReadonly(Boolean.TRUE);
            });
            dataForm.getQuery().setWhere("BOARD_KEY=:boardKey");
        }
        ConfDashBoardPO confDashBoardPO = super.query(dataForm,queryParameters);
        return confDashBoardPO;
    }
}
