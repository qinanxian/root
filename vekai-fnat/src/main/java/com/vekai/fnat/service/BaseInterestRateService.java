package com.vekai.fnat.service;

import com.vekai.fnat.model.BaseInterestRate;

import java.util.List;

public interface BaseInterestRateService {

    void clearCache();
    /**
     * 取指定类型的基准利率
     * @param rateType
     * @return
     */
    List<BaseInterestRate> getBaseInterestRateList(String rateType);
}
