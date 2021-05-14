package com.vekai.workflow.nopub.bom.impl;

import com.vekai.workflow.nopub.bom.BomAttribute;
import com.vekai.workflow.nopub.bom.BomContext;
import com.vekai.workflow.nopub.bom.BomObject;
import com.vekai.workflow.nopub.bom.BomParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;

/**
 * 业务对象默认解析器
 */
@Component
public class BomParserImpl implements BomParser{
    private ScriptEngineManager engineManager = new ScriptEngineManager();
    private ScriptEngine engine = engineManager.getEngineByName("groovy");
    private String globalScript;
    public String getGlobalScript() {
        return globalScript;
    }

    public void setGlobalScript(String globalScript) {
        this.globalScript = globalScript;
    }

    public BomParserImpl() {
    }

    @Override
    public void parse(BomObject bomObject,BomContext context) {
        Bindings variables = engine.createBindings();
        if (context != null) {
            variables.putAll(context);
        }
        parseExpression(bomObject,variables);

    }


    protected void parseExpression(BomObject bomObject,Bindings binding){
        //1.运行全局脚本
        try {
            if(!StringUtils.isBlank(globalScript)){
                engine.eval(globalScript,binding);
            }
        } catch (ScriptException e) {
            throw new RuntimeException("配置出错，globalScript不能运行,Script:"+globalScript,e);
        }
        //2.执行每一条规则
        List<BomAttribute> attributes = bomObject.getAttributes();
        for(BomAttribute attr : attributes){
            String script = attr.getExpression();
            if(StringUtils.isBlank(script))continue;
            try {
                attr.setValue(engine.eval(script,binding));
            } catch (ScriptException e) {
                throw new RuntimeException("运行脚本出错,Script:"+script,e);
            }
        }

    }
}
