package com.vekai.appframe.cmon.dashboard.controller;

import com.vekai.appframe.cmon.dashboard.dto.DashBoard;
import com.vekai.appframe.cmon.dashboard.service.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Created by luyu on 2018/5/28.
 */
@RestController
@RequestMapping("common/dashboard")
public class DashboardController{

    @Autowired
    DashBoardService dashboardSerivce;

    @PostMapping("save/{userId}")
    public Integer updateDashboard(@RequestBody List<DashBoard> dashboards,
                        @PathVariable("userId")String userId) {
        Integer result = dashboardSerivce.updateDashboards(dashboards,userId);
        return result;
    }

    @RequestMapping("/{userId}")
    public List<DashBoard> getDashBoard(@PathVariable("userId")String userId) {
        List<DashBoard> dashBoards = dashboardSerivce.getDashBoard(userId);
        return dashBoards;
    }

    @RequestMapping("reset/{userId}")
    public List<DashBoard> resetDashBoard(@PathVariable("userId")String userId) {
        List<DashBoard> dashBoards = dashboardSerivce.resetDashBoard(userId);
        return dashBoards;
    }
}
