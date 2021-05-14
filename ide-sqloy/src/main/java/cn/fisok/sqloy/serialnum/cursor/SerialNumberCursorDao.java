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
package cn.fisok.sqloy.serialnum.cursor;

import cn.fisok.sqloy.serialnum.model.SerialNumberCursor;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 流水号记录数据读写器(有序的，需要持久化记录的）
 */
public interface SerialNumberCursorDao {

    SerialNumberCursor findOne(String generatorId);

    void insertOne(SerialNumberCursor serialNumberCursor);

    void increase(String generatorId,int count);

    void lock(String generatorId);

    void unlock(String generatorId);
}
