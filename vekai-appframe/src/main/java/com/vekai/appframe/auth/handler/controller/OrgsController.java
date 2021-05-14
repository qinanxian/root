package com.vekai.appframe.auth.handler.controller;

import com.vekai.appframe.auth.service.UserOrgService;
import com.vekai.appframe.constant.AppframeConst;
import com.vekai.auth.entity.UserOrg;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.lang.MapObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class OrgsController {
    @Autowired
    UserOrgService userOrgService;

    @RequestMapping(value = "/User/addOrg",method = RequestMethod.POST)
    @ResponseBody
    public int addOrg(@RequestBody List<MapObject> dataList){
        UserOrg userOrg = new UserOrg();
        BeanKit.copyProperties(dataList.get(0),userOrg);
        userOrg.setOrgId(dataList.get(0).getValue("id").strValue());
        userOrg.setIsMain(AppframeConst.YES_NO_N);
        userOrg.setId(null);
        userOrgService.saveUserOrg(userOrg);
        return 1;
    }
}
