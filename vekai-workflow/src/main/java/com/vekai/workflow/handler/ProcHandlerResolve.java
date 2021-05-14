package com.vekai.workflow.handler;

import java.util.List;

/**
 * 查找适用于流程的数据处理类
 */
public interface ProcHandlerResolve {
    List<ProcHandler> findHandlers(String definitionKey);
}
