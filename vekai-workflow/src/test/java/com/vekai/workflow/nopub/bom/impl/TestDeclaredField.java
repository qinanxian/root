package com.vekai.workflow.nopub.bom.impl;

import com.vekai.workflow.handler.impl.WorkflowDBProcHandler;
import com.vekai.workflow.BaseTest;
import org.junit.Test;

import java.lang.reflect.Method;


public class TestDeclaredField  extends BaseTest {

    @Test
    public void test01(){
        WorkflowDBProcHandler workflowDBProcHandler = new WorkflowDBProcHandler();
        Class<? extends WorkflowDBProcHandler> aClass = workflowDBProcHandler.getClass();

        for (Method method : aClass.getDeclaredMethods()) {
            System.out.println(method.getName()+"---");
        }
        ;

        System.out.println("-------------------------------------------");
        Class asClass = new WorkflowHandlerTest01().getClass();
        for (Method method : asClass.getDeclaredMethods()) {
            System.out.println(method.getName()+"---");
        }
        ;
    }
}
