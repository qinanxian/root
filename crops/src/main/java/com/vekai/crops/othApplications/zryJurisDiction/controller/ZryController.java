package com.vekai.crops.othApplications.zryJurisDiction.controller;

import com.vekai.crops.othApplications.zryJurisDiction.service.ZryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/zry")
public class ZryController {
    @Autowired
    ZryService zryService;

    @PostMapping("/getPreQuota/{loanId}")
    @ResponseBody
    public String getUserList(@PathVariable("loanId") String loanId) {

        return zryService.getPreQuota(loanId);
    }
}