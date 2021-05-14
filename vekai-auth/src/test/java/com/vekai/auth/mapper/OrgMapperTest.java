package com.vekai.auth.mapper;//package com.vekai.auth.mapper;
//
//import com.baomidou.mybatisplus.mapper.EntityWrapper;
//import com.vekai.auth.BaseTest;
//import com.vekai.auth.entity.Org;
//import org.junit.Assert;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class OrgMapperTest extends BaseTest {
//    @Autowired
//    protected OrgMapper orgMapper;
//
//    @Test
//    public void existTest(){
//        Assert.assertNotNull(orgMapper);
//    }
//
//    @Test
//    public void saveTest(){
//        orgMapper.delete(new EntityWrapper<Org>()
//                .where("ID={0}", "0")
//        );
//        Org org = new Org();
//        org.setId("0");
//        org.setCode("HQ");
////        org.setSortCode("0");
//        org.setName("总部");
//        orgMapper.insert(org);
//    }
//}
