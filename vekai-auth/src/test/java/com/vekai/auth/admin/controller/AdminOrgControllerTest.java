package com.vekai.auth.admin.controller;

import com.vekai.auth.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminOrgControllerTest extends BaseTest {

    Logger logger = LoggerFactory.getLogger(AdminOrgControllerTest.class);

    @Test
    public void testGetAllOrgTree() throws Exception {
        String url = "/auth/admin/org/allOrgTree";

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);

        MvcResult result = mockMvc.perform(requestBuilder)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andReturn();

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getResponse());
        Assert.assertNotNull(result.getResponse().getContentAsString());
        logger.info("response:" + result.getResponse().getContentAsString());
    }

    @Test
    public void testGetSubOrgTreeByOrgId() throws Exception {
        String url = "/auth/admin/org/0001/subOrgTree";

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);

        MvcResult result = mockMvc.perform(requestBuilder)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andReturn();

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getResponse());
        Assert.assertNotNull(result.getResponse().getContentAsString());
        logger.info("response:" + result.getResponse().getContentAsString());
    }
}
