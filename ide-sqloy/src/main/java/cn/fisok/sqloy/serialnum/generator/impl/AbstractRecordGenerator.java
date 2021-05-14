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

import cn.fisok.raw.kit.DateKit;
import cn.fisok.sqloy.serialnum.cursor.SerialNumberCursorDao;
import cn.fisok.sqloy.serialnum.model.SerialNumberCursor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/1/4
 * @desc : 抽象记录的流水号生成器
 */
public abstract class AbstractRecordGenerator {
    public abstract SerialNumberCursorDao getSerialNumberCursorDao();

    /**
     * 生成一批流水号(纯数字）
     * @param generatorId 生成器标识 类名.属性名+可能的标识
     * @param count
     * @return
     */
    public Long[] nextNumbers(String generatorId,int count) {
        List<Long> retList = new ArrayList<Long>(count);

        SerialNumberCursorDao cursorDao = getSerialNumberCursorDao();
        cursorDao.lock(generatorId);

        SerialNumberCursor cursor = cursorDao.findOne(generatorId);

        Long cursorValue = (long)count;
        if(cursor == null){
            cursor = new SerialNumberCursor();
            cursor.setId(generatorId);
            cursor.setCursorValue(count+1);
            cursor.setCreatedTime(DateKit.now());
            cursor.setUpdatedTime(DateKit.now());
            cursorDao.insertOne(cursor);
            cursorValue = 1L;
        }else{
            cursorValue = cursor.getCursorValue();
            cursorDao.increase(generatorId,count);
        }
        cursorDao.unlock(generatorId);

        for(int i=0;i<count;i++){
            retList.add(cursorValue+i);
        }

        return retList.toArray(new Long[count]);
    }

}
