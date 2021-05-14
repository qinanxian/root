package com.vekai.workflow.nopub;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.workflow.BaseTest;
import com.vekai.workflow.entity.WorkflowCandidate;
import com.vekai.workflow.nopub.service.WorkflowEntityService;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WorkflowCandidateTest extends BaseTest {
    @Autowired
    private BeanCruder beanCruder;
    @Autowired
    private WorkflowEntityService workflowEntityService;

    @Test
    public void test01AddWorkflowCandidate(){
        WorkflowCandidate workflowCandidate = new WorkflowCandidate();
        workflowCandidate.setCandidateId("1");
        workflowCandidate.setTaskId("111");
        workflowCandidate.setCandidateType("type1");
        workflowCandidate.setUserId("1111");
        workflowCandidate.setSourceType("sourceType1");
        workflowCandidate.setScopeType("scopeType1");
        workflowCandidate.setScopeRoleId("2222");
        workflowCandidate.setScopeOrgId("3333");
        workflowCandidate.setScopeTeamId("4444");

        workflowEntityService.addWorkflowCandidate(workflowCandidate);
        WorkflowCandidate workflowCandidateTest = beanCruder
                .selectOne(WorkflowCandidate.class,"select * from WKFL_CANDIDATE where CANDIDATE_ID='1'");
        Assert.assertEquals("type1",workflowCandidateTest.getCandidateType());
    }

    @Test
    public void test02GetWorkflowCandidate(){
        WorkflowCandidate workflowCandidate = workflowEntityService.getWorkflowCandidate("1");
        Assert.assertNotNull(workflowCandidate);
        Assert.assertEquals("type1",workflowCandidate.getCandidateType());
    }

    @Test
    public void test03GetWorkflowCandidateByTaskId(){
        WorkflowCandidate workflowCandidate = workflowEntityService.getWorkflowCandidateByTaskIdAndUserId("111","001");
        Assert.assertNotNull(workflowCandidate);
        Assert.assertEquals("type1",workflowCandidate.getCandidateType());
    }

    @Test
    public void test04UpdateWorkflowCandidate(){
        WorkflowCandidate workflowCandidate = workflowEntityService.getWorkflowCandidate("1");
        workflowCandidate.setCandidateType("type2");
        workflowEntityService.updateWorkflowCandidate(workflowCandidate);

        WorkflowCandidate workflowCandidateTest = workflowEntityService.getWorkflowCandidate("1");
        Assert.assertEquals("type2",workflowCandidateTest.getCandidateType());
    }

    @Test
    public void test05DeleteWorkflowCandidate(){
        workflowEntityService.deleteWorkflowCandidate("1");
        WorkflowCandidate workflowCandidate = beanCruder
                .selectOne(WorkflowCandidate.class,"select * from WKFL_CANDIDATE where CANDIDATE_ID='1'");
        Assert.assertNull(workflowCandidate);
    }
}
