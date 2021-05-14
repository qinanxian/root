package com.vekai.appframe.cmon.shortcutmenu.service.impl;

import com.vekai.appframe.cmon.shortcutmenu.entity.CmonShortcutMenu;
import com.vekai.appframe.cmon.shortcutmenu.service.ShortCutMenuService;
import com.vekai.auth.entity.User;
import com.vekai.auth.holder.AuthHolder;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ShortCutMenuServiceImpl implements ShortCutMenuService {

    @Autowired
    private BeanCruder beanCruder;


    @Override
    public List<CmonShortcutMenu> getShortCutMenuList() {
        User user = AuthHolder.getUser();
        String sql = "select * from CMON_SHORTCUT_MENU WHERE RELATED_USER_ID=:userId";
        List<CmonShortcutMenu> cmonMenuShortcutList = beanCruder.selectList(CmonShortcutMenu.class,
                sql, MapKit.mapOf("userId", user.getId()));
        return cmonMenuShortcutList;
    }

    @Override
    @Transactional
    public Integer deleteShortCutMenu(String shortCutMenuId) {
        String deleteSql = "DELETE FROM CMON_SHORTCUT_MENU WHERE ID=:shortCutMenuId";
        return beanCruder.execute(deleteSql,MapKit.mapOf("shortCutMenuId",shortCutMenuId));
    }
}
