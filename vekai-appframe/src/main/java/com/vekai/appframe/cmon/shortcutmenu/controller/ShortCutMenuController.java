package com.vekai.appframe.cmon.shortcutmenu.controller;

import com.vekai.appframe.cmon.shortcutmenu.entity.CmonShortcutMenu;
import com.vekai.appframe.cmon.shortcutmenu.service.ShortCutMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shortcutMenu")
public class ShortCutMenuController {

    @Autowired
    private ShortCutMenuService shortCutMenuService;

    /*
     *查询当前快捷菜单
     */
    @GetMapping(path = "/query")
    public List<CmonShortcutMenu> getMenuShortCut() {
        return shortCutMenuService.getShortCutMenuList();
    }

    /*
     *删除某个快捷菜单
     */
    @PostMapping(path = "/delete/{shortcutMenuId}")
    public void delete(@PathVariable("shortcutMenuId") String shortcutMenuId) {
        shortCutMenuService.deleteShortCutMenu(shortcutMenuId);
    }
}
