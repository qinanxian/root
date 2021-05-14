package com.vekai.base.dictionary.controller;

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

public class DictControllerTest extends BaseTest {

    Logger logger = LoggerFactory.getLogger(DictControllerTest.class);

    @Test
    public void testGetDict() throws Exception {
        String url = "/base/dicts/Gender";

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
    public void testGetDictByFilter() throws Exception {
        String url = "/base/dicts/Gender/sort-filter/2";

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
