package com.vekai.fiscal.autoconfigure;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FiscalProperties.class)
@ComponentScan(basePackages={"com.vekai.fiscal"})
public class FiscalAutoConfiguration {

    private FiscalProperties properties;

    public FiscalAutoConfiguration(FiscalProperties properties) {
        this.properties = properties;
    }

//    @Autowired
//    private BeanCruder beanCruder;
//    @Autowired
//    private SQLTextLoader sqlTextLoader;
//
//    @Bean("fisicalEventService")
//    public FisicalEventService getFisicalEventService(){
//        FisicalEventServiceImpl fisicalEventServiceImpl = new FisicalEventServiceImpl();
//        fisicalEventServiceImpl.setbeanCruder(beanCruder);
//        fisicalEventServiceImpl.setSqlTextLoader(sqlTextLoader);
//        return fisicalEventServiceImpl;
//    }
}
