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

import cn.fisok.raw.kit.JSONKit;
import cn.fisok.sqloy.annotation.SQLDao;
import cn.fisok.raw.kit.ClassKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.sqloy.exception.SqloyException;
import org.apache.commons.lang3.ClassUtils;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : SQLDao 自动扫描处理类，在所有的类定义注册完成之后执行
 */
@Configuration
public class SQLDaoDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    private List<String> lookupBasePackages(BeanDefinitionRegistry registry) {
        List<String> packages = new ArrayList<String>();
        String[] names = registry.getBeanDefinitionNames();
        logger.info("names:"+ JSONKit.toJsonString(names));
        for (int i = 0; i < names.length; i++) {
            String beanName = names[i];
            BeanDefinition definition = registry.getBeanDefinition(beanName);
            String clazzName = definition.getBeanClassName();
            if (StringKit.isBlank(clazzName)) {
                continue;
            }
//            Class<?> clazz = ClassKit.forName(clazzName);
            Class<?> clazz = null;
            try {
                clazz = ClassUtils.getClass(clazzName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            ComponentScan anno = null;
            try{
                if("org.activiti.spring.boot.SecurityAutoConfiguration".equals(clazzName)){
                    int x = 1+1;
                }
                anno = clazz.getAnnotation(ComponentScan.class);
            }catch (Exception e){
                throw new SqloyException(e,"加载类{0}出错",clazz);
            }
            if (anno != null) {
                String[] basePackages = anno.basePackages();
                for (String pkg : basePackages) {
                    packages.add(pkg);
                }
            }
        }
        return packages;

    }

    private void register(String pkg, BeanNameGenerator beanNameGenerator, BeanDefinitionRegistry registry) {
        //取所有的自动扫描包
        Reflections reflections = null;
        reflections = new Reflections(new ConfigurationBuilder()
                .forPackages(pkg)
                .filterInputsBy(new FilterBuilder().include(".*?\\.class"))//只要class文件
                .setExpandSuperTypes(false));
//        try{
//            reflections = new Reflections(pkg);
//        Reflections reflections = new Reflections(new ConfigurationBuilder()
//                .setUrls(ClasspathHelper.forPackage(pkg))
//                .setScanners(new TypeAnnotationsScanner()));
//        }catch(Exception e){}

        Set<Class<?>> clazzSet = reflections.getTypesAnnotatedWith(SQLDao.class);
        for (Class<?> clazz : clazzSet) {
            RootBeanDefinition beanDefinition = new RootBeanDefinition();
            beanDefinition.setBeanClass(SQLDaoFactoryBean.class);
            beanDefinition.setLazyInit(true);
            beanDefinition.getPropertyValues().addPropertyValue("clazz", clazz);
//            String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);
            String beanName = clazz.getName();
            registry.registerBeanDefinition(beanName, beanDefinition);

        }
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        logger.debug("自动扫描SQLDao.....[开始]");
        final BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
        List<String> packages = lookupBasePackages(registry);
        packages.forEach(pkg -> {
            register(pkg, beanNameGenerator, registry);
        });
        logger.debug("自动扫描SQLDao.....[完成]");
    }

}
