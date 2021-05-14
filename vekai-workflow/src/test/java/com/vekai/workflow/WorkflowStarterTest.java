package com.vekai.workflow;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

public class WorkflowStarterTest extends BaseTest {
    @Autowired
    protected DataSource dataSource;
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    protected NamedParameterJdbcTemplate parameterJdbcTemplate;
    @Autowired
    protected SqlSessionFactory sqlSessionFactory;
    @Autowired
    protected SqlSessionTemplate sqlSessionTemplate;

    @Test
    public void runtimeTest(){
        Assert.assertNotNull(dataSource);
        Assert.assertNotNull(jdbcTemplate);
        Assert.assertNotNull(parameterJdbcTemplate);
        Assert.assertNotNull(sqlSessionFactory);
        Assert.assertNotNull(sqlSessionTemplate);

    }
}
