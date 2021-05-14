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

import cn.fisok.raw.kit.NumberKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.sqloy.autoconfigure.SqloyProperties;
import cn.fisok.sqloy.serialnum.consts.GeneratorType;
import cn.fisok.sqloy.serialnum.generator.SerialNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/1/10
 * @desc : 自动递增序列流水号+随机数字
 */
@Component(GeneratorType.AUTO_INCREMENT36_WITH_RANDOM)
public class AutoIncrement36WithRandomGeneratorImpl implements SerialNumberGenerator<String> {
    @Autowired
    SqloyProperties sqloyProperties;
    @Autowired AutoIncrementGeneratorImpl autoIncrementGenerator;

    public String next(String generatorId) {
        return nextBatch(generatorId,1)[0];
    }

    public String next(String generatorId, Object object) {
        return nextBatch(generatorId,1,null)[0];
    }

    @Transactional
    public String[] nextBatch(String generatorId, int count) {
        return nextBatch(generatorId,count,null);
    }

    public String[] nextBatch(String generatorId, int count, Object object) {
        Long[] numbers = autoIncrementGenerator.nextNumbers(generatorId,count);

        List<String> retList = new ArrayList<String>(count);
        String valueDefaultTpl = sqloyProperties.getSerialNumber().getDefaultTemplate();
        String valueTpl = StringKit.nvl(sqloyProperties.getSerialNumber().getTemplateMap().get(generatorId),valueDefaultTpl);
        valueTpl = StringKit.nvl(valueTpl,"000000");

        for(int i=0;i<count;i++){
            String snItem = NumberKit.convert36Radix(numbers[i]);
            snItem = StringKit.leftPad(snItem,valueTpl.length(),"0");
            String random = NumberKit.convert36Radix(NumberKit.randomInt(0,36*36));
            retList.add(snItem+random);
        }

        return retList.toArray(new String[count]);
    }
}
