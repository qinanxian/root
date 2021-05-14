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
package cn.fisok.raw.autoconfigure;

import cn.fisok.raw.kit.ListKit;
import cn.fisok.raw.kit.LogKit;
import cn.fisok.raw.kit.StringKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/01/04
 * @desc :
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableConfigurationProperties(RawProperties.class)
@ComponentScan(basePackages = "cn.fisok.raw")
public class RawAutoConfiguration implements WebMvcConfigurer {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private RawProperties properties;

    public RawAutoConfiguration(RawProperties properties) {
        this.properties = properties;
    }

//
//    @Bean("runtimePostProcessor")
//    public BeanPostProcessorImpl getBeanPostProcessorImpl(){
//        return new BeanPostProcessorImpl();
//    }
    @PostConstruct
    public void init(){
        LogKit.info("==========>FISOK-RAW模块加载成功<==========");
    }




    @Bean
    public FilterRegistrationBean characterEncodingFilterRegistration() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        CharacterEncodingFilter filter = new CharacterEncodingFilter(properties.getCharset().toString(), true);
        registrationBean.setFilter(filter);
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

    @Bean("scriptEngine")
    @Scope("prototype")
    public ScriptEngine getScriptEngineManager() {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName(properties.getScriptEngineName());
        //1.运行全局脚本
        try {
            if (StringKit.isNotBlank(properties.getGlobalScript())) {
                scriptEngine.eval(properties.getGlobalScript());
            }
        } catch (ScriptException e) {
            logger.error("执行全局脚本出错,["+properties.getGlobalScript()+"]",e);
        }

        return scriptEngine;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


    public StringHttpMessageConverter getStringHttpMessageConverter(){
        StringHttpMessageConverter shmc = new StringHttpMessageConverter(properties.getCharset());
        shmc.setSupportedMediaTypes(ListKit.listOf(new MediaType(MediaType.TEXT_PLAIN,properties.getCharset())));
        return shmc;
    }


    //配置HTTP消息转换器
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(getStringHttpMessageConverter());
    }

//    @Bean
//    public LocaleResolver localeResolver() {
//        SessionLocaleResolver slr = new SessionLocaleResolver();
//        // 默认语言
//        slr.setDefaultLocale(Locale.CHINESE);
//        return slr;
//    }

}
