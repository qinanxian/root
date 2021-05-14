package com.vekai.crops.etl.config;

import com.vekai.crops.etl.jdbc.JDBCConnector;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Value("${ods.datasource.driver-class-name}")
    public String odsClzName;
    @Value("${ods.datasource.url}")
    public String odsUrl;
    @Value("${ods.datasource.username}")
    public String odsUsername;
    @Value("${ods.datasource.password}")
    public String odsPassword;

    @Value("${erdm.datasource.driver-class-name}")
    public String erdmClzName;
    @Value("${erdm.datasource.url}")
    public String erdmUrl;
    @Value("${erdm.datasource.username}")
    public String erdmUsername;
    @Value("${erdm.datasource.password}")
    public String erdmPassword;


    @Bean(name = "odsJDBCConnector")
    public JDBCConnector odsJDBCConnector() {
        JDBCConnector jdbcConnector = new JDBCConnector(odsClzName, odsUrl, odsUsername, odsPassword);
        return jdbcConnector;
    }


    @Bean(name = "erdmJDBCConnector")
    public JDBCConnector erdmJDBCConnector() {
        JDBCConnector jdbcConnector = new JDBCConnector(erdmClzName, erdmUrl, erdmUsername, erdmPassword);
        return jdbcConnector;
    }
}