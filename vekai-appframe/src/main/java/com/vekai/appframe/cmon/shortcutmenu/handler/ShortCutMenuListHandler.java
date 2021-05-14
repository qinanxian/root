package com.vekai.appframe.cmon.shortcutmenu.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.PaginResult;
import com.vekai.appframe.cmon.shortcutmenu.entity.CmonShortcutMenu;
import com.vekai.auth.autoconfigure.AuthProperties;
import com.vekai.auth.entity.Role;
import com.vekai.auth.entity.RolePermit;
import com.vekai.auth.entity.User;
import com.vekai.auth.holder.AuthHolder;
import com.vekai.auth.service.AuthService;
import com.vekai.base.menu.model.MenuEntry;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ShortCutMenuListHandler extends MapDataListHandler {


    @Autowired
    BeanCruder beanCruder;
    @Autowired
    AuthService authService;
    @Autowired
    AuthProperties authProperties;


    @Override
    public PaginResult<MapObject> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        Boolean isNeedFilterMenu = authProperties.isAuthzFilterEnabled();
        if (isNeedFilterMenu) {
            Set<Role> roles = authService.getUserRoles(AuthHolder.getUser().getId());
            List<String> menuList = roles.stream().map(role -> this.getRolePermit(role.getId()))
                    .flatMap(Collection::stream).filter(rolePermit -> rolePermit.getPermitCode().contains("@menu"))
                    .map(rolePermit -> rolePermit.getPermitCode().split(":")[1])
                    .map(menuId -> menuId.startsWith("/")? menuId.substring(1):menuId)
                    .map(sortCode -> this.getMenuId(sortCode))
                    .filter(sortCode->sortCode!= null)
                    .collect(Collectors.toList());
            String urls = menuList.stream()
                    .map(url -> "\'" + url + "\'")
                    .collect(Collectors.joining(","));
            dataForm.getQuery().setWhere(dataForm.getQuery().getWhere() + " AND ID IN (" + urls + ")");
        }
        PaginResult<MapObject> result= super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
        return result;
    }

    private String getMenuId(String sortCode) {
        String sql = "select * from FOWK_MENU WHERE SORT_CODE=:sortCode";
        MenuEntry menuEntry=  beanCruder.selectOne(MenuEntry.class, sql, MapKit.mapOf("sortCode", sortCode));
        return menuEntry != null? menuEntry.getId():null;
    }

    private List<RolePermit> getRolePermit(String roleId) {
        String sql = "SELECT * FROM AUTH_ROLE_PERMIT WHERE ROLE_ID=:roleId";
        List<RolePermit> rolePermits = beanCruder.selectList(RolePermit.class,sql,"roleId",roleId);
        return rolePermits;
    }


    public int apply(DataForm dataForm, MapObject data) {
        User user = AuthHolder.getUser();
        int result=0;
        Object[]  objects = data.getValue("selectedData").objectArray();
        List<Object> objectList = Arrays.asList(objects);
        CmonShortcutMenu cmonMenuShortcut = null;
        for (Object object : objectList) {
            MenuEntry menu = new MenuEntry();
            BeanKit.copyProperties(object,menu);
            cmonMenuShortcut=new CmonShortcutMenu();
            cmonMenuShortcut.setMeunId(menu.getId());
            cmonMenuShortcut.setMenuName(menu.getName());
            cmonMenuShortcut.setMenuIcon(menu.getIcon());
            cmonMenuShortcut.setRelatedUserId(user.getId());
            cmonMenuShortcut.setMenuUrl(menu.getUrl());
            result +=beanCruder.save(cmonMenuShortcut);
        }
        return result;

    }
}
