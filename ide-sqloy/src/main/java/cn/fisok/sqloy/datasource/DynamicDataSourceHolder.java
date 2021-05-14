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
package cn.fisok.sqloy.datasource;

import cn.fisok.sqloy.core.SqlConsts;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/1/5
 * @desc : 动态数据源保持器
 */
public abstract class DynamicDataSourceHolder {
    private static ThreadLocal<String> CONTEXT_HOLDER = ThreadLocal.withInitial(() -> SqlConsts.DS_MASTER);

    public static void setDataSourceKey(String key){
        CONTEXT_HOLDER.set(key);
    }

    public static String getDataSourceKey(){
        return CONTEXT_HOLDER.get();
    }

    public static void clear(){
        CONTEXT_HOLDER.remove();
    }


}
