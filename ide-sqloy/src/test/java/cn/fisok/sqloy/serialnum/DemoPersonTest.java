package cn.fisok.sqloy.serialnum;

import cn.fisok.sqloy.BaseTest;
import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.entity.DemoPerson;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class DemoPersonTest extends BaseTest {

    @Autowired
    BeanCruder accessor;

    @Test
    public void testGenerateSerialNo(){
        DemoPerson person=new DemoPerson();
        person.setCode("P2001");
        person.setName("DEMO");
        person.setChnName("测试客户");
        person.setGender("2");
        accessor.save(person);

        System.out.println("id="+person.getId());
    }


    @Test
    public void testGenerateSerialNos(){
        List<DemoPerson> personList=new ArrayList<>();
        DemoPerson person=new DemoPerson();
        person.setCode("P2020");
        person.setName("DEMO2020");
        person.setChnName("测试客户2020");
        person.setGender("2");
        personList.add(person);

        person=new DemoPerson();
        person.setCode("P2021");
        person.setName("DEMO2021");
        person.setChnName("测试客户2021");
        person.setGender("2");
        personList.add(person);

        accessor.save(personList);
    }
}
