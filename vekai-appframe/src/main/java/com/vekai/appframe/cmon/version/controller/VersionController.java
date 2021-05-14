package com.vekai.appframe.cmon.version.controller;

import com.vekai.appframe.cmon.version.entity.CmonVersion;
import com.vekai.appframe.cmon.version.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by luyu on 2018/9/30.
 */
@RestController
@RequestMapping("/common/version")
public class VersionController {

    @Autowired
    VersionService versionService;

    @GetMapping("/latest")
    public CmonVersion getVersionInfo() {
        return versionService.getVersionInfo();
    }
}
