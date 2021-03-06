//package com.vekai.dataform.handler;
//
//import com.vekai.BaseTest;
//import com.vekai.dataform.handler.impl.DataBoxListHandler;
//import com.vekai.dataform.model.DataForm;
//import com.vekai.dataform.model.DataFormElement;
//import com.vekai.dataform.model.DataFormFilter;
//import com.vekai.dataform.model.types.*;
//import com.vekai.entity.Person;
//import com.vekai.saber.jdbc.query.PaginationQueryResult;
//import com.vekai.saber.kit.JSONKit;
//import com.vekai.saber.kit.MapKit;
//import com.vekai.saber.lang.DataBox;
//import com.vekai.saber.lang.ValueObject;
//import org.junit.Assert;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by tisir<yangsong158@qq.com> on 2017-05-29
// */
//public class DataFormHandlerTest extends BaseTest {
//
//    @Autowired
//    private DataBoxListHandler dataBoxListHandler;
//    @Autowired
//    private PersonListHandler personListHandler;
//    @Autowired
//    private PersonListHandlerExt personListHandlerExt;
//
//    @Test
//    public void testDataBoxListHandler(){
//        Assert.assertNotNull(dataBoxListHandler);
//        Assert.assertNotNull(personListHandler);
//        Assert.assertNotNull(personListHandlerExt);
//    }
//
//    @Test
//    public void testQueryDataBoxListByDataForm(){
//        Map<String,Object> paramaters = MapKit.newHashMap();
//        paramaters.put("Code","P1006");
//        paramaters.put("Birth", "1993-1-1");
//
//        Map<String,ValueObject> filterMap = MapKit.newHashMap();
//        filterMap.put("code1",ValueObject.valueOf("P1"));
//        filterMap.put("engName",ValueObject.valueOf("a"));
//        filterMap.put("gender",ValueObject.valueOf(new String[]{"F","M"}));
//
//        Map<String,String> sortMap = MapKit.newLinkedHashMap();
//        sortMap.put("code1","desc");
//        sortMap.put("gender","asc");
//
//        PaginationQueryResult<DataBox> result = dataBoxListHandler.query(personListForm(),paramaters,filterMap,sortMap,15,0);
//        System.out.println(JSONKit.toJsonString(result));
//    }
//
//
////    @Test
//    public void testQueryBeanListByDataForm(){
////        PaginationQueryResult<Person> result = personListHandler.query(personListForm(),null,null,15,0);
//        PaginationQueryResult<Person> result = personListHandlerExt.query(personListForm(),null,null,null,15,0);
//
//        System.out.println(JSONKit.toJsonString(result));
//    }
//
//    public DataForm personListForm(){
//        DataForm dataForm = new DataForm();
//
//        dataForm.setPack("demo");
//        dataForm.setId("PersonList");
//        dataForm.setName("??????????????????");
//        dataForm.setTable("DEMO_PERSON");
//        dataForm.setQuery("from DEMO_PERSON where CODE>:Code and BIRTH<:Birth");
//        dataForm.setSortCode("D-0020");
//        dataForm.setTags("??????,??????");
//        dataForm.setHandler("com.vekai.dataform.handler.impl.DataBoxListHandler");
//        dataForm.getFormUIHint().setFormStyle(FormStyle.DataGrid);
//
//        //??????????????????
//        List<DataFormElement> elements = dataForm.getElements();
//        //??????????????????
//        String group10 = "10:????????????";
//        dataForm.addElement(new DataFormElement("code1","CODE","????????????",group10, ElementDataType.String));
//        dataForm.addElement(new DataFormElement("name1","NAME","??????",group10, ElementDataType.String));
//        dataForm.addElement(new DataFormElement("chnName","CHN_NAME","?????????",group10));
//        dataForm.addElement(new DataFormElement("engName","ENG_NAME","?????????",group10));
//        dataForm.addElement(new DataFormElement("gender","GENDER","??????",group10));
//        dataForm.addElement(new DataFormElement("birth1","BIRTH","????????????",group10, ElementDataType.Date));
//        dataForm.addElement(new DataFormElement("height","HEIGHT","??????",group10, ElementDataType.Double));
//        dataForm.addElement(new DataFormElement("weight","WEIGHT","??????",group10, ElementDataType.Double));
//        dataForm.addElement(new DataFormElement("nation","NATION","??????",group10));
//        dataForm.addElement(new DataFormElement("political","POLITICAL","????????????",group10));
//        dataForm.addElement(new DataFormElement("marital","MARITAL","????????????",group10));
//        dataForm.addElement(new DataFormElement("educationLevel","EDUCATION_LEVEL","????????????",group10));
//        dataForm.addElement(new DataFormElement("graduatedFrom","GRADUATED_FROM","????????????",group10));
//        dataForm.addElement(new DataFormElement("domicilePlaceProvince","DOMICILE_PLACE_PROVINCE","???????????????",group10));
//        dataForm.addElement(new DataFormElement("domicilePlaceCity","DOMICILE_PLACE_CITY","???????????????",group10));
//
//        dataForm.getElement("birth1").setSummaryExpression("MAX(${COLUMN})");
//        dataForm.getElement("height").setSummaryExpression("MIN(${COLUMN})");
//        dataForm.getElement("gender").getElementUIHint().setDictCodeMode(ElementDataDictCodeMode.DictCode).setDictCodeExpr("DemoGender");
//        dataForm.getElement("birth1").getElementUIHint().setDataFormat(ElementDataFormat.Date).setEditStyle(ElementDataEditStyle.DatePicker);
//        dataForm.getElement("height").getElementUIHint().setDataFormat(ElementDataFormat.Double).setSuffix("CM");
//        dataForm.getElement("weight").getElementUIHint().setDataFormat(ElementDataFormat.Double).setSuffix("KG");
//        dataForm.getElement("political").getElementUIHint().setDictCodeMode(ElementDataDictCodeMode.DictCode).setDictCodeExpr("DemoPolitical");
//        dataForm.getElement("marital").getElementUIHint().setDictCodeMode(ElementDataDictCodeMode.DictCode).setDictCodeExpr("DemoMarital");
//        dataForm.getElement("educationLevel").getElementUIHint().setDictCodeMode(ElementDataDictCodeMode.DictCode).setDictCodeExpr("DemoEducationLevel");
//        dataForm.getElement("graduatedFrom").getElementUIHint().setNote("??????????????????????????????");
//
//        //????????????
//        String group40 = "40:????????????";
//        dataForm.addElement(new DataFormElement("monthIncome","MONTH_INCOME","???????????????",group40, ElementDataType.Double));
//        dataForm.addElement(new DataFormElement("familyMonthIncome","FAMILY_MONTH_INCOME","???????????????",group40, ElementDataType.Double));
//        dataForm.addElement(new DataFormElement("familyMonthCost","FAMILY_MONTH_COST","????????????????????????",group40, ElementDataType.Double));
//        dataForm.addElement(new DataFormElement("familyYearIncome","FAMILY_YEAR_INCOME","???????????????",group40, ElementDataType.Double));
//        dataForm.getElement("monthIncome").getElementUIHint().setDataFormat(ElementDataFormat.Currency).setSuffix("???");
//        dataForm.getElement("familyMonthIncome").getElementUIHint().setDataFormat(ElementDataFormat.Currency).setSuffix("???");
//        dataForm.getElement("familyMonthCost").getElementUIHint().setDataFormat(ElementDataFormat.Currency).setSuffix("???");
//        dataForm.getElement("familyYearIncome").setDigitalMultiple(10000d).getElementUIHint().setDataFormat(ElementDataFormat.Currency).setSuffix("??????");
//        dataForm.getElement("familyYearIncome").setSummaryExpression("SUM(${COLUMN})");
//
//
//        String group60 = "60:????????????";
//        dataForm.addElement(new DataFormElement("createdBy","CREATED_BY","?????????",group60));
//        dataForm.addElement(new DataFormElement("createdTime","CREATED_TIME","????????????",group60, ElementDataType.Date));
//        dataForm.addElement(new DataFormElement("updatedBy","UPDATED_BY","?????????",group60));
//        dataForm.addElement(new DataFormElement("updatedTime","UPDATED_TIME","????????????",group60, ElementDataType.Date));
//
//        //?????????????????????
//        dataForm.addFilter(new DataFormFilter().setCode("code1").setName("??????").setBindFor("code1"));
//        dataForm.addFilter(new DataFormFilter().setCode("engName").setName("?????????").setBindFor("engName").setComparePattern(ElementFilterComparePattern.Contain));
////        dataForm.addFilter(new DataFormFilter().setCode("gender").setName("??????").setBindFor("gender").setComparePattern(ElementFilterComparePattern.Equal));
//
//        return dataForm;
//    }
//
//}
