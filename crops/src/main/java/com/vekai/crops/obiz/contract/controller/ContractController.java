package com.vekai.crops.obiz.contract.controller;

import com.vekai.crops.obiz.contract.entity.ObizContract;
import com.vekai.crops.obiz.contract.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contract")
public class ContractController {
    @Autowired
    ContractService contractService;

    @GetMapping("/byId/{contractId}")
    public ObizContract queryContract(@PathVariable("contractId") String contractId) {
        return contractService.queryContract(contractId);
    }
}
