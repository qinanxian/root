package com.vekai.appframe.base.pilot.controller;


import com.vekai.appframe.base.pilot.service.MyPilotService;
import com.vekai.base.pilot.model.StartNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appframe/pilot")
public class PilotController {
    @Autowired
    MyPilotService myPilotService;
    @GetMapping("/my")
    public StartNode getMyPilot(){
        return myPilotService.getMyPilot();
    }

    /**
     * 获取系统中所有的菜单数据以及权限项
     * @return
     */
    @GetMapping("/all")
    public StartNode getSysPilot(){
        return myPilotService.getSysPilot();
    }

}
