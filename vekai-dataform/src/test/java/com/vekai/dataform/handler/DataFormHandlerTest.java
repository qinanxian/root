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
//        dataForm.setName("个人详细信息");
//        dataForm.setTable("DEMO_PERSON");
//        dataForm.setQuery("from DEMO_PERSON where CODE>:Code and BIRTH<:Birth");
//        dataForm.setSortCode("D-0020");
//        dataForm.setTags("案例,列表");
//        dataForm.setHandler("com.vekai.dataform.handler.impl.DataBoxListHandler");
//        dataForm.getFormUIHint().setFormStyle(FormStyle.DataGrid);
//
//        //处理元素部分
//        List<DataFormElement> elements = dataForm.getElements();
//        //基本信息部分
//        String group10 = "10:基本信息";
//        dataForm.addElement(new DataFormElement("code1","CODE","个人代号",group10, ElementDataType.String));
//        dataForm.addElement(new DataFormElement("name1","NAME","姓名",group10, ElementDataType.String));
//        dataForm.addElement(new DataFormElement("chnName","CHN_NAME","中文名",group10));
//        dataForm.addElement(new DataFormElement("engName","ENG_NAME","英文名",group10));
//        dataForm.addElement(new DataFormElement("gender","GENDER","性别",group10));
//        dataForm.addElement(new DataFormElement("birth1","BIRTH","出生日期",group10, ElementDataType.Date));
//        dataForm.addElement(new DataFormElement("height","HEIGHT","身高",group10, ElementDataType.Double));
//        dataForm.addElement(new DataFormElement("weight","WEIGHT","体重",group10, ElementDataType.Double));
//        dataForm.addElement(new DataFormElement("nation","NATION","名族",group10));
//        dataForm.addElement(new DataFormElement("political","POLITICAL","政治面貌",group10));
//        dataForm.addElement(new DataFormElement("marital","MARITAL","婚姻状况",group10));
//        dataForm.addElement(new DataFormElement("educationLevel","EDUCATION_LEVEL","最高学历",group10));
//        dataForm.addElement(new DataFormElement("graduatedFrom","GRADUATED_FROM","毕业院校",group10));
//        dataForm.addElement(new DataFormElement("domicilePlaceProvince","DOMICILE_PLACE_PROVINCE","籍贯（省）",group10));
//        dataForm.addElement(new DataFormElement("domicilePlaceCity","DOMICILE_PLACE_CITY","籍贯（市）",group10));
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
//        dataForm.getElement("graduatedFrom").getElementUIHint().setNote("取得最高学历所在院校");
//
//        //经济状况
//        String group40 = "40:经济状况";
//        dataForm.addElement(new DataFormElement("monthIncome","MONTH_INCOME","个人月收入",group40, ElementDataType.Double));
//        dataForm.addElement(new DataFormElement("familyMonthIncome","FAMILY_MONTH_INCOME","家庭月收入",group40, ElementDataType.Double));
//        dataForm.addElement(new DataFormElement("familyMonthCost","FAMILY_MONTH_COST","家庭每月固定支出",group40, ElementDataType.Double));
//        dataForm.addElement(new DataFormElement("familyYearIncome","FAMILY_YEAR_INCOME","家庭年收入",group40, ElementDataType.Double));
//        dataForm.getElement("monthIncome").getElementUIHint().setDataFormat(ElementDataFormat.Currency).setSuffix("元");
//        dataForm.getElement("familyMonthIncome").getElementUIHint().setDataFormat(ElementDataFormat.Currency).setSuffix("元");
//        dataForm.getElement("familyMonthCost").getElementUIHint().setDataFormat(ElementDataFormat.Currency).setSuffix("元");
//        dataForm.getElement("familyYearIncome").setDigitalMultiple(10000d).getElementUIHint().setDataFormat(ElementDataFormat.Currency).setSuffix("万元");
//        dataForm.getElement("familyYearIncome").setSummaryExpression("SUM(${COLUMN})");
//
//
//        String group60 = "60:操作信息";
//        dataForm.addElement(new DataFormElement("createdBy","CREATED_BY","创建人",group60));
//        dataForm.addElement(new DataFormElement("createdTime","CREATED_TIME","创建时间",group60, ElementDataType.Date));
//        dataForm.addElement(new DataFormElement("updatedBy","UPDATED_BY","更新人",group60));
//        dataForm.addElement(new DataFormElement("updatedTime","UPDATED_TIME","更新时间",group60, ElementDataType.Date));
//
//        //添加两个过滤项
//        dataForm.addFilter(new DataFormFilter().setCode("code1").setName("编号").setBindFor("code1"));
//        dataForm.addFilter(new DataFormFilter().setCode("engName").setName("英文名").setBindFor("engName").setComparePattern(ElementFilterComparePattern.Contain));
////        dataForm.addFilter(new DataFormFilter().setCode("gender").setName("性别").setBindFor("gender").setComparePattern(ElementFilterComparePattern.Equal));
//
//        return dataForm;
//    }
//
//}
