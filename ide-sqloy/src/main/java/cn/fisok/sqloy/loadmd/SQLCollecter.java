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
package cn.fisok.sqloy.loadmd;

import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/05
 * @desc : SQLDao收集器，把SQL聚集到一起
 */
public class SQLCollecter {
    private Map<String,String> sqlTextMap ;
    public SQLCollecter(Map<String,String> sqlTextMap){
        this.sqlTextMap = sqlTextMap;
    }
    public String sql(String key){
        return sqlTextMap.get(key);
    }
}
