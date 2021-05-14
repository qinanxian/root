package com.vekai.workflow.nopub.bom;

/**
 * 业务对象模型加载接口
 * @author 杨松<syang@amarsoft.com>
 * @date 2017年1月19日
 */
public interface BomLoader {
    BomObject load(String bomDefinitionKey);
}
