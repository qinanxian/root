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

import cn.fisok.sqloy.serialnum.consts.CursorRecordType;
import cn.fisok.sqloy.serialnum.cursor.SerialNumberCursorDao;
import cn.fisok.sqloy.serialnum.model.SerialNumberCursor;
import org.springframework.stereotype.Component;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 使用Redis记录最大流水号
 */
@Component(CursorRecordType.REDIS)
public class SerialNumberCursorDao4Redis implements SerialNumberCursorDao {


    public SerialNumberCursor findOne(String generatorId) {
        return null;
    }

    public void insertOne(SerialNumberCursor serialNumberCursor) {

    }

    public void increase(String generatorId, int count) {

    }

    public void lock(String generatorId) {

    }

    public void unlock(String generatorId) {

    }
}
