package com.vekai.dataform.mapper;

import cn.fisok.raw.kit.ClassKit;
import cn.fisok.sqloy.core.SqlQuery;
import com.vekai.dataform.entity.Person;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.model.types.ElementDataType;
import com.vekai.dataform.model.types.FormDataModelType;
import com.vekai.dataform.model.types.FormStyle;
import com.vekai.dataform.service.DataFormService;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

public class DataFormSQLTest {


    @Test
    public void dataformSQLGenTest() {
        DataForm dataForm = demoPersonInfo();
        DataFormService dataFormService = new DataFormService();
        dataFormService.fillSelectedItems(dataForm);

        String sql = dataForm.getQuery().buildQuerySql(false);
        System.out.println(sql);
    }

    public static DataForm demoPersonInfo() {
        DataForm dataForm = new DataForm();

        dataForm.setPack("demo");
        dataForm.setCode("BeanPersonInfo");
        dataForm.setName("个人详细信息");
        dataForm.setDataModel(Person.class.getName());
        dataForm.setDataModelType(FormDataModelType.JavaBean);
        dataForm.setSortCode("D-0010");
        dataForm.setTags("案例,详情");
        dataForm.getFormUIHint().setColumnNumber(2);
        dataForm.getFormUIHint().setFormStyle(FormStyle.DetailInfo);


        SqlQuery query = new SqlQuery();
        query.setFrom("DEMO_PERSON");
        query.setWhere("ID=:id");
        dataForm.setQuery(query);

        //处理元素部分
        List<DataFormElement> elements = dataForm.getElements();
        //基本信息部分
        String group10 = "10:基本信息";
        dataForm.addElement(new DataFormElement("code", "CODE", "个人代号", group10, ElementDataType.String).setPrimaryKey(true));
        dataForm.addElement(new DataFormElement("name", "NAME", "姓名", group10, ElementDataType.String));
        dataForm.addElement(new DataFormElement("chnName", "CHN_NAME", "中文名", group10));
        dataForm.addElement(new DataFormElement("engName", "ENG_NAME", "英文名", group10));
        dataForm.addElement(new DataFormElement("gender", "GENDER", "性别", group10));
        dataForm.addElement(new DataFormElement("birth", "BIRTH", "出生日期", group10, ElementDataType.Date));
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
//        dataForm.getElement("gender").getElementUIHint().setDictCodeMode(ElementDataDictCodeMode.DictCode).setDictCodeExpr("Gender");
//        dataForm.getElement("birth").getElementUIHint().setDataFormat(ElementDataFormat.Date).setEditStyle(ElementDataEditStyle.DatePicker);
//        dataForm.getElement("height").getElementUIHint().setDataFormat(ElementDataFormat.Double).setSuffix("CM");
//        dataForm.getElement("weight").getElementUIHint().setDataFormat(ElementDataFormat.Double).setSuffix("KG");
//        dataForm.getElement("political").getElementUIHint().setDictCodeMode(ElementDataDictCodeMode.DictCode).setDictCodeExpr("DemoPolitical");
//        dataForm.getElement("marital").getElementUIHint().setDictCodeMode(ElementDataDictCodeMode.DictCode).setDictCodeExpr("DemoMarital");
//        dataForm.getElement("educationLevel").getElementUIHint().setDictCodeMode(ElementDataDictCodeMode.DictCode).setDictCodeExpr("DemoEducationLevel");
//        dataForm.getElement("graduatedFrom").getElementUIHint().setNote("取得最高学历所在院校");
//
//        //联系信息
//        String group20 = "20:联系信息";
//        dataForm.addElement(new DataFormElement("domicilePlaceAddress","DOMICILE_PLACE_ADDRESS","户籍地址",group20));
//        dataForm.addElement(new DataFormElement("presentAddress","PRESENT_ADDRESS","居住地址",group20));
//        dataForm.addElement(new DataFormElement("mobilePhone","MOBILE_PHONE","手机号",group20));
//        dataForm.addElement(new DataFormElement("email","EMAIL","电子邮件",group20));
//        dataForm.getElement("domicilePlaceAddress").getElementUIHint().setColspan(2);
//        dataForm.getElement("presentAddress").getElementUIHint().setColspan(2);
//
//        //职业信息
//        String group30 = "30:职业信息";
//        dataForm.addElement(new DataFormElement("entryDate","ENTRY_DATE","入职年月",group30, ElementDataType.Date));
//        dataForm.addElement(new DataFormElement("workAs","WORK_AS","职业",group30));
//        dataForm.addElement(new DataFormElement("jobTitle","JOB_TITLE","职称",group30));
//        dataForm.addElement(new DataFormElement("companyIndustry","COMPANY_INDUSTRY","单位所属行业",group30));
//        dataForm.addElement(new DataFormElement("companyPostcode","COMPANY_POSTCODE","单位邮编",group30));
//        dataForm.addElement(new DataFormElement("companyAddress","COMPANY_ADDRESS","单位地址",group30));
//
//        dataForm.getElement("entryDate").getElementUIHint().setEditStyle(ElementDataEditStyle.YearMonthPicker);
//        dataForm.getElement("jobTitle").getElementUIHint().setEditStyle(ElementDataEditStyle.DictCodePicker).setDictCodeMode(ElementDataDictCodeMode.DictCode).setDictCodeExpr("DemoJobTitle");
//        dataForm.getElement("companyIndustry").getElementUIHint().setEditStyle(ElementDataEditStyle.DictCodePicker).setDictCodeMode(ElementDataDictCodeMode.DictCode).setDictCodeExpr("GBIndustry");
//        dataForm.getElement("companyAddress").getElementUIHint().setColspan(2);
//
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
//        dataForm.getElement("familyYearIncome").setMultiplier(10000d).getElementUIHint().setDataFormat(ElementDataFormat.Currency).setSuffix("万元");
//
//        //其他信息
//        String group50 = "50:其他信息";
//        dataForm.addElement(new DataFormElement("hobby","HOBBY","爱好",group50, ElementDataType.StringArray));
//        dataForm.addElement(new DataFormElement("remark","REMARK","备注",group50));
//        dataForm.addElement(new DataFormElement("status","STATUS","状态",group50));
//        dataForm.addElement(new DataFormElement("revision","REVISION","修改次数",group50, ElementDataType.Integer));
//        dataForm.getElement("hobby").getElementUIHint().setEditStyle(ElementDataEditStyle.CheckBox).setDictCodeMode(ElementDataDictCodeMode.DictCode).setDictCodeExpr("DemoHobby");
//        dataForm.getElement("remark").getElementUIHint().setEditStyle(ElementDataEditStyle.TextArea);
//
//        String group60 = "60:操作信息";
//        dataForm.addElement(new DataFormElement("createdBy","CREATED_BY","创建人",group60));
//        dataForm.addElement(new DataFormElement("createdTime","CREATED_TIME","创建时间",group60, ElementDataType.Date));
//        dataForm.addElement(new DataFormElement("updatedBy","UPDATED_BY","更新人",group60));
//        dataForm.addElement(new DataFormElement("updatedTime","UPDATED_TIME","更新时间",group60, ElementDataType.Date));
//
//        dataForm.addFilter(new DataFormFilter().setCode("code").setBindFor("code").setComparePattern(ElementFilterComparePattern.Contain).setQuick(true));       //编码
//        dataForm.addFilter(new DataFormFilter().setCode("chnName").setBindFor("chnName").setComparePattern(ElementFilterComparePattern.Contain).setQuick(true)); //中文名
//        dataForm.addFilter(new DataFormFilter().setCode("engName").setBindFor("engName").setComparePattern(ElementFilterComparePattern.Contain).setQuick(true)); //英文名
//        dataForm.addFilter(new DataFormFilter().setCode("gender").setBindFor("gender"));         //性别,默认为等于，且可以多选
//        dataForm.addFilter(new DataFormFilter().setCode("monthIncome").setBindFor("monthIncome").setComparePattern(ElementFilterComparePattern.Range)); //收入为区间
//        dataForm.addFilter(new DataFormFilter().setCode("status").setComparePattern(ElementFilterComparePattern.Equal));    //状态不绑定字段

        return dataForm;
    }

    @Test
    public void testFieldDataType() {
        Field[] fields = new Field[]{
                ClassKit.getField(Person.class, "id"),
                ClassKit.getField(Person.class, "birth"),
                ClassKit.getField(Person.class, "height"),
                ClassKit.getField(Person.class, "addresses"),
                ClassKit.getField(Person.class, "viewTimes"),
                ClassKit.getField(Person.class, "revision"),
        };

        for(Field field : fields){
            Class<?> typeClass = field.getType();
            if(Number.class.isAssignableFrom(typeClass)){
                if(Integer.class.isAssignableFrom(typeClass)||Long.class.isAssignableFrom(typeClass)){
                    System.out.println(field.getName()+"-->int");
                }else{
                    System.out.println(field.getName()+"-->double");
                }
            }else if(Date.class.isAssignableFrom(typeClass)){
                System.out.println(field.getName()+"-->date");
            }else if(Date.class.isAssignableFrom(typeClass)){
                System.out.println(field.getName()+"-->date");
            }else {
                System.out.println(field.getName()+"-->string");
            }
        }

    }
}
