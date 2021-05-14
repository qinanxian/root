package com.vekai.workflow.controller.workflowDesignController;

import com.vekai.workflow.controller.WorkflowDesignController;
import com.vekai.workflow.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: qyyao
 * @Description:
 * @Date: Created in 17:33 11/04/2018
 */
public class BatchDownloadBpmnFilesTest extends BaseTest{

    @Autowired
    private WorkflowDesignController designController;



    @Test
    public void testDownload(){
        String[] models = {"822501","3077501"};
        HttpServletResponse response =new MockHttpServletResponse();
        designController.exportBatch(models,response);
    }
}
