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
package cn.fisok.raw.kit;
import org.apache.commons.lang3.ArrayUtils;


/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 22:37
 * @desc :
 */
public abstract class ArrayKit {
    public static <T> T[] concat(final T[] array1, final T... array2) {
        return ArrayUtils.addAll(array1,array2);
    }
}