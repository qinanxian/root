package com.vekai.appframe.conf.dashboard.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.appframe.cmon.dashboard.entity.ConfDashBoardPO;
import com.vekai.appframe.cmon.dashboard.entity.ConfDashBoardRolePO;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by luyu on 2018/6/14.
 */
@Component
public class HomeBlockPickListHandler extends BeanDataListHandler<ConfDashBoardPO> {

    @Transactional
    public Integer relateDashBoard(DataForm dataForm, MapObject object) {
        if (object.isEmpty())
            return 1;
        String roleId = ((Map<String,String>)object.get("0")).get("roleId").toString();
        List<ConfDashBoardRolePO>  confDashBoardPOList = new ArrayList<>();
        object.forEach((k,v) -> {
            Map<String,String> map = (Map<String,String>)v;
            String curRoleId = map.get("roleId").toString();
            String boardKey = map.get("boardKey").toString();
            ConfDashBoardRolePO confDashBoardRole = new ConfDashBoardRolePO();
            confDashBoardRole.setBoardKey(boardKey);
            confDashBoardRole.setRoleId(curRoleId);
            confDashBoardPOList.add(confDashBoardRole);
        });

        String deleteSql = "DELETE FROM CONF_DASH_BOARD_ROLE WHERE ROLE_ID=:roleId";
        beanCruder.execute(deleteSql,MapKit.mapOf("roleId",roleId));
        return beanCruder.save(confDashBoardPOList);
    }
}
