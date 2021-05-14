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

import cn.fisok.raw.holder.ApplicationContextHolder;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.sqloy.autoconfigure.SqloyProperties;
import cn.fisok.sqloy.serialnum.consts.GeneratorType;
import cn.fisok.sqloy.serialnum.cursor.SerialNumberCursorDao;
import cn.fisok.sqloy.serialnum.generator.SerialNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/1/4
 * @desc : 自动递增序列流水号生成器
 */
@Component(GeneratorType.AUTO_INCREMENT)
public class AutoIncrementGeneratorImpl extends AbstractRecordGenerator implements SerialNumberGenerator<String> {

    @Autowired
    protected SqloyProperties sqloyProperties;
    protected SerialNumberCursorDao serialNumberCursorDao;

    public String next(String generatorId) {
        return next(generatorId,null);
    }

    public String next(String generatorId, Object object) {
        return nextBatch(generatorId,1)[0];
    }

    public SerialNumberCursorDao getSerialNumberCursorDao() {
        if(serialNumberCursorDao == null){
            String cursorRecordType = sqloyProperties.getSerialNumber().getSnCursorRecordType();
            return ApplicationContextHolder.getBean(cursorRecordType,SerialNumberCursorDao.class);
        }
        return serialNumberCursorDao;
    }

    @Transactional
    public String[] nextBatch(String generatorId, int count) {
        return nextBatch(generatorId,count,null);
    }

    public String[] nextBatch(String generatorId, int count, Object object) {
        Long[] numbers = nextNumbers(generatorId,count);

        List<String> retList = new ArrayList<String>(count);
        String valueDefaultTpl = sqloyProperties.getSerialNumber().getDefaultTemplate();
        String valueTpl = StringKit.nvl(sqloyProperties.getSerialNumber().getTemplateMap().get(generatorId),valueDefaultTpl);
        valueTpl = StringKit.nvl(valueTpl,"0");

        DecimalFormat decimalFormat=new DecimalFormat(valueTpl);
        for(int i=0;i<count;i++){
            retList.add(decimalFormat.format(numbers[i]));
        }

        return retList.toArray(new String[count]);
    }
}
