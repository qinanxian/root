package com.vekai.appframe.cmon.dashboard.service;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.appframe.cmon.dashboard.dto.DashBoard;
import com.vekai.appframe.cmon.dashboard.entity.CmonDashBoardPO;
import com.vekai.appframe.cmon.dashboard.entity.ConfDashBoardPO;
import com.vekai.appframe.cmon.dashboard.entity.ConfDashBoardRolePO;
import com.vekai.appframe.constant.AppframeConst;
import com.vekai.appframe.constant.AppframeObjectType;
import com.vekai.auth.entity.Role;
import com.vekai.auth.entity.UserBehavior;
import com.vekai.auth.service.AuthService;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.MapKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by luyu on 2018/5/28.
 */
@Service
public class DashBoardService {

    @Autowired
    BeanCruder beanCruder;
    @Autowired
    AuthService authService;

    public List<DashBoard> getDashBoard(String userId) {
        List<CmonDashBoardPO> cmonDashBoardPOList = this.getCmonDashBoardByUserId(userId);
        List<DashBoard> inUsedDashBoards = null;
        List<DashBoard> dashBoards = this.getDefaultDashBoard(userId);
        if (cmonDashBoardPOList.size() > 0) {
            Map<String,CmonDashBoardPO> cmonDashBoardPOMap = cmonDashBoardPOList.stream()
                    .collect(Collectors.toMap(CmonDashBoardPO::getBoardKey,
                            CmonDashBoardPO -> CmonDashBoardPO));
            dashBoards.forEach(item -> {
                if (cmonDashBoardPOMap.containsKey(item.getBoardKey())) {
                    BeanKit.copyProperties(cmonDashBoardPOMap.get(item.getBoardKey()),item);
                    item.setIsUsed(AppframeConst.YES_NO_Y);
                }
            });
        }
        return dashBoards;
    }

    private Map<String,ConfDashBoardPO> getDefaultConfDashBoard(String userId) {
        //获取角色配置的组件
        Set<Role> roles = authService.getUserRoles(userId);
        Map<String,ConfDashBoardPO> confDashBoardPOMap = new HashMap<>();
        for(Role role: roles){
            List<ConfDashBoardPO> confDashBoardPOList = this.getConfDashBoardListByRoleId(role.getId());
            confDashBoardPOMap.putAll(confDashBoardPOList.stream()
                    .collect(Collectors.toMap(ConfDashBoardPO::getBoardKey,
                            ConfDashBoardPO -> ConfDashBoardPO)));
        }

        //获取公共的组件
        List<ConfDashBoardPO> pubConfDashBoardPOList = this.getPublicConfDashBoard();
        confDashBoardPOMap.putAll(pubConfDashBoardPOList.stream()
                .collect(Collectors.toMap(ConfDashBoardPO::getBoardKey,
                        ConfDashBoardPO -> ConfDashBoardPO)));
        return confDashBoardPOMap;
    }

    private List<DashBoard> getDefaultDashBoard(String userId) {
        Map<String,ConfDashBoardPO> confDashBoardPOMap = this.getDefaultConfDashBoard(userId);
        List<ConfDashBoardPO> allConfDashBoards = new ArrayList<ConfDashBoardPO>();
        Set<String> keySet = confDashBoardPOMap.keySet();
        Iterator<String> it = keySet.iterator();
        while(it.hasNext()) {
            allConfDashBoards.add(confDashBoardPOMap.get(it.next()));
        }
        List<DashBoard> dashBoards = this.convertConfDashBoardToDashBoard(allConfDashBoards);
        return dashBoards;
    }

    private List<DashBoard> convertConfDashBoardToDashBoard(List<ConfDashBoardPO> allConfDashBoards) {
        List<DashBoard> dashBoards = new ArrayList<>();
        for (ConfDashBoardPO confDashBoardPO:allConfDashBoards) {
            DashBoard dashBoard = new DashBoard();
            BeanKit.copyProperties(confDashBoardPO,dashBoard);
            dashBoards.add(dashBoard);
            dashBoard.setIsUsed(AppframeConst.YES_NO_N);
        }
        return dashBoards;
    }

    private List<ConfDashBoardPO> getPublicConfDashBoard() {
        String sql = "SELECT * FROM CONF_DASH_BOARD WHERE IS_PUBLIC =:isPublic";
        return beanCruder.selectList(ConfDashBoardPO.class,sql,"isPublic", AppframeConst.YES_NO_Y);
    }

    private List<ConfDashBoardPO> getConfDashBoardListByRoleId(String roleId) {
        String sql = "SELECT * FROM CONF_DASH_BOARD_ROLE WHERE ROLE_ID=:roleId";
        List<ConfDashBoardRolePO> confDashBoardRolePOList = beanCruder.selectList(
                ConfDashBoardRolePO.class,sql,"roleId",roleId);
        return confDashBoardRolePOList.stream()
                .map(item -> this.getConfDashBoardByBoardKey(item.getBoardKey()))
                .collect(Collectors.toList());
    }

    private List<DashBoard> convertCmonDashBoardToDashBoard(List<CmonDashBoardPO> cmonDashBoardPOList) {
        List<DashBoard> dashBoards = new ArrayList<>();
        for (CmonDashBoardPO cmonDashBoardPO:cmonDashBoardPOList) {
            DashBoard dashBoard = new DashBoard();
            BeanKit.copyProperties(cmonDashBoardPO,dashBoard);
            ConfDashBoardPO confDashBoardPO = this.getConfDashBoardByBoardKey(cmonDashBoardPO.getBoardKey());
            dashBoard.setUri(confDashBoardPO.getUri());
            dashBoards.add(dashBoard);
        }
        return dashBoards;
    }

    private List<CmonDashBoardPO> getCmonDashBoardByUserId(String userId) {
        String sql = "SELECT * FROM CMON_DASH_BOARD WHERE USER_ID=:userId";
        return beanCruder.selectList(CmonDashBoardPO.class,sql,"userId",userId);
    }

    private ConfDashBoardPO getConfDashBoardByBoardKey(String boardKey) {
        String sql = "SELECT * FROM CONF_DASH_BOARD WHERE BOARD_KEY=:boardKey";
        return beanCruder.selectOne(ConfDashBoardPO.class,sql,"boardKey",boardKey);
    }

   /* private CmonDashboardPO getDashboardByCode(String code) {
        String sql = "SELECT * FROM CMON_DASHBOARD WHERE CODE =:code";
        CmonDashboardPO dashboardPO = beanCruder.selectOne(CmonDashboardPO.class,sql,"code",code);
        return dashboardPO;
    }*/

   /* public Integer updateDashboard(DashBoard dashboard, String userId) {
        UserBehavior userBehavior = Optional.ofNullable(this.getUserBehaviorByUserId(userId)).orElseGet(UserBehavior::new);
        userBehavior.setObjectType(ObjectType.DASHBOARD.toString());
        userBehavior.setValue(dashboard.getValue());
        userBehavior.setUserId(userId);
        return beanCruder.save(userBehavior);
    }*/

    private UserBehavior getUserBehaviorByUserId(String userId) {
        String sql = "SELECT * FROM AUTH_USER_BEHAVIOR WHERE USER_ID = :userId AND OBJECT_TYPE=:objectType";
        UserBehavior userBehavior = beanCruder.selectOne(UserBehavior.class,sql,
                MapKit.mapOf("userId",userId,"objectType", AppframeObjectType.DASHBOARD.toString()));
        return userBehavior;
    }

    public List<DashBoard> resetDashBoard(String userId) {
        String deleteSql = "DELETE FROM CMON_DASH_BOARD WHERE USER_ID=:userId";
        beanCruder.execute(deleteSql,MapKit.mapOf("userId",userId));
        return this.getDashBoard(userId);
    }

    public Integer updateDashboards(List<DashBoard> dashboards, String userId) {
        Integer result =1;
        String deleteSql = "DELETE FROM CMON_DASH_BOARD WHERE USER_ID=:userId";
        result += beanCruder.execute(deleteSql,MapKit.mapOf("userId",userId));
        dashboards = dashboards.stream()
                .filter(item -> AppframeConst.YES_NO_Y.equals(item.getIsUsed()))
                .collect(Collectors.toList());
        List<CmonDashBoardPO> cmonDashboards = this.convertDashBoardToCmonDashBoard(dashboards,userId);
        result +=beanCruder.save(cmonDashboards);
        return result;
    }

    private List<CmonDashBoardPO> convertDashBoardToCmonDashBoard(List<DashBoard> dashboards, String userId) {
        List<CmonDashBoardPO> cmonDashBoardPOList = new ArrayList<>();
        for (DashBoard dashBoard : dashboards) {
            CmonDashBoardPO cmonDashBoardPO = new CmonDashBoardPO();
            BeanKit.copyProperties(dashBoard,cmonDashBoardPO);
            cmonDashBoardPOList.add(cmonDashBoardPO);
            cmonDashBoardPO.setUserId(userId);
        }
        return cmonDashBoardPOList;
    }
}
