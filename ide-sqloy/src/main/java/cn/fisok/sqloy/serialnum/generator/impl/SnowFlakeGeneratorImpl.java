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

import cn.fisok.raw.kit.SnowFlake;
import cn.fisok.sqloy.serialnum.consts.GeneratorType;
import cn.fisok.sqloy.serialnum.generator.SerialNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/7/27
 * @desc :
 */
@Component(GeneratorType.SNOW_FLAKE)
public class SnowFlakeGeneratorImpl implements SerialNumberGenerator<Long> {

    @Autowired
    SnowFlake snowFlake;

    @Override
    public Long next(String generatorId) {
        return next(generatorId,null);
    }

    @Override
    public Long next(String generatorId, Object object) {
        return snowFlake.nextId();
    }

    @Override
    public Long[] nextBatch(String generatorId, int count) {
        return nextBatch(generatorId,count,null);
    }

    @Override
    public Long[] nextBatch(String generatorId, int count, Object object) {
        List<Long> snList = new ArrayList<>();
        for(int i=0;i<count;i++){
            snList.add(snowFlake.nextId());
        }
        return snList.toArray(new Long[count]);
    }
}
