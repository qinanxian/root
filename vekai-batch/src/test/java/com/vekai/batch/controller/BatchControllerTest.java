package com.vekai.batch.controller;

import com.vekai.batch.BaseTest;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BatchControllerTest extends BaseTest {


    @Test
    public void syncAndGetTimers() throws Exception {
        String url = "/batch/jobs/register/synchronize";
        //构建请求对象
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url);
        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();


        //构建请求对象
        url = "/batch/timers";
        requestBuilder = MockMvcRequestBuilders.get(url);
        result = mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

    }
}
