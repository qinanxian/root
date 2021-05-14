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
package cn.fisok.raw.beans.propertyeditors;


import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.ValueObject;

import java.beans.PropertyEditorSupport;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 23:45
 * @desc : 用于spring-batch处理Long和字串的相互转换
 */
public class LongPropertyEditor extends PropertyEditorSupport {
    private Long defaultValue = null;

    public LongPropertyEditor(){

    }
    public LongPropertyEditor(Long defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setAsText(String text) throws IllegalArgumentException {
        Long value = defaultValue;
        if(StringKit.isNotBlank(text)){
            value = ValueObject.valueOf(text).longValue();
        }
        setValue(value);
    }
}
