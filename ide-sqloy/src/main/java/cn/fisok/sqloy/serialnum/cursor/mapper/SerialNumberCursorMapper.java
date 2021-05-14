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
package cn.fisok.sqloy.serialnum.cursor.mapper;

import cn.fisok.sqloy.serialnum.model.SerialNumberCursor;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : Mybatis的Mapper
 */
public interface SerialNumberCursorMapper {

    SerialNumberCursor findOne(String id);

    void insertOne(SerialNumberCursor serialNumberCursor);

    void increase(String generatorId);

    void increaseBatch(String generatorId,int count);

}
