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
package cn.fisok.sqloy.serialnum.generator.impl;

import cn.fisok.raw.kit.StringKit;
import cn.fisok.sqloy.serialnum.consts.GeneratorType;
import cn.fisok.sqloy.serialnum.generator.SerialNumberGenerator;
import org.springframework.stereotype.Component;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : UUID流水号生成器
 */
@Component(GeneratorType.UUID)
public class UUIDGeneratorImpl implements SerialNumberGenerator<String> {

    public String next(String generatorId) {
        return next(generatorId,null);
    }

    public String next(String generatorId, Object object) {
        return StringKit.uuid();
    }


    public String[] nextBatch(String generatorId, int count) {
        return nextBatch(generatorId,count,null);
    }

    public String[] nextBatch(String generatorId, int count, Object object) {
        String[] serialNos = new String[count];
        for (int i = 0; i < count; i++) {
            serialNos[i] = next(generatorId);
        }

        return serialNos;
    }

}
