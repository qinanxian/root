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
package cn.fisok.sqloy.dialect;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 处理不同的数据库在分页以及数据库差异化的分另处理
 */
public interface SqlDialect {
    /**
     * 处理成不同的分页查询SQL
     *
     * @param sql sql
     * @param index index
     * @param size size
     * @return String
     */
    public String getPaginationSql(String sql, int index, int size);

    /**
     * 处理查询总数量SQL
     *
     * @param sql sql
     * @return String
     */
    public String getCountSql(String sql);
}
