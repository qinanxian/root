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
import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.exception.SqloyException;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcUtils;

import javax.sql.DataSource;
import java.io.StringBufferInputStream;
import java.sql.SQLException;
import java.util.*;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/1/8
 * @desc : Druid多数据源支持
 */
public abstract class DruidDynamicDatasourceKit {
    /**
     * 使用数据源配置完成数据源列表构建
     * @param datasourcesProperties
     * @return
     */
    public static Map<Object, DataSource> buildDatasources(Map<String,Object> datasourcesProperties) {
        Map<Object, DataSource> datasourceMap = new LinkedHashMap<>();

        Iterator<String> dsKeyIterator = datasourcesProperties.keySet().iterator();
        while(dsKeyIterator.hasNext()){
            String dsName = dsKeyIterator.next();
            Map<String,Object> jdbcProperties = (Map<String,Object>)datasourcesProperties.get(dsName);

            DruidDataSource dataSource = buildDatasource(dsName, MapObject.build(jdbcProperties));
//            String dbType = dataSource.getDbType();
            try {
                dataSource.init();
            } catch (SQLException e) {
                LogKit.error("初始化数据源"+dsName+"出错!",e);
                throw new SqloyException("初始化数据源"+dsName+"出错!",e);
            }
            datasourceMap.put(dsName,dataSource);
        }

        return datasourceMap;
    }

    public static DruidDataSource buildDatasource(String name, MapObject jdbcProperties){
        DruidDataSource dataSource = new DruidDataSource();

        //参考：https://blog.csdn.net/wangmx1993328/article/details/81865153

        dataSource.setName(name);
        dataSource.setDriverClassName(jdbcProperties.getValue("driver-class-name").strValue());
        dataSource.setUrl(jdbcProperties.getValue("url").strValue());
        dataSource.setDbType(JdbcUtils.getDbType(dataSource.getRawJdbcUrl(),dataSource.getDriverClassName()));
        dataSource.setUsername(jdbcProperties.getValue("username").strValue());
        dataSource.setPassword(jdbcProperties.getValue("password").strValue());
        dataSource.setInitialSize(jdbcProperties.getValue("initial-size").intValue(0));
        dataSource.setMaxActive(jdbcProperties.getValue("max-active").intValue(8));
        dataSource.setMaxIdle(jdbcProperties.getValue("max-idle").intValue(8));
        dataSource.setMinIdle(jdbcProperties.getValue("min-idle").intValue(1));
        dataSource.setMinIdle(jdbcProperties.getValue("min-idle").intValue(1));
        dataSource.setMaxWait(jdbcProperties.getValue("max-wait").intValue(60000));
        dataSource.setPoolPreparedStatements(jdbcProperties.getValue("pool-prepared-statements").boolValue(false));
        dataSource.setMaxOpenPreparedStatements(jdbcProperties.getValue("max-open-prepared-statements").intValue(-1));
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(jdbcProperties.getValue("max-pool-prepared-statement-per-connection-size").intValue(20));
        dataSource.setValidationQuery(jdbcProperties.getValue("validation-query").strValue());
        dataSource.setTestOnBorrow(jdbcProperties.getValue("test-on-borrow").boolValue(false));
        dataSource.setTestWhileIdle(jdbcProperties.getValue("test-while-idle").boolValue(false));
        dataSource.setTestOnReturn(jdbcProperties.getValue("test-on-return").boolValue(false));
        dataSource.setTimeBetweenEvictionRunsMillis(jdbcProperties.getValue("time-between-eviction-runs-millis").longValue());
        //对于建立时间超过removeAbandonedTimeout的连接强制关闭
        dataSource.setRemoveAbandoned(jdbcProperties.getValue("remove-abandoned").boolValue(true));                     //打开removeAbandoned功能
        dataSource.setRemoveAbandonedTimeout(jdbcProperties.getValue("remove-abandoned-timeout").intValue(180*1000));   //设置直接强制关闭超时时间
        dataSource.setLogAbandoned(jdbcProperties.getValue("log-abandoned").boolValue(false));                          //关闭abanded连接时输出错误日志

        try {
            String sFilters = jdbcProperties.getValue("filters").strValue();
            if (sFilters != null && !"".equals(sFilters)) {
                dataSource.setFilters(sFilters);
            }
            String sConnectionProperties = jdbcProperties.getValue("connection-properties").strValue();
            if (sConnectionProperties != null && !"".equals(sConnectionProperties)) {
                Properties properties = new Properties();
                String[] sProperties = sConnectionProperties.split(";");
                for (int i=0;i<sProperties.length;i++) {
                    properties.load(new StringBufferInputStream(sProperties[i].trim()));
                }
                dataSource.setConnectProperties(properties);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataSource;
    }
}
