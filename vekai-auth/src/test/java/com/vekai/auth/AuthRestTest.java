package com.vekai.auth;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import org.springframework.test.web.servlet.result.HeaderResultMatchers;

public class AuthRestTest extends BaseTest {
    @Test
    public void testLogon() throws Exception {

        //构建请求对象
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/logon")
                .param(AuthConsts.REDIRECT_LOCATION_PARAM,"/index")
                .param("userCode","syang")
                .param("password","Ro000000");

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/index"))
                .andReturn();
    }
//    @Test
    public void testLogonError() throws Exception {

        //构建请求对象
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/logon")
                .param(AuthConsts.REDIRECT_LOCATION_PARAM,"/index")
                .param("userCode","syang")
                .param("password","Ro0000001");

        MvcResult result = mockMvc.perform(requestBuilder)
//                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isInternalServerError())
                .andReturn();
        Assert.assertTrue(IncorrectCredentialsException.class.isAssignableFrom(result.getResolvedException().getClass()));
    }
}
