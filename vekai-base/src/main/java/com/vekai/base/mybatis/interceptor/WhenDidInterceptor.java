package com.vekai.base.mybatis.interceptor;

import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.DateKit;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Created by tisir yangsong158@qq.com on 2017-05-07
 * mybatis日期处理拦截
 */
//method只定义了update，query，flushStatements，commit，rollback，createCacheKey，isCached，clearLocalCache，deferLoad，getTransaction，close，isClosed这几个方法
@Intercepts({
        @Signature(method = "query", type = Executor.class, args = { MappedStatement.class, Object.class,RowBounds.class, ResultHandler.class }),
        @Signature(method = "update",type = Executor.class,  args = {MappedStatement.class, Object.class }),
//        @Signature(method = "prepare", type = StatementHandler.class, args = { Connection.class })
})
public class WhenDidInterceptor implements Interceptor {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private Properties properties = null;

    @Autowired


    public Properties getProperties() {
		return properties;
	}

	public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object beanObject = args[1];

        String sqlId = mappedStatement.getId();
        logger.trace("mybatis-sqlId:"+sqlId);
        Object target = invocation.getTarget();

        Configuration configuration = mappedStatement.getConfiguration();
        StatementHandler handler = configuration.newStatementHandler((Executor) target, mappedStatement,beanObject, RowBounds.DEFAULT, null, null);
        String methodName = invocation.getMethod().getName();

        BoundSql boundSql = handler.getBoundSql();
        String sql = boundSql.getSql();


        String createTime = "createTime";
        String updatedTime = "updatedTime";

        if("update".equals(methodName)){
            if(isInsert(sql)){
                if(BeanKit.propertyExists(beanObject,createTime)
                        &&containCreatedTime(sql)){
                    BeanKit.setProperty(beanObject, createTime, DateKit.now());
                }
                if(BeanKit.propertyExists(beanObject,updatedTime)
                        &&containUpdatedTime(sql)){
                    BeanKit.setProperty(beanObject, updatedTime, DateKit.now());
                }
            }else if(isUpdate(sql)){
                if(BeanKit.propertyExists(beanObject,updatedTime)
                        &&containUpdatedTime(sql)){
                    BeanKit.setProperty(beanObject, updatedTime, DateKit.now());
                }
            }
        }

        return invocation.proceed();
    }


    Pattern insertPattern = Pattern.compile("\\s*insert",Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
    Pattern updatePattern = Pattern.compile("\\s*update",Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
    Pattern createdTimePattern = Pattern.compile("\\bCREATED_TIME\\b",Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
    Pattern updatedTimePattern = Pattern.compile("\\bUPDATED_TIME\\b",Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
    protected boolean isInsert(String sql){
        return insertPattern.matcher(sql).find();
    }
    protected boolean isUpdate(String sql){
        return updatePattern.matcher(sql).find();
    }
    protected boolean containCreatedTime(String sql){
        return createdTimePattern.matcher(sql).find();
    }
    protected boolean containUpdatedTime(String sql){
        return updatedTimePattern.matcher(sql).find();
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
