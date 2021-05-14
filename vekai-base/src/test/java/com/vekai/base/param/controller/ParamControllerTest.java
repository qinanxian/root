package com.vekai.base.param.controller;

import com.vekai.base.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ParamControllerTest extends BaseTest {

    Logger logger = LoggerFactory.getLogger(ParamControllerTest.class);

    @Test
    public void testGetParam() throws Exception {
        String url = "/base/params/DemoTree";

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
    public void testGetParamItemsAsTree() throws Exception {
        String url = "/base/params/itemsTree/DemoTree";

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
    public void testGetParamByFilter() throws Exception {
        String url = "/base/params/DemoTree/items/sort-filter/10";

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
    public void testGetParamItemsAsTreeByFilter() throws Exception {
        String url = "/base/params/DemoTree/itemsTree/sort-filter/10";

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
