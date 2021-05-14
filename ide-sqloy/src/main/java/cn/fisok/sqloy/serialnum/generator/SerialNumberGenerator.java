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
package cn.fisok.sqloy.serialnum.generator;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 流水号格式化处理器
 */
public interface SerialNumberGenerator<T> {
    /**
     * 生成流水号
     * @param generatorId 一般是类名.属性名.附加日期等
     * @return
     */
    T next(String generatorId);

    /**
     * 生成流水号
     * @param generatorId 一般是类名.属性名.附加日期等
     * @param object 业务对象
     * @return
     */
    T next(String generatorId, Object object);

    /**
     * 生成一批流水号
     * @param generatorId 一般是类名.属性名.附加日期等
     * @param count 数量
     * @return
     */
    T[] nextBatch(String generatorId, int count);

    /**
     * 生成一批流水号
     * @param generatorId 一般是类名.属性名.附加日期等
     * @param count 数量
     * @param object 参考业务对象
     * @return
     */
    T[] nextBatch(String generatorId, int count,Object object);

}
