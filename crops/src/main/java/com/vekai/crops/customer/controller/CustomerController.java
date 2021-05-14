package com.vekai.crops.customer.controller;


import com.vekai.crops.customer.entity.CustomerPO;
import com.vekai.crops.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cust")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping("/byId/{custId}")
    public CustomerPO getCustomer(@PathVariable("custId") String custId) {
        return customerService.getCustomer(custId);
    }
}
