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
import cn.fisok.sqloy.annotation.*;
import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.core.MapObjectCruder;
import cn.fisok.sqloy.exception.SQLDaoException;
import cn.fisok.sqloy.exception.SqloyException;
import cn.fisok.sqloy.loadmd.SQLCollecter;
import cn.fisok.sqloy.loadmd.impl.SQLTextLoaderImpl;
import cn.fisok.raw.holder.ApplicationContextHolder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.core.ResolvableType;
import org.springframework.data.repository.query.Param;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : SQLDao 自动扫描处理类，在所有的类定义注册完成之后执行
 */
public class SQLDaoFactoryBean implements FactoryBean {

    private Class<?> clazz;

    public Object getObject() throws Exception {
        SQLAccessorProxy proxy = new SQLAccessorProxy(clazz);
        Object impl = proxy.getProxy();
        return impl;
    }

    public Class<?> getObjectType() {
        return clazz;
    }

    public boolean isSingleton() {
        return true;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public static class SQLAccessorProxy implements MethodInterceptor {
        private Class<?> interfaceClazz;
        private Enhancer enhancer = new Enhancer();
        private BeanCruder beanCruder;
        private MapObjectCruder mapObjectCruder;

        public SQLAccessorProxy(Class<?> interfaceClazz) {
            this.interfaceClazz = interfaceClazz;
        }

        public Object getProxy(){
            enhancer.setSuperclass(interfaceClazz); //设置需要创建子类的类
            enhancer.setCallback(this);
            return enhancer.create();               //通过字节码技术动态创建子类实例
        }

        //实现MethodInterceptor接口方法
        public Object intercept(Object object, Method method, Object[] args,
                                MethodProxy proxy) throws Throwable {
            String methodName = method.getName();
            switch (methodName){
                case "hashCode":return proxy.invokeSuper(object, args);//通过代理类调用父类中的方法
                case "toString":return proxy.invokeSuper(object, args);
            }

//            System.out.println("调用方法："+object.getClass().getName()+"->"+method.getName());
            try{
                return exec(object,method,args);
            }catch (Exception e){
//                Object realInst = BeanKit.getProxyTarget(object);
                String message = StringKit.format("执行方法{0}.{1}出错,出错消息:{2}",object.getClass().getName(),methodName,e.getMessage());
                LogKit.error(message,e);
                throw new SqloyException(message,e.getCause());
            }
        }

        /**
         * 执行SQLDao注解的类
         * @param object SQLDao执行类
         * @param method 执行方法
         * @param args 传入的参数
         * @return
         */
        public Object exec(Object object, Method method, Object[] args){
            Insert annoInsert = method.getAnnotation(Insert.class);
            Update annoUpdate = method.getAnnotation(Update.class);
            Save annoSave = method.getAnnotation(Save.class);
            Delete annoDelete = method.getAnnotation(Delete.class);
            Select annoSelect = method.getAnnotation(Select.class);

            String mapFile = getSqlMapFile();

            //根据注解，选择执行的SQL
            if(annoInsert != null){                             //INSERT 处理
                String sqlName = StringKit.nvl(annoInsert.name(),method.getName());
                String sql = getSqlTextFromFile(method,mapFile,sqlName);
                String sqlText = StringKit.nvl(sql,annoInsert.value());
                return execInsert(object,method,args,sqlText);
            }else if(annoUpdate != null){                        //UPDATE 处理
                String sqlName = StringKit.nvl(annoUpdate.name(),method.getName());
                String sql = getSqlTextFromFile(method,mapFile,sqlName);
                String sqlText = StringKit.nvl(sql,annoUpdate.value());
                return execUpdate(object,method,args,sqlText);
            }else if(annoSave != null){                          //SAVE 处理
                String sqlName = StringKit.nvl(annoSave.name(),method.getName());
                String sql = getSqlTextFromFile(method,mapFile,sqlName);
                String sqlText = StringKit.nvl(sql,annoSave.value());
                return execSave(object,method,args,sqlText);
            }else if(annoDelete != null){                        //DELETE 处理
                String sqlName = StringKit.nvl(annoDelete.name(),method.getName());
                String sql = getSqlTextFromFile(method,mapFile,sqlName);
                String sqlText = StringKit.nvl(sql,annoDelete.value());
                return execDelete(object,method,args,sqlText);
            }else if(annoSelect != null){                        //SELECT 处理
                String sqlName = StringKit.nvl(annoSelect.name(),method.getName());
                String sql = getSqlTextFromFile(method,mapFile,sqlName);
                String sqlText = StringKit.nvl(sql,annoSelect.value());
                return execSelect(object,method,args,sqlText);
            }else{
                String sqlText = getSqlTextFromFile(method,mapFile,method.getName());
                if(StringKit.isBlank(sqlText)){
                    String className = Optional
                            .ofNullable(object)
                            .map(obj->obj.getClass().getName())
                            .orElseGet(()->"");

                    String methodName = Optional
                            .ofNullable(method)
                            .map(m->m.getName())
                            .orElseGet(()->"");

                    throw new SQLDaoException("{0}.{1}() 方法没有找到对应的SQL",className,methodName);
                }
                return execSelect(object,method,args,sqlText);
            }


        }

        /**
         * 把参数中的方法通过解析到Map中来
         * @param method
         * @param args
         * @return
         */
        protected Map<String,Object> parseArgToMap(Method method, Object[] args){
            Map<String,Object> queryParam = new HashMap<>();
            //根据方法的参数名，取方法的参数名，这种做法只有1.8才支持
            Parameter[] parameters = method.getParameters();
//            for(int i=0;i<parameters.length;i++){
//                queryParam.put(parameters[i].getName(),args[i]);
//            }

//            根据注解，组装查询参数
//            Class<?>[] parameterTypes = method.getParameterTypes();
//            for(int i=0;i<parameterTypes.length;i++){
//                Class<?> ptClazz = parameterTypes[i];
//                SqlParam sqlParam = ptClazz.getAnnotation(SqlParam.class);
//                if(sqlParam==null||StringKit.isBlank(sqlParam.value())){
//                    continue;
//                }
//                queryParam.put(sqlParam.value(),args[i]);
//            }

            //根据注解，解析参数以及对应的值
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                //之前老的写法
//                Annotation[] annotations = parameter.getDeclaredAnnotations();
                Param paramObject = parameter.getAnnotation(Param.class);
                if(paramObject != null && StringKit.isNotBlank(paramObject.value())){
                    queryParam.put(paramObject.value(),args[i]);
                }

                //新的注解写法
                SqlParam sqlParam = parameter.getAnnotation(SqlParam.class);
                if(sqlParam != null && !StringKit.isBlank(sqlParam.value())){
                    queryParam.put(sqlParam.value(),args[i]);
                }else{
                    //Java1.8的写法
                    queryParam.put(parameters[i].getName(),args[i]);
                }
            }

            return queryParam;
        }

        protected Object execSelect(Object object, Method method, Object[] args,String sqlText){
            ValidateKit.isTrue(SQLKit.isSelect(sqlText),"{0}不是一个查询语句",sqlText);

            Class<?> dataClazz = getReturnDataClass(method);

            Map<String,Object> queryParam = parseArgToMap(method,args);

            //如果不是Map，那么就是JavaBean
            if(!dataClazz.isAssignableFrom(Map.class)){
                boolean isReturnListData = isReturnListData(method);
                if(isReturnListData){
                    return selectList(dataClazz,sqlText,queryParam);
                }else{
                    return selectOne(dataClazz,sqlText,queryParam);
                }
            }
            return null;
        }

        protected Object execInsert(Object object, Method method, Object[] args,String sqlText){
            if(StringKit.isBlank(sqlText)){ //如果没有SQL，则是插入一个JavaBean
                Object bean = args[0];
                return getBeanCruder().insert(bean);
            }else{                          //如果有SQL，则执行SQL
                ValidateKit.isTrue(SQLKit.isInsert(sqlText),"{0}不是一个插入语句",sqlText);
                Map<String,Object> dataOne = parseArgToMap(method,args);
                return getBeanCruder().execute(sqlText,dataOne);
            }
        }
        protected Object execUpdate(Object object, Method method, Object[] args,String sqlText){
            if(StringKit.isBlank(sqlText)){ //如果没有SQL，则是插入一个JavaBean
                Object bean = args[0];
                return getBeanCruder().update(bean);
            }else{                          //如果有SQL，则执行SQL
                ValidateKit.isTrue(SQLKit.isUpdate(sqlText),"{0}不是一个更新语句",sqlText);
                Map<String,Object> dataOne = parseArgToMap(method,args);
                return getBeanCruder().execute(sqlText,dataOne);
            }
        }
        protected Object execSave(Object object, Method method, Object[] args,String sqlText){
            if(StringKit.isBlank(sqlText)){ //如果没有SQL，则是插入一个JavaBean
                Object bean = args[0];
                if(bean instanceof List){
                    return getBeanCruder().save((List)bean);
                }else{
                    return getBeanCruder().save(bean);
                }
            }else{                          //如果有SQL，则执行SQL
                if(SQLKit.isInsert(sqlText) && SQLKit.isUpdate(sqlText)){
                    throw new SqloyException("{0}不是一个修改语句",sqlText);
                }
                Map<String,Object> dataOne = parseArgToMap(method,args);
                return getBeanCruder().execute(sqlText,dataOne);
            }
        }
        protected Object execDelete(Object object, Method method, Object[] args,String sqlText){
            if(StringKit.isBlank(sqlText)){ //如果没有SQL，则是插入一个JavaBean
                Object bean = args[0];
                return getBeanCruder().delete(bean);
            }else{                          //如果有SQL，则执行SQL
                ValidateKit.isTrue(SQLKit.isDelete(sqlText),"{0}不是一个删除语句",sqlText);
                Map<String,Object> dataOne = parseArgToMap(method,args);
                return getBeanCruder().execute(sqlText,dataOne);
            }
        }


        public BeanCruder getBeanCruder() {
            if(beanCruder == null) beanCruder = ApplicationContextHolder.getBean(BeanCruder.class);
            return beanCruder;
        }



        public MapObjectCruder getMapObjectCruder() {
            if(mapObjectCruder == null) mapObjectCruder = ApplicationContextHolder.getBean(MapObjectCruder.class);
            return mapObjectCruder;
        }



        private Object selectOne(Class<?> clazz, String sql,Map<String,Object> queryParam){
            return getBeanCruder().selectOne(clazz,sql,queryParam);
        }
        private List<?> selectList(Class<?> clazz,String sql,Map<String,Object> queryParam){
            return getBeanCruder().selectList(clazz,sql,queryParam);
        }

        /**
         * 获取返回数据的数据类型
         *
         * @param method method
         * @return class
         */
        private Class<?> getReturnDataClass(Method method){
            Class<?> clazz = method.getReturnType();
            //如果是List，则需要取List<T>中T的类型
            if(isReturnListData(method)){
                clazz = ResolvableType.forMethodReturnType(method).getGeneric(0).getRawClass();
            }
            return clazz;
        }

        private boolean isReturnListData(Method method){
            Class<?> clazz = method.getReturnType();
            return clazz.isAssignableFrom(List.class);
        }

        /**
         * 映射文件规则
         * 1. 如果注解SQLDao配置了映射文件，直接取
         * 2. 如果没有配置SQLDao,则根据接口的classpath取同名文件+.sql.md
         *
         * @return String
         */
        private String getSqlMapFile(){
            String mapFile = StringKit.format("classpath:{0}.sql.md",interfaceClazz.getName().replaceAll("\\.","/"));
            SQLDao anno = interfaceClazz.getAnnotation(SQLDao.class);
            if(StringKit.isNoneBlank(anno.value())){
                mapFile = anno.value();
            }
            return mapFile;
        }

        /**
         * SQL计算规则优先级
         * 1. 文件.md.sql优先级最高
         * 2. SQL内容注解
         * 3. 根据API名称自动计算
         * TODO 优先从文件中取语句,如果生产模式，则考虑加载缓存，防止多次读取文件
         * @param method  method
         * @param mapFile mapFile
         * @return String
         */
        private String getSqlTextFromFile(Method method,String mapFile,String sqlName){
            SQLTextLoaderImpl loader = new SQLTextLoaderImpl();
            SQLCollecter sqlCollecter = loader.parse(mapFile);

            //取查询名称，如果注解绑定的查询映射，则使用注解绑定的，如果没有，则使用方法名
            NamedMapping nameAnno = method.getAnnotation(NamedMapping.class);
            if(nameAnno != null && StringKit.isNotBlank(nameAnno.value())){
                sqlName = nameAnno.value();
            }
            String sqlText = sqlCollecter.sql(sqlName);

            return sqlText;
        }

        /**
         * 检查文件是否存在
         * @param mapFile
         * @return
         */
        private boolean sqlFileExists(String mapFile){
            SQLTextLoaderImpl loader = new SQLTextLoaderImpl();
            return loader.exists(mapFile);
        }

        private String calcSelectSqlByMethodName(Method method,String table){
            return null;
        }
    }

}
