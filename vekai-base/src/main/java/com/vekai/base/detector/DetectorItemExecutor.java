package com.vekai.base.detector;

/**
 * 检查器执行项
 */
public interface DetectorItemExecutor {
    DetectorMessage exec(DetectorContext ctx);
}
