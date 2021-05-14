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

import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/1/8
 * @desc :
 */
@Component
public class DataSourceSwitcherRunner {
    @Autowired
    protected BeanCruder beanAccessor;

    @UseDataSource("slave")
    public String getDatasourceSlave(){
        String database = beanAccessor.selectOne(String.class,"select database()");
        return database;
    }
    public String getDatasourceDefault(){
        String database = beanAccessor.selectOne(String.class,"select database()");
        return database;
    }
    @UseDataSource("master")
    public String getDatasourceMaster(){
        String database = beanAccessor.selectOne(String.class,"select database()");
        return database;
    }
}
