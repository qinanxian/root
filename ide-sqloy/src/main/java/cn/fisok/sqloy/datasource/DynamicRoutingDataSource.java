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

import cn.fisok.raw.kit.LogKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.sqloy.core.SqlConsts;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/1/5
 * @desc : 该类继承自 AbstractRoutingDataSource 类，
 * 在访问数据库时会调用该类的 determineCurrentLookupKey() 方法获取数据库实例的 key
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    protected Object determineCurrentLookupKey() {
        String dataSourceKey = DynamicDataSourceHolder.getDataSourceKey();
        //如果没有使用主数据源，打个日志
        if(!StringKit.equals(dataSourceKey, SqlConsts.DS_MASTER)){
            LogKit.trace("current datasource is : {0}",dataSourceKey);
        }
        return dataSourceKey;
    }

}
