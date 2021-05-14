package com.vekai.base.detector;

import cn.fisok.raw.lang.MapObject;

/**
 * 检查器上下文容器初始化执行器
 */
public abstract class DetectorContextInitor {
    public abstract void init(DetectorContext context);

    public final void  init0(DetectorContext context, MapObject params){
        context.setParam(params);
    }

    protected DetectorContextInitor setContextParam(DetectorContext context,String xpath, Object object) {
        context.setParam(xpath,object);
        return this;
    }
}
