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
package cn.fisok.sqloy.serialnum.finder.impl;

import cn.fisok.sqloy.autoconfigure.SqloyProperties;
import cn.fisok.sqloy.serialnum.finder.SerialNumberGeneratorFinder;
import cn.fisok.sqloy.serialnum.generator.SerialNumberGenerator;
import cn.fisok.raw.holder.ApplicationContextHolder;
import cn.fisok.raw.kit.StringKit;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 流水号生成器实现类查找实现类
 */
//@Component
public class SerialNumberGeneratorFinderImpl implements SerialNumberGeneratorFinder {

    private static final String GENERATOR_VALIDATOR_TEMPLATE =
            "流水号生成器[{0}]对应的实现Bean[{1}]不存在，并且默认的流水号生成器[{2}]也不可用";

    private SqloyProperties sqloyProperties;

    public SqloyProperties getSqloyProperties() {
        return sqloyProperties;
    }

    @Autowired
    public void setSqloyProperties(SqloyProperties sqloyProperties) {
        this.sqloyProperties = sqloyProperties;
    }

    @Override
    public SerialNumberGenerator find(String generatorId) {
        //查找 属性 对应的流水号生成器（类）
        String beanName = sqloyProperties.getSerialNumber().getGeneratorMap().get(generatorId);
        SerialNumberGenerator generator = null;
        if (StringKit.isNotBlank(beanName)) {
            generator = ApplicationContextHolder.getBean(beanName, SerialNumberGenerator.class);
        }
        //如果没有，就使用默认的
        String defaultGenerator = sqloyProperties.getSerialNumber().getDefaultGenerator();
        if (generator == null) {
            generator = ApplicationContextHolder.getBean(defaultGenerator, SerialNumberGenerator.class);
        }

        Validate.notNull(generator, GENERATOR_VALIDATOR_TEMPLATE, generatorId, beanName,
                defaultGenerator);

        return generator;
    }

}
