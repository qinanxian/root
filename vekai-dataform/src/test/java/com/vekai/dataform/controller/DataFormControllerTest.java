package com.vekai.dataform.controller;

import com.vekai.dataform.BaseTest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DataFormControllerTest extends BaseTest {


    @Test
    public void getInfoMeta() throws Exception {
        String url = "/dataform/meta/demo-PersonInfo";
        //构建请求对象
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url)
                .param("k1","v1")
                .param("k2","v2");

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void getListMeta() throws Exception {
        String url = "/dataform/meta/demo-PersonList";
        //构建请求对象
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

    }
    @Test
    public void getList() throws Exception {
//        String url = "/dataform/data/list/demo.PersonList/;/;/0-15";
//        String url = "/dataform/data/list/demo-PersonList/NULL/code1=DESC/0-15?code1=P101&engName=b";
        String url = "/dataform/data/list/demo-PersonList/code=P1001;gender=M/birth=ASC;code=DESC/0-15?code1=P101&engName=b&__quick=10";
        //构建请求对象
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void getBeanList() throws Exception {
//        String url = "/dataform/data/list/demo.PersonList/;/;/0-15";
        String url = "/dataform/data/list/demo-BeanPersonList/code=P1001;gender=M/1=1/0-15";
//        String url = "/dataform/data/list/demo-PersonList/NULL/code1=DESC/0-15?code1=P101&engName=b";
//        String url = "/dataform/data/list/demo-BeanPersonList/code=P1001;gender=M/birth=ASC;code=DESC/0-15?code1=P101&engName=b&monthIncome=10&monthIncome=1000000&status=A";
        //构建请求对象
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void getInfo() throws Exception {
        String url = "/dataform/data/one/demo-PersonInfo/id=11";
        //构建请求对象
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url)
                .param("k1","v1")
                .param("k2","v2");

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void saveListTest() throws Exception {
        String url = "/dataform/save/list/demo-PersonList/";
        String data = "[{\"code\":\"P1010\",\"name\":\"杰瑞\",\"chnName\":\"\",\"engName\":\"Jerry\",\"gender\":\"M\",\"birth\":699379200000,\"height\":null,\"weight\":null,\"nation\":null,\"political\":null,\"marital\":null,\"educationLevel\":null,\"graduatedFrom\":null,\"domicilePlaceProvince\":null,\"domicilePlaceCity\":null,\"domicilePlaceAddress\":null,\"presentAddress\":null,\"mobilePhone\":null,\"email\":null,\"workAs\":null,\"jobTitle\":null,\"companyIndustry\":null,\"companyAddress\":null,\"companyPostcode\":null,\"entryDate\":null,\"monthIncome\":null,\"familyMonthIncome\":null,\"familyYearIncome\":null,\"familyMonthCost\":null,\"hobby\":null,\"remark\":null,\"status\":null,\"revision\":null,\"createdBy\":null,\"createdTime\":null,\"updatedBy\":null,\"updatedTime\":null},{\"code\":\"P1011\",\"name\":\"卡伦\",\"chnName\":\"\",\"engName\":\"Karen\",\"gender\":\"M\",\"birth\":599760000000,\"height\":null,\"weight\":null,\"nation\":null,\"political\":null,\"marital\":null,\"educationLevel\":null,\"graduatedFrom\":null,\"domicilePlaceProvince\":null,\"domicilePlaceCity\":null,\"domicilePlaceAddress\":null,\"presentAddress\":null,\"mobilePhone\":null,\"email\":null,\"workAs\":null,\"jobTitle\":null,\"companyIndustry\":null,\"companyAddress\":null,\"companyPostcode\":null,\"entryDate\":null,\"monthIncome\":null,\"familyMonthIncome\":null,\"familyYearIncome\":null,\"familyMonthCost\":null,\"hobby\":null,\"remark\":null,\"status\":null,\"revision\":null,\"createdBy\":null,\"createdTime\":null,\"updatedBy\":null,\"updatedTime\":null}]";
//        String data = "[{\"code\":\"P1010\",\"name\":\"杰瑞\",\"chnName\":\"\",\"engName\":\"Jerry\",\"gender\":\"M\",\"height\":null,\"weight\":null,\"nation\":null,\"political\":null,\"marital\":null,\"educationLevel\":null,\"graduatedFrom\":null,\"domicilePlaceProvince\":null,\"domicilePlaceCity\":null,\"domicilePlaceAddress\":null,\"presentAddress\":null,\"mobilePhone\":null,\"email\":null,\"workAs\":null,\"jobTitle\":null,\"companyIndustry\":null,\"companyAddress\":null,\"companyPostcode\":null,\"entryDate\":null,\"monthIncome\":null,\"familyMonthIncome\":null,\"familyYearIncome\":null,\"familyMonthCost\":null,\"hobby\":null,\"remark\":null,\"status\":null,\"revision\":null,\"createdBy\":null,\"createdTime\":null,\"updatedBy\":null,\"updatedTime\":null},{\"code\":\"P1011\",\"name\":\"卡伦\",\"chnName\":\"\",\"engName\":\"Karen\",\"gender\":\"M\",\"height\":null,\"weight\":null,\"nation\":null,\"political\":null,\"marital\":null,\"educationLevel\":null,\"graduatedFrom\":null,\"domicilePlaceProvince\":null,\"domicilePlaceCity\":null,\"domicilePlaceAddress\":null,\"presentAddress\":null,\"mobilePhone\":null,\"email\":null,\"workAs\":null,\"jobTitle\":null,\"companyIndustry\":null,\"companyAddress\":null,\"companyPostcode\":null,\"entryDate\":null,\"monthIncome\":null,\"familyMonthIncome\":null,\"familyYearIncome\":null,\"familyMonthCost\":null,\"hobby\":null,\"remark\":null,\"status\":null,\"revision\":null,\"createdBy\":null,\"createdTime\":null,\"updatedBy\":null,\"updatedTime\":null}]";
        //构建请求对象
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(data)
                .param("k1","v1")
                .param("k2","v2");

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void saveBeanListTest() throws Exception {
        String url = "/dataform/save/list/demo-BeanPersonList/";
//        String data = "[{\"code\":\"P1010\",\"name\":\"杰瑞\",\"chnName\":\"\",\"engName\":\"Jerry\",\"gender\":\"M\",\"birth\":699379200000,\"height\":null,\"weight\":null,\"nation\":null,\"political\":null,\"marital\":null,\"educationLevel\":null,\"graduatedFrom\":null,\"domicilePlaceProvince\":null,\"domicilePlaceCity\":null,\"domicilePlaceAddress\":null,\"presentAddress\":null,\"mobilePhone\":null,\"email\":null,\"workAs\":null,\"jobTitle\":null,\"companyIndustry\":null,\"companyAddress\":null,\"companyPostcode\":null,\"entryDate\":null,\"monthIncome\":null,\"familyMonthIncome\":null,\"familyYearIncome\":null,\"familyMonthCost\":null,\"hobby\":null,\"remark\":null,\"status\":null,\"revision\":null,\"createdBy\":null,\"createdTime\":null,\"updatedBy\":null,\"updatedTime\":null},{\"code\":\"P1011\",\"name\":\"卡伦\",\"chnName\":\"\",\"engName\":\"Karen\",\"gender\":\"M\",\"birth\":599760000000,\"height\":null,\"weight\":null,\"nation\":null,\"political\":null,\"marital\":null,\"educationLevel\":null,\"graduatedFrom\":null,\"domicilePlaceProvince\":null,\"domicilePlaceCity\":null,\"domicilePlaceAddress\":null,\"presentAddress\":null,\"mobilePhone\":null,\"email\":null,\"workAs\":null,\"jobTitle\":null,\"companyIndustry\":null,\"companyAddress\":null,\"companyPostcode\":null,\"entryDate\":null,\"monthIncome\":null,\"familyMonthIncome\":null,\"familyYearIncome\":null,\"familyMonthCost\":null,\"hobby\":null,\"remark\":null,\"status\":null,\"revision\":null,\"createdBy\":null,\"createdTime\":null,\"updatedBy\":null,\"updatedTime\":null}]";
        String data = "[{\"id\":\"10\",\"code\":\"P1010\",\"name\":\"杰瑞\",\"chnName\":\"\",\"engName\":\"Jerry\",\"gender\":\"M\",\"birth\":699379200000,\"height\":null,\"weight\":null,\"nation\":null,\"political\":null,\"marital\":null,\"educationLevel\":null,\"graduatedFrom\":null,\"domicilePlaceProvince\":null,\"domicilePlaceCity\":null,\"domicilePlaceAddress\":null,\"presentAddress\":null,\"mobilePhone\":null,\"email\":null,\"workAs\":null,\"jobTitle\":null,\"companyIndustry\":null,\"companyAddress\":null,\"companyPostcode\":null,\"entryDate\":null,\"monthIncome\":null,\"familyMonthIncome\":null,\"familyYearIncome\":null,\"familyMonthCost\":null,\"hobby\":null,\"remark\":null,\"status\":null,\"revision\":null,\"createdBy\":null,\"createdTime\":null,\"updatedBy\":null,\"updatedTime\":null},{\"id\":\"11\",\"code\":\"P1011\",\"name\":\"卡伦\",\"chnName\":\"\",\"engName\":\"Karen\",\"gender\":\"M\",\"birth\":599760000000,\"height\":null,\"weight\":null,\"nation\":null,\"political\":null,\"marital\":null,\"educationLevel\":null,\"graduatedFrom\":null,\"domicilePlaceProvince\":null,\"domicilePlaceCity\":null,\"domicilePlaceAddress\":null,\"presentAddress\":null,\"mobilePhone\":null,\"email\":null,\"workAs\":null,\"jobTitle\":null,\"companyIndustry\":null,\"companyAddress\":null,\"companyPostcode\":null,\"entryDate\":null,\"monthIncome\":null,\"familyMonthIncome\":null,\"familyYearIncome\":null,\"familyMonthCost\":null,\"hobby\":null,\"remark\":null,\"status\":null,\"revision\":null,\"createdBy\":null,\"createdTime\":null,\"updatedBy\":null,\"updatedTime\":null}]";
        //构建请求对象
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(data)
                .param("k1","v1")
                .param("k2","v2");

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void deleteBeanListTest() throws Exception {
        String url = "/dataform/delete/list/demo-BeanPersonList/";
//        String data = "[{\"code\":\"P1010\",\"name\":\"杰瑞\",\"chnName\":\"\",\"engName\":\"Jerry\",\"gender\":\"M\",\"birth\":699379200000,\"height\":null,\"weight\":null,\"nation\":null,\"political\":null,\"marital\":null,\"educationLevel\":null,\"graduatedFrom\":null,\"domicilePlaceProvince\":null,\"domicilePlaceCity\":null,\"domicilePlaceAddress\":null,\"presentAddress\":null,\"mobilePhone\":null,\"email\":null,\"workAs\":null,\"jobTitle\":null,\"companyIndustry\":null,\"companyAddress\":null,\"companyPostcode\":null,\"entryDate\":null,\"monthIncome\":null,\"familyMonthIncome\":null,\"familyYearIncome\":null,\"familyMonthCost\":null,\"hobby\":null,\"remark\":null,\"status\":null,\"revision\":null,\"createdBy\":null,\"createdTime\":null,\"updatedBy\":null,\"updatedTime\":null},{\"code\":\"P1011\",\"name\":\"卡伦\",\"chnName\":\"\",\"engName\":\"Karen\",\"gender\":\"M\",\"birth\":599760000000,\"height\":null,\"weight\":null,\"nation\":null,\"political\":null,\"marital\":null,\"educationLevel\":null,\"graduatedFrom\":null,\"domicilePlaceProvince\":null,\"domicilePlaceCity\":null,\"domicilePlaceAddress\":null,\"presentAddress\":null,\"mobilePhone\":null,\"email\":null,\"workAs\":null,\"jobTitle\":null,\"companyIndustry\":null,\"companyAddress\":null,\"companyPostcode\":null,\"entryDate\":null,\"monthIncome\":null,\"familyMonthIncome\":null,\"familyYearIncome\":null,\"familyMonthCost\":null,\"hobby\":null,\"remark\":null,\"status\":null,\"revision\":null,\"createdBy\":null,\"createdTime\":null,\"updatedBy\":null,\"updatedTime\":null}]";
        String data = "[{\"id\":\"10\",\"code\":\"P1010\"},{\"id\":\"11\",\"code\":\"P1011\",\"name\":\"卡伦\"}]";
        //构建请求对象
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(data);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void invokeHandlerTest() throws Exception {
        String url = "/dataform/invoke/demo-BeanPersonList/createNewPerson";
        // dataform/invoke/{formId}/{methodName}";
        // volist.invoke('methodName',{}).then((data)=>{});
//        String data = "[{\"code\":\"P1010\",\"name\":\"杰瑞\",\"chnName\":\"\",\"engName\":\"Jerry\",\"gender\":\"M\",\"birth\":699379200000,\"height\":null,\"weight\":null,\"nation\":null,\"political\":null,\"marital\":null,\"educationLevel\":null,\"graduatedFrom\":null,\"domicilePlaceProvince\":null,\"domicilePlaceCity\":null,\"domicilePlaceAddress\":null,\"presentAddress\":null,\"mobilePhone\":null,\"email\":null,\"workAs\":null,\"jobTitle\":null,\"companyIndustry\":null,\"companyAddress\":null,\"companyPostcode\":null,\"entryDate\":null,\"monthIncome\":null,\"familyMonthIncome\":null,\"familyYearIncome\":null,\"familyMonthCost\":null,\"hobby\":null,\"remark\":null,\"status\":null,\"revision\":null,\"createdBy\":null,\"createdTime\":null,\"updatedBy\":null,\"updatedTime\":null},{\"code\":\"P1011\",\"name\":\"卡伦\",\"chnName\":\"\",\"engName\":\"Karen\",\"gender\":\"M\",\"birth\":599760000000,\"height\":null,\"weight\":null,\"nation\":null,\"political\":null,\"marital\":null,\"educationLevel\":null,\"graduatedFrom\":null,\"domicilePlaceProvince\":null,\"domicilePlaceCity\":null,\"domicilePlaceAddress\":null,\"presentAddress\":null,\"mobilePhone\":null,\"email\":null,\"workAs\":null,\"jobTitle\":null,\"companyIndustry\":null,\"companyAddress\":null,\"companyPostcode\":null,\"entryDate\":null,\"monthIncome\":null,\"familyMonthIncome\":null,\"familyYearIncome\":null,\"familyMonthCost\":null,\"hobby\":null,\"remark\":null,\"status\":null,\"revision\":null,\"createdBy\":null,\"createdTime\":null,\"updatedBy\":null,\"updatedTime\":null}]";
        String data = "{\"code\":\"PX8008\"}";
        //构建请求对象
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(data);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
    }
}
