package com.vekai.workflow.activiti.editor.model;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vekai.workflow.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: qyyao
 * @Description:
 * @Date: Created in 17:53 02/07/2018
 */
public class getModelJsonData  extends BaseTest{

    @Autowired
    private ModelEditorJsonRestResource restResource;


    @Test
    public void testJsonData(){
        String modelId ="3657562";
        ObjectNode editorJson = restResource.getEditorJson(modelId);
        Assert.assertNotNull(editorJson);
    }
}
