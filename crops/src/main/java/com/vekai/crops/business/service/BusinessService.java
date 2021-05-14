package com.vekai.crops.business.service;

import com.vekai.crops.business.mapper.BusinessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BusinessService {

    @Autowired
    private BusinessMapper businessMapper;

    public Integer whiteEntry(String applyId){

        Integer count = businessMapper.whiteEntry(applyId);

        return count;
    }

}
