package com.vekai.fnat.service.impl;

import com.vekai.fnat.model.BaseInterestRate;
import com.vekai.fnat.service.BaseInterestRateService;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class BaseInterestRateServiceImpl implements BaseInterestRateService {
    public static final String CACHE_KEY = "baseInterestRateCache";

    @Autowired
    protected BeanCruder beanCruder;



//    @Cacheable(value=CACHE_KEY,key="#rateType")
    public List<BaseInterestRate> getBaseInterestRateList(String rateType) {
        String sql = "select * from FNAT_INTEREST_RATE where RATE_TYPE=:rateType order by EFFECT_DATE asc,SORT_CODE ASC,ID asc";
        return beanCruder.selectList(BaseInterestRate.class,sql,"rateType",rateType);
    }


//    @CacheEvict(value=CACHE_KEY,beforeInvocation=true)
    public void clearCache(){

    }
}
