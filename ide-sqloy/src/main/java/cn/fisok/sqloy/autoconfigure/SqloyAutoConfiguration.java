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
package cn.fisok.sqloy.autoconfigure;

import cn.fisok.raw.kit.*;
import cn.fisok.sqloy.core.*;
import cn.fisok.sqloy.datasource.DruidDynamicDatasourceKit;
import cn.fisok.sqloy.datasource.DynamicRoutingDataSource;
import cn.fisok.sqloy.serialnum.finder.SerialNumberGeneratorFinder;
import cn.fisok.sqloy.loadmd.SQLTextLoader;
import cn.fisok.sqloy.loadmd.impl.SQLTextLoaderImpl;
import cn.fisok.sqloy.serialnum.finder.impl.SerialNumberGeneratorFinderImpl;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author : ??????<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : fisok-jdbc??????????????????
 */
@EnableConfigurationProperties(SqloyProperties.class)
@ComponentScan(basePackages = "cn.fisok.sqloy")
public class SqloyAutoConfiguration {

    private SqloyProperties properties;

    public SqloyAutoConfiguration(SqloyProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init(){
        LogKit.info("==========>FISOK-SQLOY??????????????????<==========");
    }
    /**
     * ?????????????????????
     *
     * @return
     */
    @Primary
    @Order(1)
    @Bean(name = "datasource")
    public DataSource getDynamicDataSource() {
        Map<String, Object> dsProperties = properties.getDatasource();

        //???????????????????????????????????????????????????
        Map<Object, DataSource> dssMap = DruidDynamicDatasourceKit.buildDatasources(dsProperties);
        Map<Object,Object> vdsMap = new LinkedHashMap<>();
        vdsMap.putAll(dssMap);
        DataSource dsMaster = dssMap.get(SqlConsts.DS_MASTER);
        ValidateKit.notNull(dsMaster,"??????????????????????????????master????????????");

        //????????????????????????
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setDefaultTargetDataSource(dsMaster);
        dataSource.setTargetDataSources(vdsMap);

        return dataSource;
    }


    @Order(9)
    @Bean
    @ConfigurationProperties(prefix = "mybatis")
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true); //??????????????????

        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfiguration(configuration);

        return sqlSessionFactoryBean;
    }

    /**
     * ??????
     *
     * @return
     */
    /*@Order(10)
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }*/

    @Bean
    public SerialNumberGeneratorFinder getSerialNumberGeneratorFinder(){
        return new SerialNumberGeneratorFinderImpl();
    }

    @Bean("beanQuery")
    @ConditionalOnClass(NamedParameterJdbcTemplate.class)
    @ConditionalOnMissingBean(BeanQuery.class)
    public BeanQuery getBeanQuery(NamedParameterJdbcTemplate jdbcTemplate) {
        BeanQuery query = new BeanQuery();

        query.setJdbcTemplate(jdbcTemplate);
//        query.setSqlDialectType(properties.getSqlDialectType());

        return query;
    }

    @Bean("beanUpdater")
    @ConditionalOnClass({BeanQuery.class, SerialNumberGeneratorFinder.class})
    @ConditionalOnMissingBean(BeanUpdater.class)
    public BeanUpdater getBeanUpdater(BeanQuery dataQuery, SerialNumberGeneratorFinder finder) {
        BeanUpdater updater = new BeanUpdater();
        updater.setJdbcTemplate(dataQuery.getJdbcTemplate());
        updater.setBeanQuery(dataQuery);
        updater.setSerialNumberGeneratorFinder(finder);
//        updater.setSqlDialectType(properties.getSqlDialectType());
        return updater;
    }

    @Bean("beanCruder")
    @ConditionalOnClass({BeanQuery.class, BeanUpdater.class})
    @ConditionalOnMissingBean(BeanCruder.class)
    public BeanCruder getBeanCruder(BeanQuery beanQuery, BeanUpdater beanUpdater) {
        BeanCruder beanCruder = new BeanCruder(beanQuery, beanUpdater);
        return beanCruder;
    }


    @Bean("mapObjectQuery")
    @ConditionalOnClass(NamedParameterJdbcTemplate.class)
    @ConditionalOnMissingBean(MapObjectQuery.class)
    public MapObjectQuery getMapObjectQuery(NamedParameterJdbcTemplate jdbcTemplate) {
        MapObjectQuery query = new MapObjectQuery();

        query.setJdbcTemplate(jdbcTemplate);
//        query.setSqlDialectType(properties.getSqlDialectType());

        return query;
    }

    @Bean("mapObjectUpdater")
    @ConditionalOnClass(NamedParameterJdbcTemplate.class)
    @ConditionalOnMissingBean(MapObjectUpdater.class)
    public MapObjectUpdater getMapObjectUpdater(NamedParameterJdbcTemplate jdbcTemplate, MapObjectQuery mapObjectQuery) {
        MapObjectUpdater updater = new MapObjectUpdater();

        updater.setJdbcTemplate(jdbcTemplate);
        updater.setMapObjectQuery(mapObjectQuery);
//        updater.setSqlDialectType(properties.getSqlDialectType());

        return updater;
    }

    @Bean("mapObjectCruder")
    @ConditionalOnClass({MapObjectQuery.class, MapObjectUpdater.class})
    @ConditionalOnMissingBean(MapObjectCruder.class)
    public MapObjectCruder getMapObjectCruder(MapObjectQuery mapObjectQuery, MapObjectUpdater mapObjectUpdater) {
        MapObjectCruder mapObjectCruder = new MapObjectCruder(mapObjectQuery, mapObjectUpdater);
        return mapObjectCruder;
    }

    @Bean("sqlFileLoader")
    public SQLTextLoader getSQLFileLoader() {
        SQLTextLoader loader = new SQLTextLoaderImpl();
        return loader;
    }

    @Bean
    public ServletRegistrationBean druidStatViewServlet() {
        //????????????
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
//        // ?????????(????????????,????????????????????????,??????IP????????????????????????)
//        servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
//        // IP????????? (??????????????????deny?????????allow) ???????????????????????????????????????????????????ip?????????????????????????????????
//        servletRegistrationBean.addInitParameter("deny", "127.0.0.2");

        // ?????????????????????????????????
        if(StringKit.isNotBlank(properties.getDruidLoginUser())){
            servletRegistrationBean.addInitParameter("loginUsername", properties.getDruidLoginUser());
            servletRegistrationBean.addInitParameter("loginPassword", properties.getDruidLoginPass());
        }
        // ????????????????????????.
        if(StringKit.isNotBlank(properties.getDruidResetEnable())){
            servletRegistrationBean.addInitParameter("resetEnable", properties.getDruidResetEnable());
        }
        return servletRegistrationBean;
    }

    @Bean
    public SnowFlake getSnowFlake(){
        SnowFlake snowFlake = new SnowFlake(properties.getSnowFlakeDatacenterId(), NetKit.getMachineNum());
        return snowFlake;
    }


    @Bean
    public FilterRegistrationBean druidStatFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        // ??????????????????
        filterRegistrationBean.addUrlPatterns("/*");
        // ????????????????????????????????????
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        LogKit.debug("druid-servlet ????????????!");
        return filterRegistrationBean;

    }

}
