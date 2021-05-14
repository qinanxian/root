package com.vekai.dataform.autoconfigure;

import com.vekai.dataform.DataFormConsts;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.handler.impl.BeanDataOneHandler;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.mapper.DataFormMapper;
import com.vekai.dataform.mapper.impl.DataFormMapperJSONFileImpl;
import com.vekai.dataform.service.DataFormAdminService;
import com.vekai.dataform.service.impl.DataFormAdminServiceJSONImpl;
import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.core.MapObjectCruder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by tisir yangsong158@qq.com on 2017-05-13
 * 认证授权服务模块自动注入,主要完成基本配置以及自动扫描包路径的基础设置
 */
@Configuration
@EnableConfigurationProperties(DataFormProperties.class)
@ComponentScan(basePackages={"com.vekai.dataform"})
public class DataFormAutoConfiguration implements InitializingBean {

    private DataFormProperties properties;


    public DataFormAutoConfiguration(DataFormProperties properties) {
        this.properties = properties;
    }

    @Bean("dataFormMapper")
    public DataFormMapper getDataFormMapper(){
        DataFormMapperJSONFileImpl dataFormMapper = new DataFormMapperJSONFileImpl();
        return  dataFormMapper;
//        if (DataFormProperties.TYPE_XML.equalsIgnoreCase(properties.getDataFormMapperType())) {
//            DataFormMapperXMLFileImpl dataFormMapper = new DataFormMapperXMLFileImpl();
//            dataFormMapper.setBasePath(properties.getResourcePath());
//            dataFormMapper.setVersionConflictDetection(false);
//            return  dataFormMapper;
//        } else if (DataFormProperties.TYPE_JSON.equalsIgnoreCase(properties.getDataFormMapperType())) {
//            DataFormMapperJSONFileImpl dataFormMapper = new DataFormMapperJSONFileImpl();
//           /* dataFormMapper.setBasePath(properties.getResourcePath());
//            dataFormMapper.setVersionConflictDetection(false);*/
//            return  dataFormMapper;
//        }
//        else {
//            DataFormMapperDBTableImpl dataFormMapper = new DataFormMapperDBTableImpl();
//            return  dataFormMapper;
//        }
    }

    @Bean("dataFormAdminService")
    public DataFormAdminService getDataFormAdminService(){
//        if (DataFormProperties.TYPE_JSON.equalsIgnoreCase(properties.getDataFormMapperType())) {
        DataFormAdminServiceJSONImpl dataFormAdminServiceJSONImpl = new DataFormAdminServiceJSONImpl();
        return  dataFormAdminServiceJSONImpl;
//        } else {
////            DataFormAdminServiceDBImpl dataFormAdminServiceDBImpl = new DataFormAdminServiceDBImpl();
////            return  dataFormAdminServiceDBImpl;
//            return null;
//        }
    }

    @Bean(DataFormConsts.MAP_DATA_LIST_HANDLER_DEFAULT)
    @ConditionalOnClass({MapObjectCruder.class})
    public MapDataListHandler getMapDataListHandler(MapObjectCruder mapObjectCruder){
        MapDataListHandler mapDataListHandler = new MapDataListHandler();
        mapDataListHandler.setMapObjectCruder(mapObjectCruder);
        return mapDataListHandler;
    }

    @Bean(DataFormConsts.MAP_DATA_ONE_HANDLER_DEFAULT)
    @ConditionalOnClass({MapObjectCruder.class})
    public MapDataOneHandler getMapDataOneHandler(MapObjectCruder mapObjectCruder){
        MapDataOneHandler mapDataOneHandler = new MapDataOneHandler();
        mapDataOneHandler.setMapObjectCruder(mapObjectCruder);
        return mapDataOneHandler;
    }

    @Bean(DataFormConsts.BEAN_DATA_ONE_HANDLER_DEFAULT)
    @ConditionalOnClass({BeanCruder.class})
    public BeanDataOneHandler<Object> getBeanDataOneHandler(BeanCruder beanCruder){
        BeanDataOneHandler<Object> handler = new BeanDataOneHandler<Object>();
        handler.setBeanCruder(beanCruder);
        return handler;
    }

    @Bean(DataFormConsts.BEAN_DATA_LIST_HANDLER_DEFAULT)
    @ConditionalOnClass({BeanCruder.class})
    public BeanDataListHandler<Object> getBeanDataListHandler(BeanCruder beanCruder){
        BeanDataListHandler<Object> handler = new BeanDataListHandler<Object>();
        handler.setBeanCruder(beanCruder);
        return handler;
    }

    public void afterPropertiesSet() throws Exception {
    }
}
