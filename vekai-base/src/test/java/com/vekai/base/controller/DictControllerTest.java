package com.vekai.base.controller;

import com.vekai.base.BaseTest;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


public class DictControllerTest extends BaseTest {

    @Test
    public void getIndustryCodeTree() throws Exception {
        //构建请求对象
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/base/dicts/Industry/tree")
                ;

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }
}
