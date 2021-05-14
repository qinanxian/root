package com.vekai.base;

import com.vekai.base.menu.model.MenuEntry;
import cn.fisok.sqloy.core.BeanCruder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by luyu on 2017/12/19.
 */
public class MenuTest extends BaseTest {

    @Autowired
    protected BeanCruder beanCruder;

    @Test
    public void testGetMenu() {
        List<MenuEntry> menuEntries = MenuTestData.menuEntries;
        beanCruder.save(menuEntries);
    }
}
