package com.vekai.lact.service;

import cn.fisok.web.kit.HttpKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@RestController
@RequestMapping("/lact")
public class LactController {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    ServerProperties serverProperties;

    @GetMapping("/ping")
    public String ping() {
        return "LACT IS RUNNING";
    }

    @GetMapping("/invokeAmix")
    public String invokeAmix() {
        String url = "http://MAIN-AMIX-APP" + serverProperties.getServlet().getContextPath() + "/ping/rest";
        return restTemplate.getForObject(url, String.class);
    }

    @GetMapping("/transferFile")
    public void transferFile(HttpServletResponse response) throws Exception {
        String url = "http://MAIN-AMIX-APP" + serverProperties.getServlet().getContextPath() + "/showcase/pageoffice/show/CASE-D61.pdf";
        OutputStream outputStream = response.getOutputStream();
        logger.info("******************************************");
        logger.info("begin to transfer at ");

        restTemplate.execute(url, HttpMethod.GET, null, clientResponse -> {
            HttpKit.renderStream(response, clientResponse.getBody(), "octets/stream", null);
            return null;
        });

    }

    @GetMapping("/transferFile2")
    public void transferFile2(HttpServletResponse response) throws Exception {
        String url = "http://MAIN-AMIX-APP" + serverProperties.getServlet().getContextPath() + "/showcase/pageoffice/show/CASE-D61.pdf";
        logger.info("******************************************");
        logger.info("begin to transfer at ");
        HttpKit.renderStream(response,
                restTemplate.getForObject(url, Resource.class).getInputStream(),
                "octets/stream", null);
    }
}