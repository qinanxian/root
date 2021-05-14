package com.vekai.base.menu.service;

import com.vekai.base.menu.model.MenuEntry;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by luyu on 2017/12/19.
 */
@Service
public class MenuService {

    @Autowired
    private BeanCruder beanCruder;

    public List<MenuEntry> getUserMenu() {
        List<MenuEntry> menuList = beanCruder.selectList(MenuEntry.class,"select * from FOWK_MENU where ENABLED='Y' ORDER BY SORT_CODE ASC");
        return menuList;
    }

    public List<MenuEntry> getAllMenus() {
        List<MenuEntry> menuList = beanCruder.selectList(MenuEntry.class,"select * from FOWK_MENU where ENABLED='Y' ORDER BY SORT_CODE ASC");
        return menuList;
    }
}
