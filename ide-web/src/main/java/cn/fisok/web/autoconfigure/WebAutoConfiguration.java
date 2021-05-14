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
package cn.fisok.web.autoconfigure;

import cn.fisok.raw.kit.FileKit;
import cn.fisok.raw.kit.LogKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.web.holder.WebHolder;
import cn.fisok.web.interceptor.WebHolderInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.UrlPathHelper;

import javax.annotation.PostConstruct;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/1/10
 * @desc : WEB运行支持自动加载器
 */
@Configuration
@ComponentScan(basePackages = "cn.fisok.web")
@EnableConfigurationProperties(WebProperties.class)
public class WebAutoConfiguration extends WebMvcConfigurerAdapter implements ServletContextInitializer {
    private WebProperties properties;

    public WebAutoConfiguration(WebProperties properties) {
        this.properties = properties;
    }

    /**
     * 开发人员可以根据需求定制URL路径的匹配规则
     * Springboot 默认是无法使用矩阵变量绑定参数的。需要覆盖WebMvcConfigurer中的configurePathMatch方法。
     * @param configurer
     */
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = configurer.getUrlPathHelper();
        if (null == urlPathHelper){
            urlPathHelper = new UrlPathHelper();
        }
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }

    @PostConstruct
    public void init(){
        LogKit.info("==========>FISOK-WEB模块加载成功<==========");
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        String tmpDir = properties.getMultipartTempLocation();
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofBytes(properties.getMultipartMaxFileSize()));
        if(StringKit.isNotBlank(tmpDir)){
            factory.setLocation(tmpDir);
            File file = new File(tmpDir);
            file.mkdirs();
        }
        return factory.createMultipartConfig();
    }

    /**
     * 拦截器，处理WEB的请求及会话对象置入请求线程中去
     * @param registry
     */
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new WebHolderInterceptor()).addPathPatterns("/**");
    }


    /**
     * 启动的时候把ServletContext放入保持器
     * @param servletContext
     * @throws ServletException
     */
    public void onStartup(ServletContext servletContext) throws ServletException {
        WebHolder.setServletContext(servletContext);
    }

    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if(properties.getCross().isCorsEnable()) {
            registry.addMapping(properties.getCross().getPathPattern())
                    .allowedMethods(properties.getCross().getAllowedMethods())
                    .allowedOrigins(properties.getCross().getAllowedOrigins())
                    .exposedHeaders("WWW-Authenticate")
                    .allowCredentials(true);
        }
    }

}
