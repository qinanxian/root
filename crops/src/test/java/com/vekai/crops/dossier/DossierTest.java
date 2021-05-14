//package com.vekai.crops.dossier;
//
//import com.vekai.crops.BaseTest;
//import com.vekai.appframe.conf.dossier.dao.ConfDossierDao;
//import com.vekai.appframe.conf.dossier.model.ConfDossier;
//import org.junit.Assert;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class DossierTest  extends BaseTest {
//    @Autowired
//    private ConfDossierDao confDossierDao;
//
//    @Test
//    public void testNotNull(){
//        Assert.assertNotNull(confDossierDao);
//    }
//
//    @Test
//    public void testQuery(){
//        ConfDossier confDossier = confDossierDao.queryConfDossier("Biz0007");
//        Assert.assertEquals("Biz0007",confDossier.getDossierDefKey());
//    }
//}
