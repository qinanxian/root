package com.vekai.appframe.cmon.shortcutmenu.service;


import com.vekai.appframe.cmon.shortcutmenu.entity.CmonShortcutMenu;

import java.util.List;

public interface ShortCutMenuService {

    List<CmonShortcutMenu>  getShortCutMenuList();

    //删除快捷菜单
    Integer deleteShortCutMenu(String shortCutMenuId);
}
