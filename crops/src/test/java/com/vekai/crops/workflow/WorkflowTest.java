//package com.vekai.crops.workflow;
//
//import com.vekai.crops.BaseTest;
//import com.vekai.appframe.conf.dossier.dao.ConfDossierDao;
//import com.vekai.appframe.conf.dossier.model.ConfDossier;
//import com.vekai.workflow.model.ProcTask;
//import com.vekai.workflow.service.WorkflowTaskService;
//import org.junit.Assert;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.HashMap;
//import java.util.List;
//
//public class WorkflowTest extends BaseTest {
//   @Autowired
//   private WorkflowTaskService workflowTaskService;
//
//    @Test
//    public void test01(){
//        List<ProcTask> tasks = workflowTaskService.commit("80008", new HashMap<>(), "xji", "测试");
//        System.out.println(tasks);
//    }
//
//    @Test
//    public void test02(){
//        List<String> users = workflowTaskService.getCandidateGroupUsers("82503");
//        System.out.println(users);
//    }
//
//    @Test
//    public void test03(){
//        workflowTaskService.claimTask("82503", "bliu");
//        List<String> users = workflowTaskService.getCandidateGroupUsers("82503");
//        System.out.println(users);
//    }
//
//    @Test
//    public void test04(){
//        workflowTaskService.unClaimTask("82503");
//        List<String> users = workflowTaskService.getCandidateGroupUsers("82503");
//        System.out.println(users);
//    }
//}
