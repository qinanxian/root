//package com.vekai.office.autoconfigure;
//
//import com.vekai.runtime.autoconfigure.RawProperties;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//@EnableConfigurationProperties(OfficeProperties.class)
//public class POServletAutoConfiguration {
//    protected Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Autowired
//    protected OfficeProperties properties;
//    @Autowired
//    private RawProperties runtimeProperties;
//
//    public POServletAutoConfiguration(OfficeProperties properties) {
//        this.properties = properties;
//    }
//
//    public OfficeProperties getProperties() {
//        return properties;
//    }
//
//    public void setProperties(OfficeProperties properties) {
//        this.properties = properties;
//    }
//
//    public RawProperties getRuntimeProperties() {
//        return runtimeProperties;
//    }
//
//    public void setRuntimeProperties(RawProperties runtimeProperties) {
//        this.runtimeProperties = runtimeProperties;
//    }
//
//    /**
//     * 添加PageOffice的服务器端授权程序Servlet（必须）
//     *
//     * @return
//     */
//    @Bean
//    @ConditionalOnProperty(prefix = "com.vekai.office.page-office", name = "enable", havingValue = "true")
//    public ServletRegistrationBean pageOfficeServlet() {
//        String licensePath = properties.getPageOffice().getLicensePath();
//        String staticResourceUrl = properties.getPageOffice().getStaticResourceUrl();
//        com.zhuozhengsoft.pageoffice.poserver.Server poserver = new com.zhuozhengsoft.pageoffice.poserver.Server();
//        try{
//            poserver.setSysPath(licensePath);//设置PageOffice注册成功后,license.lic文件存放的目录
//        }catch (Exception e){
//            logger.error("加载PageOffice授权文件出错",e);
//        }
//
//        ServletRegistrationBean srb = new ServletRegistrationBean(poserver);
//        srb.addUrlMappings(staticResourceUrl + PageOfficeProperties.DIFF_PATH_SEGMENT + "/poserver.zz");
//        srb.addUrlMappings(staticResourceUrl + PageOfficeProperties.DIFF_PATH_SEGMENT + "/posetup.exe");
//        srb.addUrlMappings(staticResourceUrl + PageOfficeProperties.DIFF_PATH_SEGMENT + "/pageoffice.js");
//        srb.addUrlMappings(staticResourceUrl + PageOfficeProperties.DIFF_PATH_SEGMENT + "/jquery.min.js");
//        srb.addUrlMappings(staticResourceUrl + PageOfficeProperties.DIFF_PATH_SEGMENT + "/pobstyle.css");
//        srb.addUrlMappings(staticResourceUrl + PageOfficeProperties.DIFF_PATH_SEGMENT + "/sealsetup.exe");
//        return srb;
//    }
//}
