package com.vekai.lact.core.loader.impl;

import cn.fisok.raw.lang.MapObject;
import com.vekai.lact.core.loader.DataObjectLoader;
import com.vekai.lact.entity.LactLoanSegmentEntity;
import com.vekai.lact.model.LactLoan;
import cn.fisok.raw.kit.BeanKit;

/**
 * 简单的扁平结构的数据填充
 */
public class FillSimpleDataObjectFromMapLoader implements DataObjectLoader<LactLoan> {
    @Override
    public void load(LactLoan loan, MapObject parameter) {
        LactLoanSegmentEntity segment = loan.getOnlyOneSegment();

        MapObject segmentMapObject = parameter.getSubDataObject("segment");

        //MAP到数据的转换
        BeanKit.mapFillBean(parameter,loan);
        BeanKit.mapFillBean(segmentMapObject,segment);

    }
}
