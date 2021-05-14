package cn.fisok.sqloy.dao;

import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.NumberKit;
import cn.fisok.sqloy.BaseTest;
import cn.fisok.sqloy.entity.DemoPerson;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class DaoCruderTest extends BaseTest {
    @Autowired
    protected PersonDao personDao;
    @Test
    public void test01() throws Exception{
//        System.out.println("personDao:"+personDao);


//        Class<?> clazz = PersonDao.class;
//        Class<?> c = ResolvableType.forMethodReturnType(clazz.getMethod("getPersonList", String.class))
//                .getGeneric(0).getRawClass();
//        System.out.println("in-list:"+c.getName());
//
//        Class<?> c1 = ResolvableType.forMethodReturnType(clazz.getMethod("getPerson", String.class))
//                .getGeneric(0).getRawClass();
//        System.out.println("bean:"+c1);
    }

    @Test
    public void testGetPerson(){
        DemoPerson person = personDao.getPerson(17L);
        Assert.assertNotNull(person);
        Assert.assertEquals("安帅达",person.getName());
    }

    @Test
    public void testSelect(){
        List<DemoPerson> personList = personDao.getPersonList("1");
        for(int i=0;i<personList.size();i++){
            int idx = NumberKit.randomInt(0,personList.size());
            Assert.assertEquals("1",personList.get(idx).getGender());
        }
    }

    @Test
    public void testSelectAnno(){
        DemoPerson person1 = personDao.getPersonByCode("P1017");
        DemoPerson person2 = personDao.getPersonByName("安帅达");

        Assert.assertEquals(person1.getChnName(),"安帅达");
        Assert.assertEquals(person1.getName(),person2.getName());
    }

    @Test
    public void testInsert(){
        DemoPerson p = new DemoPerson();
        p.setCode("YS001");
        p.setName("张小小");

        //1.插入
        personDao.insertPerson(p);
        Assert.assertNotNull(p.getId());

        //2.保存
        p.setName("张小大");
        personDao.savePerson(p);
        p = personDao.getPerson(p.getId());
        Assert.assertEquals("张小大",p.getName());

        //更新
        personDao.updatePersonGraduatedFrom("南京大学",p.getId());
        p = personDao.getPerson(p.getId());
        Assert.assertEquals("南京大学",p.getGraduatedFrom());

        personDao.updatePersonHeight(187,p.getId());
        p = personDao.getPerson(p.getId());
        Assert.assertEquals(187,p.getHeight(),0.01);

        personDao.updatePersonBirth(DateKit.xparse("1987-01-30"),p.getId());
        p = personDao.getPerson(p.getId());
        Assert.assertEquals("1987-01-30",DateKit.format(p.getBirth(),"yyyy-MM-dd"));

        p.setHeight(85);
        personDao.updatePerson(p);
        p = personDao.getPerson(p.getId());
        Assert.assertEquals(85.0,p.getHeight(),0.001);

        //删除
        personDao.deletePerson(p.getId());
        p = personDao.getPerson(p.getId());
        Assert.assertNull(p);
    }
}
