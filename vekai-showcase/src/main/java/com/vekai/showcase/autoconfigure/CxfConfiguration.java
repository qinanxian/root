//package com.vekai.showcase.autoconfigure;
//
//import com.vekai.showcase.webservices.DemoService;
//import com.vekai.showcase.webservices.impl.DemoServiceImpl;
//import org.apache.cxf.Bus;
//import org.apache.cxf.bus.spring.SpringBus;
//import org.apache.cxf.jaxws.EndpointImpl;
//import org.apache.cxf.transport.servlet.CXFServlet;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.xml.ws.Endpoint;
//
////@Configuration
//public class CxfConfiguration {
//
//    @Bean
//    public ServletRegistrationBean webServicesDispatcherServlet() {
//        return new ServletRegistrationBean(new CXFServlet(),"/wsdemo/*");
//    }
//    @Bean(name = Bus.DEFAULT_BUS_ID)
//    public SpringBus springBus() {
//        return new SpringBus();
//    }
//
//
//    @Bean
//    public DemoService demoService() {
//        return new DemoServiceImpl();
//    }
//    @Bean
//    public Endpoint endpoint(SpringBus bus) {
//        //外部访问地址：http://localhost:8080/amix/wsdemo/wsapi?wsdl
//        EndpointImpl endpoint = new EndpointImpl(bus, demoService());
//        endpoint.publish("/wsapi");
//        return endpoint;
//    }
//
//}