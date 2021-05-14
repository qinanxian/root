package com.vekai.workflow.liteflow.handler;

import java.util.List;

/**
 * 查找适用于简式流程的数据处理类
 */
public interface LiteHandlerResolve {
    List<LiteHandler> findHandlers(String definitionKey);
}
