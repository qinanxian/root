package com.vekai.crops.obiz.duebill.controller;

import com.vekai.crops.obiz.duebill.entity.ObizDuebill;
import com.vekai.crops.obiz.duebill.service.DuebillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/duebill")
public class DuebillController {

    @Autowired
    DuebillService duebillService;

    @GetMapping("/byId/{duebillId}")
    public ObizDuebill queryDuebill(@PathVariable("duebillId") String duebillId) {
        return duebillService.getDuebill(duebillId);
    }
}
