package com.vekai.workflow.liteflow.parser;



import com.vekai.auth.entity.Role;
import com.vekai.auth.entity.User;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.workflow.exception.WorkflowException;
import com.vekai.workflow.liteflow.model.LiteTaskDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @Author: qyyao
 * @Description: 简式流程表达式解析
 * @Date: Created in 15:00 09/03/2018
 */

@Component
public class LiteFlowParse {
    private final static String LITE_TASK_DEF_PREFIX = "T";
    private final static int EXPR_LENGTH = 2;

    private final static String AUTH_USER_TABLE = "AUTH_USER";
    private final static String AUTH_ROLE_TABLE = "AUTH_ROLE";

    @Autowired
    private BeanCruder beanCruder;


    /**
     * 反向解析
     * example: xmzuo->syang|r:001->qyyao&r:001
     *
     * @param expression
     * @return
     */
    public List<LiteTaskDef> expressionParse(String expression) {
        String[] split = ValidateExpr(expression);
        /**
         * xmzuo->syang|r:001->qyyao&r:001
         */

        List<LiteTaskDef> liteTaskDefList = new ArrayList<>();
        LiteTaskDef liteTaskDef = null;
        for (int i = 0; i < split.length; i++) {
            String str = split[i];
            liteTaskDef = new LiteTaskDef();
            liteTaskDef.setTaskName(str);
            liteTaskDef.setTaskDefKey(LITE_TASK_DEF_PREFIX + (i + 1));
            if (str.contains("|") && str.contains("&")) {
                throw new WorkflowException("该部分表达式: " + str + " 格式错误!,'&,|' 符号每个阶段只允许存在一种");
            }
            List<String> candidates = new ArrayList<>();
            if (str.contains("|")) {
                String[] split1 = str.split("\\|");
                for (String str1 : split1) {
                    if ("".equals(str1)) {
                        throw new WorkflowException("表达式格式错误: " + expression);
                    }

                }
                candidates.addAll(Arrays.asList(split1));
                liteTaskDef.setCandidates(candidates);
                liteTaskDef.setParallel(Boolean.FALSE);
            } else if (str.contains("&")) {
                String[] split2 = str.split("&");
                for (String str2 : split2) {
                    if ("".equals(str2)) {
                        throw new WorkflowException("表达式格式错误: " + expression);
                    }
                }
                candidates.addAll(Arrays.asList(split2));
                liteTaskDef.setCandidates(candidates);
                liteTaskDef.setParallel(Boolean.TRUE);
            } else {
                candidates.add(str);
                liteTaskDef.setCandidates(candidates);
                liteTaskDef.setParallel(Boolean.FALSE);
            }
            liteTaskDefList.add(liteTaskDef);
        }
        return liteTaskDefList;
    }



    /**
     * 正向解析表达式
     * example: 左晓敏|姚启扬|测试->吴继康&谢荣康->杨松&管理员
     * 问题1:遇到同名的如何处理
     * 问题2:初始阶段为并行任务(吴继康&谢荣康),无法判断哪个
     */
    public String getExpression(String expression) {
        String newExpr = expression;
        //初步校验输入的字符串格式
        String[] subStrList = ValidateExpr(expression);
        for (String subStr : subStrList) {
            //校验每一部分子串的格式
            if (subStr.contains("|") && subStr.contains("&")) {
                throw new WorkflowException("该表达式:{0}有误,请检查后重新填写", expression);
            }
            //查找用户/角色是否存在,并转换表达式
            if (subStr.contains("|")) {
                String[] split = subStr.split("\\|");
                newExpr = convertExpr(newExpr, split);
            }
            if (subStr.contains("&")) {
                String[] split = subStr.split("&");
                newExpr = convertExpr(newExpr, split);
            }
            if((!subStr.contains("|"))&&(!subStr.contains("&"))){
                String[] split =subStr.split("\\|");
                newExpr =convertExpr(newExpr,split);
            }

        }
        return newExpr;
    }

    private String convertExpr(String newExpr, String[] split) {
        for (String aSplit : split) {
            if ("".equals(aSplit)) {
                throw new WorkflowException("该部分表达式:{0}格式错误", aSplit);
            }
            User user = beanCruder.selectOne(User.class,
                "select ID,NAME from " + AUTH_USER_TABLE + " where NAME=:name",
                MapKit.mapOf("name", aSplit));
            Role role = beanCruder.selectOne(Role.class,
                "select ID,NAME from " + AUTH_ROLE_TABLE + " where NAME =:name",
                MapKit.mapOf("name", aSplit));
            if (null == user && null == role) {
                throw new WorkflowException("该用户/角色:{0}不存在", aSplit);
            }
            if(null!=user&&null!=role){
                throw new WorkflowException("用户名与部门名重复!",aSplit);
            }
            if (null != user) {
                newExpr = newExpr.replace(user.getName(), user.getId());
            }
            if (null != role) {
                String subRoleExpr = "r:" + role.getId();
                newExpr = newExpr.replace(role.getName(), subRoleExpr);
            }
        }
        return newExpr;
    }



    private String[] ValidateExpr(String expression) {
        if (null == expression || "".equals(expression)) {
            throw new WorkflowException("简式流程定义为空");
        }

        String[] split = expression.split("->");

        if (split.length < EXPR_LENGTH) {
            throw new WorkflowException("表达式:{0} 格式错误", expression);
        }

        for (String str : split) {
            if ("".equals(str)) {
                throw new WorkflowException("表达式格式错误:" + expression);
            }
            if (str.endsWith("|")||str.endsWith("&")){
                throw new WorkflowException("表达式格式错误:" +expression);
            }

            if (str.startsWith("|")||str.startsWith("&")){
                throw new WorkflowException("表达式格式错误:" +expression);
            }


            //用正则表达式对子串进行校验
            //            String sunStr ="->|&";
            //            Pattern pattern = Pattern.compile(sunStr);
            //            Matcher matcher = pattern.matcher(str);
            //            if (matcher.find()){
            //                throw new WorkflowException("该表达式:{0}格式错误",expression);
            //            }
        }
        return split;
    }
}