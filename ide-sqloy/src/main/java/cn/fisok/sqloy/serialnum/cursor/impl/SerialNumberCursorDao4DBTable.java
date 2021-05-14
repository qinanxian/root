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
package cn.fisok.sqloy.serialnum.cursor.impl;

import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.serialnum.consts.CursorRecordType;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.sqloy.serialnum.cursor.SerialNumberCursorDao;
import cn.fisok.sqloy.serialnum.model.SerialNumberCursor;
import cn.fisok.raw.kit.MapKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 使用数据表记录最大流水号
 */
@Primary
@Component(CursorRecordType.DB_TABLE)
public class SerialNumberCursorDao4DBTable implements SerialNumberCursorDao {

    @Autowired
    BeanCruder beanAccessor;

    public SerialNumberCursor findOne(String generatorId) {
        return beanAccessor.selectOneById(SerialNumberCursor.class, generatorId);
    }

    public void insertOne(SerialNumberCursor cursor) {
        beanAccessor.insert(cursor);
    }

    public void increase(String generatorId, int count) {
        String sql = "update FOWK_SERIAL_NUMBER set CURSOR_VALUE=CURSOR_VALUE+:count,UPDATED_TIME=:updatedTime where ID=:id";
        beanAccessor.execute(sql, MapKit.mapOf("count", count, "id", generatorId,"updatedTime", DateKit.now()));
    }

    public void lock(String generatorId) {
        String sql = "update FOWK_SERIAL_NUMBER set UPDATED_TIME=UPDATED_TIME where ID=:id";
        beanAccessor.execute(sql, MapKit.mapOf("id", generatorId,"updatedTime", DateKit.now()));
    }

    public void unlock(String generatorId) {

    }
}
