//package com.vekai.dataform.service;
//
//import com.vekai.dataform.BaseTest;
//import com.vekai.dataform.dto.CloneDataFormBean;
//import com.vekai.dataform.model.DataForm;
//import com.vekai.dataform.model.DataFormElement;
//import com.vekai.dataform.service.impl.DataFormAdminServiceJSONImpl;
//import cn.fisok.raw.kit.JSONKit;
//import org.junit.Assert;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//
//import java.util.List;
//
//public class DataFormAdminServiceTest extends BaseTest {
//
//    @Autowired
//    protected DataFormAdminServiceDBImpl adminServiceDBImpl;
//    @Autowired
//    protected DataFormAdminServiceJSONImpl adminServiceJSONImpl;
//
//    @Autowired
//    @Qualifier("dataFormAdminServiceJSONImpl")
//    private DataFormAdminService dataFormAdminService;
//
//    @Test
//    public void test1(){
////        List<DataFormElement> elements = adminServiceDBImpl.parseElementsFromTables("AUTH_USER");
//        List<DataFormElement> elements = adminServiceDBImpl.parseElementsFromTables("xxi-info","AUTH_USER AS A","AUTH_ORG B","ACT_ID_INFO");
//        System.out.println(JSONKit.toJsonString(elements));
//    }
//
//    @Test
//    public void test2(){
////        List<DataFormElement> elements = adminServiceDBImpl.parseElementsFromTables("AUTH_USER");
//        String id="PersonSimpleInfo";
//        String tables = "DEMO_PERSON";
//        List<DataFormElement> elements = adminServiceDBImpl.parseElementsFromTables(id,tables.split(","));
//        DataFormElement element = elements.get(1);
//        System.out.println(element.getCode()+":"+element.getName());
//    }
//
//    @Test
//    public void testDb2Json(){
//        adminServiceJSONImpl.dbTransferToJsonFile();
//    }
//
//
//    @Test
//    public void testGetDataForms() {
//        List<DataForm> dataForms = dataFormAdminService.getDataForms();
//        Assert.assertNotEquals(dataForms.size(),0);
//    }
//
//    @Test
//    public void testGetDataForm() {
//        String dataFormId = "invest-PlanList";
//        DataForm dataForm = dataFormAdminService.getDataForm(dataFormId);
//        Assert.assertEquals(dataForm.getCode(),"PlanList");
//    }
//
//    @Test
//    public void testCloneDataForm() {
//        CloneDataFormBean cloneDataFormBean = new CloneDataFormBean();
//        cloneDataFormBean.setOldDataFormId("invest-PlanList");
//        cloneDataFormBean.setNewDataFormId("invest-PlanListCopy");
//        DataForm cloneDataForm = dataFormAdminService.cloneDataForm(cloneDataFormBean);
//        Assert.assertEquals(cloneDataForm.getCode(),"PlanListCopy");
//    }
//
//    @Test
//    public void testGetDataFormElementDetail() {
//        String dataFormId = "invest-PlanList";
//        String code = "planType";
//        DataFormElement element = dataFormAdminService.getDataFormElementDetail(dataFormId,code);
//        Assert.assertEquals(element.getColumn(),"PLAN_TYPE");
//    }
//
//    @Test
//    public void testSaveDataFormElement() {
//        String dataFormId = "invest-PlanListCopy";
//        String code = "planType";
//        DataFormElement element = dataFormAdminService.getDataFormElementDetail(dataFormId,code);
//        element.setNameI18nCode("aaaabbbcccc");
//        element = dataFormAdminService.saveDataFormElement(element,dataFormId);
//        Assert.assertEquals(element.getNameI18nCode(),"aaaabbbcccc");
//    }
//
//    @Test
//    public void testSaveDataForm() {
//        String dataFormId = "invest-PlanListCopy";
//        DataForm dataForm = dataFormAdminService.getDataForm(dataFormId);
//        dataForm.setCode("PlanList123");
//        DataForm newDataForm = dataFormAdminService.saveDataForm(dataForm,dataFormId);
//        Assert.assertEquals(newDataForm.getId(),"invest-PlanList123");
//    }
//
//
//    @Test
//    public void testParseElementsFromTables() {
//        String dataFormId = "invest-PlanInfo";
//        String[] tables = {"IVST_PLAN"};
//        List<DataFormElement> dataFormElements = dataFormAdminService.parseElementsFromTables(dataFormId,tables);
//        Assert.assertNotEquals(dataFormElements.size(),0);
//    }
//
//}
