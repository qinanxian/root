package com.vekai.base.detector.service;

import com.vekai.base.detector.model.Detector;
import com.vekai.base.detector.model.DetectorItem;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetectorService {
    public static final String CACHE_KEY = "DetectorCache";

    @Autowired
    protected BeanCruder beanCruder;

    @Cacheable(value = CACHE_KEY, key = "#code")
    public Detector getDetector(String code) {

        Detector detector = beanCruder.selectOne(Detector.class,
                "select * from CONF_DETECTOR where CODE=:code"
                , "code", code);
        if (detector != null) {
            List<DetectorItem> detectorItemList =
                    beanCruder.selectList(DetectorItem.class
                            , "select * from CONF_DETECTOR_ITEM where DETECTOR_CODE=:detectorCode and ITEM_STATUS=:itemStatus order by SORT_CODE ASC,GROUP_CODE ASC,ITEM_CODE ASC"
                            , "detectorCode", code,
                            "itemStatus", "VALID");
            detector.setItems(detectorItemList);
        }


        return detector;
    }

    /**
     * 清空缓存
     */
    @CacheEvict(value=CACHE_KEY,allEntries=true,beforeInvocation=true)
    public void clearCacheAll(){

    }

    /**
     * 清空缓存
     */
    @CacheEvict(value=CACHE_KEY,key="#code",beforeInvocation=true)
    public void clearCacheItem(String code){
    }
}
