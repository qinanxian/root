/*
 * Copyright 2019-2029 FISOK(www.fisok.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.fisok.raw.script;

import cn.fisok.raw.holder.ApplicationContextHolder;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.lang.RawException;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 23:41
 * @desc :
 */
public class ScriptParser<T> {
    private ScriptEngine scriptEngine;

    public ScriptParser() {
    }

    public ScriptParser(ScriptEngine scriptEngine) {
        this.scriptEngine = scriptEngine;
    }

    public ScriptEngine getScriptEngine() {
        return scriptEngine;
    }

    public void setScriptEngine(ScriptEngine scriptEngine) {
        this.scriptEngine = scriptEngine;
    }

    public T parse(CharSequence expression, Map<String,Object> parameters) throws ScriptException {
        if(scriptEngine == null){
            scriptEngine = ApplicationContextHolder.getBean("scriptEngine",ScriptEngine.class);
        }

        if(scriptEngine == null){
            throw new RawException("ScriptEngine对象不存在，请初始化springboot环境");
        }
        Bindings variables = scriptEngine.createBindings();
        variables.putAll(parameters);

        //执行
        Object result = scriptEngine.eval(expression.toString(),variables);
        return (T)result;
    }

    public T parse(CharSequence expression) throws ScriptException {
        return parse(expression, MapKit.newEmptyMap());
    }
}
