package com.vekai.auth;

import com.vekai.auth.autoconfigure.AuthAutoConfiguration;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({AuthTestApplication.MyBatisMapperScannerConfig.class, AuthAutoConfiguration.class})
public class AuthTestApplication {
    public static void main(String[] args){
        SpringApplication.run(AuthTestApplication.class,args);
    }

    @Configuration
    public static class MyBatisMapperScannerConfig{

        @Bean
        public MapperScannerConfigurer mapperScannerConfigurer() {
            MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
            mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
            mapperScannerConfigurer.setBasePackage("com.vekai.**.mapper");
            return mapperScannerConfigurer;
        }
    }
}
