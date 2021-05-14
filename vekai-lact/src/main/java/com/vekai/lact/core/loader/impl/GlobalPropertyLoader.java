package com.vekai.lact.core.loader.impl;

import cn.fisok.raw.lang.MapObject;
import com.vekai.lact.core.loader.DataObjectLoader;
import com.vekai.lact.model.LactLoan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 全局参数加载器，例如年基础天数等系统全局设置的统一参数
 */
public class GlobalPropertyLoader implements DataObjectLoader<LactLoan>{
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public void load(LactLoan loan, MapObject parameter) {
        logger.trace("--加载全局参数--");
        loan.setYearDays(360);  //年基础天数
    }
}
