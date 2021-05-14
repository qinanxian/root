package com.vekai.workflow;

import cn.fisok.web.holder.WebHolder;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {WorkflowTestApplication.class})
@WebAppConfiguration
@ActiveProfiles({"dev", "shiro"})
public class BaseTest {

    @Value("${server.contextPath}")
    protected String contextPath ;

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationConnect;
    @Autowired
    protected MockHttpSession session;
    @Autowired
    protected MockHttpServletRequest request;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationConnect).build();
        MockServletContext servletContext = new MockServletContext();
        servletContext.setContextPath(contextPath);
        WebHolder.setServletContext(servletContext);
    }
}
