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
package cn.fisok.sqloy.serialnum.finder;


import cn.fisok.sqloy.serialnum.generator.SerialNumberGenerator;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 流水号生成器实现类查找接口
 */
public interface SerialNumberGeneratorFinder {
    /**
     * 查找流水号生成器
     * @param generatorId 一般是类名.字段名
     * @return
     */
    SerialNumberGenerator find(String generatorId);
}
