package com.vekai.dataform.context;

import java.util.Map;

/**
 * 环境变量提取（例如会话用户，当前机构等常用变量对象）
 */
public interface ContextEnvFetcher {
    Map<String,Object> fetchContextEnvParams();
}
