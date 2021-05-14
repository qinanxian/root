package com.vekai.base.menu.controller;

import com.vekai.base.menu.model.MenuEntry;
import com.vekai.base.menu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/base/menu")
public class MenuController {


    @Autowired
    private MenuService menuService;

    @GetMapping("/userMenu")
    public List<MenuEntry> getUserMenu(){
        return menuService.getUserMenu();
    }
}
