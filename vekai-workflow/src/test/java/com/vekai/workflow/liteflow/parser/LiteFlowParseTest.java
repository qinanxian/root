package com.vekai.workflow.liteflow.parser;


import com.vekai.workflow.exception.WorkflowException;
import com.vekai.workflow.liteflow.model.LiteTaskDef;
import com.vekai.workflow.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author: qyyao
 * @Description:
 * @Date: Created in 16:02 12/03/2018
 */
public class LiteFlowParseTest extends BaseTest{

    @Autowired
    private LiteFlowParse liteFlowParse;

    @Test
    public void testExprParse1(){
        String expression ="xmzuo->syang|qyyao|r:001->qyyao&r:001";
        List<LiteTaskDef> liteTaskDefList = liteFlowParse.expressionParse(expression);
        Assert.assertNotNull(liteTaskDefList);
    }




    @Test(expected = WorkflowException.class)
    public void testExprParse2(){
        String expression ="xmzuo->syang|r:001->qyyao&r:001|Test";
        List<LiteTaskDef> liteTaskDefList = liteFlowParse.expressionParse(expression);
        Assert.assertNotNull(liteTaskDefList);
    }


    @Test(expected = WorkflowException.class)
    public void testExprParse3(){
        String expression ="xmzuo->->syang|r:001->qyyao&r:001";
        List<LiteTaskDef> liteTaskDefList = liteFlowParse.expressionParse(expression);
        Assert.assertNotNull(liteTaskDefList);
    }


    @Test(expected = WorkflowException.class)
    public void testExprParse4(){
        String expression ="xmzuo-->syang|||r:001->qyyao&&&r:001";
        List<LiteTaskDef> liteTaskDefList = liteFlowParse.expressionParse(expression);
        Assert.assertNotNull(liteTaskDefList);
    }


    @Test(expected = WorkflowException.class)
    public void testExprParse5(){
        String expression ="xmzuo->syang|r:001->qyyao&&r:001";
        List<LiteTaskDef> liteTaskDefList = liteFlowParse.expressionParse(expression);
        Assert.assertNotNull(liteTaskDefList);
    }


    @Test
    public void testExprParse6(){
        String expression ="xmzuo->syang|r:001->";
        List<LiteTaskDef> liteTaskDefList = liteFlowParse.expressionParse(expression);
        Assert.assertNotNull(liteTaskDefList);
    }




    @Test
    public void testExprParse7(){
        String expression ="syang->cdzhang";
        List<LiteTaskDef> liteTaskDefList = liteFlowParse.expressionParse(expression);
        Assert.assertNotNull(liteTaskDefList);
    }

    //??????????????????????????????????????????
    @Test
    public void testExprParse8(){
        String expression ="syang|cdzhang|r:001->cdzhang&syang->xmzuo";
        List<LiteTaskDef> liteTaskDefList = liteFlowParse.expressionParse(expression);
        Assert.assertNotNull(liteTaskDefList);
    }


    @Test
    public void testExprParse9(){
        String expression ="?????????|?????????|????????????->?????????&?????????->??????&?????????";
        String newExpr = liteFlowParse.getExpression(expression);
        Assert.assertNotNull(newExpr);
    }

    @Test(expected = WorkflowException.class)
    public void testExprParse10(){
        String expression ="?????????|?????????||????????????->?????????&?????????->??????&?????????";
        String newExpr = liteFlowParse.getExpression(expression);
        Assert.assertNotNull(newExpr);
    }

    @Test
    public void testExprParse11(){
        String expression ="?????????&?????????&????????????->?????????&?????????->??????|?????????";
        String newExpr = liteFlowParse.getExpression(expression);
        Assert.assertNotNull(newExpr);
    }

    @Test(expected = WorkflowException.class)
    public void testExprParse12(){
        String expression ="?????????|?????????|????????????->->?????????&?????????->??????&?????????";
        String newExpr = liteFlowParse.getExpression(expression);
        Assert.assertNotNull(newExpr);
    }

    @Test(expected = WorkflowException.class)
    public void testExprParse13(){
        String expression ="?????????|?????????|->?????????&?????????->??????&?????????";
        String newExpr = liteFlowParse.getExpression(expression);
        Assert.assertNotNull(newExpr);
    }


    @Test
    public void test(){
        String str ="???????????????";
        String[] split = str.split("\\|");
        Assert.assertNotNull(split);
    }



}
