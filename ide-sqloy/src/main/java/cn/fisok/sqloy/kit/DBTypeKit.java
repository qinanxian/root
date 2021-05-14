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
package cn.fisok.sqloy.kit;

import cn.fisok.sqloy.core.DBType;
import cn.fisok.sqloy.exception.SqloyException;
import cn.fisok.raw.kit.IOKit;
import cn.fisok.raw.kit.ValidateKit;
import com.alibaba.druid.util.JdbcUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/5/11
 * @desc :
 */
public abstract class DBTypeKit {

    public static DBType getDBType(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        JdbcTemplate jdbcTemplate = namedParameterJdbcTemplate.getJdbcTemplate();
        return getDBType(jdbcTemplate);
    }

    public static DBType getDBType(JdbcTemplate jdbcTemplate){
        DataSource dataSource = jdbcTemplate.getDataSource();
        String dbType = getDBTypeStr(dataSource);
        ValidateKit.notBlank(dbType,"从dataSource中获取的dbType为空");
        return DBType.valueOf(dbType.toUpperCase());
    }



    public static String getDBTypeStr(Connection connection){
        try {
            DatabaseMetaData dbMeta = connection.getMetaData();
            String driver = dbMeta.getDriverName();
            String url = dbMeta.getURL();
            String dbType = JdbcUtils.getDbType(url,driver);
            return dbType;
        } catch (SQLException e) {
            throw new SqloyException("从connection中获取数据库类型MetaData出错",e);
        }
    }

    public static String getDBTypeStr(DataSource dataSource){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            return getDBTypeStr(connection);
        } catch (SQLException e) {
            IOKit.close(connection);
            throw new SqloyException("从datasource中获取数据库类型DBType出错",e);
        } finally {
            IOKit.close(connection);
        }
    }
}
