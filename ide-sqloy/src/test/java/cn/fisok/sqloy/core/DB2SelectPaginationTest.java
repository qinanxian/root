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
package cn.fisok.sqloy.core;

import cn.fisok.sqloy.BaseTest;
import cn.fisok.raw.kit.JSONKit;
import cn.fisok.raw.kit.MapKit;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/5/11
 * @desc :
 */
public class DB2SelectPaginationTest extends BaseTest {
    @Autowired
    protected MapObjectQuery dataQuery;

    @Test
    public void selectPaginationListTest(){
        Map<String,?> paramMap = MapKit.mapOf("code","0");
        PaginResult result = dataQuery.selectListPagination("select * from XDGC_LED_MANAGER where A6 != :code",paramMap,2,15);
        System.out.println(JSONKit.toJsonString(result,true));
    }
}
