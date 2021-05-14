package com.vekai.workflow.controller;

import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.sqloy.autoconfigure.SqloyProperties;
import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.core.DBType;
import com.vekai.auth.entity.User;
import com.vekai.auth.holder.AuthHolder;
import com.vekai.workflow.entity.WorkflowCommonComment;
import com.vekai.workflow.exception.WorkflowException;
import com.vekai.workflow.model.HistoricInstance;
import com.vekai.workflow.model.HistoricTask;
import com.vekai.workflow.model.Role;
import com.vekai.workflow.nopub.conf.entity.WorkflowResourceConstant;
import com.vekai.workflow.nopub.kit.DateTools;
import com.vekai.workflow.nopub.resource.WorkflowResourceProcessor;
import com.vekai.workflow.nopub.resource.model.WorkflowResource;
import com.vekai.workflow.nopub.resource.model.WorkflowResources;
import com.vekai.workflow.nopub.resource.model.enums.WorkflowResourceRight;
import com.vekai.workflow.service.WorkflowProcService;
import com.vekai.workflow.service.WorkflowTaskService;
import io.swagger.annotations.Api;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Api(description = "流程资源接口")
@Controller
@RequestMapping("/workflow/resource")
public class WorkflowResourceController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected WorkflowProcService workflowProcService;
    @Autowired
    protected WorkflowTaskService workflowTaskService;
    @Autowired
    private WorkflowResourceProcessor resourceProcessor;
    @Autowired
    private BeanCruder beanCruder;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DateTools dateTools;
    @Autowired
    private SqloyProperties sqloyProperties;

    private final static String WKFL_CONF_RESOURCE_TABLE = "WKFL_CONF_RESOURCE";
    private final static String AUTH_ROLE_TABLE = "AUTH_ROLE";
    private static final String WKFL_COMMON_COMMENT_TABLE ="WKFL_COMMON_COMMENT";


    /**
     * 根据流程KEY获取流程资源集
     */
    @RequestMapping(value = "/workflowResource/{procDefKey}", method = RequestMethod.GET)
    @ResponseBody
    public List<WorkflowResource> findResource(@PathVariable("procDefKey") String procDefKey) {
        ValidateKit.notNull(procDefKey, "传入的参数procDefKey为空!");
        List<WorkflowResource> resourceList = beanCruder
            .selectList(WorkflowResource.class,
                "SELECT RESOURCE_ID as id,NAME,PROC_DEF_KEY,TYPE_ as type,SORT_CODE,ACTION as "
                    + "action,ACTION_HINT,ICON_ as icon,STYLE_ as style,RESOURCE_LOCATION FROM "
                    + WKFL_CONF_RESOURCE_TABLE
                    + " where PROC_DEF_KEY=:procDefKey order by SORT_CODE",
                MapKit.mapOf("procDefKey", procDefKey));

        return resourceList;
    }

    @RequestMapping(path = "/taskResource/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public WorkflowResources taskResource(@PathVariable("taskId") String taskId) {
        List<FormProperty> taskFormProperties =
            workflowTaskService.getTaskFormProperties(taskId);
        Task task = workflowTaskService.getTask(taskId);
        ValidateKit.notNull(task,"TaskId:{0}对应的任务不存在", taskId);
        HistoricInstance histInst =
            workflowProcService.getHistInst(task.getProcessInstanceId());
        List<WorkflowResource> taskResources = resourceProcessor
            .getTaskResources(histInst.getProcessDefinitionKey(), taskFormProperties);
        return this.resourceGroup(taskResources);
    }

    @RequestMapping(path = "/histTaskResource/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public WorkflowResources histTaskResource(@PathVariable("taskId") String taskId) {
        List<FormProperty> taskFormProperties =
            workflowTaskService.getHistTaskFormProperties(taskId);
        HistoricTask histTask = workflowTaskService.getHistTask(taskId);
        ValidateKit.notNull(histTask,"TaskId:{0}对应的历史任务不存在", taskId);
        HistoricInstance histInst =
            workflowProcService.getHistInst(histTask.getProcessInstanceId());
        List<WorkflowResource> taskResources = resourceProcessor
            .getTaskResources(histInst.getProcessDefinitionKey(), taskFormProperties);
        return this.resourceGroup(taskResources);
    }

    /**
     * 获取流程实例资源
     * 要求开发人员在流程发起时传入流程实例资源所需的参数,否则有可能出现资源打开找不到参数的情况
     *
     * @param procInstId
     * @return
     */
    @RequestMapping(path = "/procInstResource/{procInstId}", method = RequestMethod.GET)
    @ResponseBody
    public WorkflowResources procInstResource(@PathVariable("procInstId") String procInstId) {
        Optional.ofNullable(procInstId).orElseThrow(() -> new WorkflowException("传入流程实例号为空"));
        HistoricInstance histInst =
            workflowProcService.getHistInst(procInstId);
        Optional.ofNullable(histInst)
            .orElseThrow(() -> new WorkflowException("流程实例:{}不存在", procInstId));
        List<WorkflowResource> taskResources = resourceProcessor
            .getProcInstResources(histInst.getProcessDefinitionKey());
        taskResources.forEach(resource -> resource.setRight(WorkflowResourceRight.Readonly));
        return this.resourceGroup(taskResources);
        //return this.getResourceGroup(taskResources);
    }



    //    private Map<String,List<WorkflowResource>> getResourceGroup(List<WorkflowResource>
    // workflowResourceList){
    //        return workflowResourceList.stream().collect(Collectors.groupingBy
    // (WorkflowResource::getType));
    //    }

    private WorkflowResources resourceGroup(List<WorkflowResource> workflowTaskResources) {

        WorkflowResources workflowResources = new WorkflowResources();

        List<WorkflowResource> buttonList = new ArrayList();
        List<WorkflowResource> linkList = new ArrayList();
        List<WorkflowResource> fieldSetList = new ArrayList();
        for (WorkflowResource workflowTaskResource : workflowTaskResources) {
            String type = workflowTaskResource.getType();
            if (WorkflowResourceConstant.BUTTON.equals(type)) {
                buttonList.add(workflowTaskResource);
            }
        }
        workflowResources.setButton(buttonList);

        for (WorkflowResource workflowTaskResource : workflowTaskResources) {
            String type = workflowTaskResource.getType();
            if (WorkflowResourceConstant.LINK.equals(type)) {
                linkList.add(workflowTaskResource);
            }
        }
        workflowResources.setLink(linkList);

        for (WorkflowResource workflowTaskResource : workflowTaskResources) {
            String type = workflowTaskResource.getType();
            if (WorkflowResourceConstant.FIELD_SET.equals(type)) {
                fieldSetList.add(workflowTaskResource);
            }
        }
        workflowResources.setFieldSet(fieldSetList);
        return workflowResources;
    }

    /**
     * 初始化流程资源
     */

    @RequestMapping(value = "initResource/{procDefKey}", method = RequestMethod.POST)
    @ResponseBody
    public void initResource(@PathVariable("procDefKey") String procDefKey) {
        ValidateKit.notNull(procDefKey, "传入的参数procDefKey为空!");
        String baseKey = "Simple";

        Integer count = beanCruder.selectCount("select count(1) from " + WKFL_CONF_RESOURCE_TABLE + " where PROC_DEF_KEY = :key", "key", baseKey);
        if(count<1) throw new WorkflowException("初始化流程资源为空");

        beanCruder.execute(
            "INSERT INTO " + WKFL_CONF_RESOURCE_TABLE
                + " ( RESOURCE_ID , PROC_DEF_KEY , SORT_CODE , TYPE_ , "
                + "NAME , ACTION ,ACTION_HINT, ICON_ , STYLE_ , IS_EXPANDED , IS_INST_RESOURCE , "
                + "REVISION , "
                + "CREATED_BY , CREATED_TIME , "
                + "UPDATED_BY , UPDATED_TIME,RESOURCE_LOCATION) SELECT RESOURCE_ID ,'" + procDefKey
                + "' AS PROC_DEF_KEY ,"
                + " SORT_CODE , TYPE_ , NAME , ACTION ,ACTION_HINT, ICON_ , STYLE_ , IS_EXPANDED , "
                + "IS_INST_RESOURCE , REVISION , CREATED_BY , "
                + "CREATED_TIME , UPDATED_BY , UPDATED_TIME,RESOURCE_LOCATION FROM " + WKFL_CONF_RESOURCE_TABLE
                + " WHERE "
                + "PROC_DEF_KEY =:Simple",
            MapKit.mapOf("procDefKey", procDefKey, "Simple", baseKey));
    }

    /**
     * 查看流程候选人/角色
     *
     * @return
     */
    @RequestMapping(value = "workflowRoles", method = RequestMethod.GET)
    @ResponseBody
    public List findAllCandidateRole() {
        String inWorkFlow = "Y";
        List roleList = beanCruder.selectList(Role.class,
            "select ID,NAME,STATUS,TYPE from " + AUTH_ROLE_TABLE
                + " where IN_WORK_FLOW =:inWorkFlow",
            MapKit.mapOf("inWorkFlow", inWorkFlow));
        return roleList;
    }


    @GetMapping("/comment/list")
    @ResponseBody
    public List<WorkflowCommonComment> getAllComment(){
        return beanCruder.selectList(WorkflowCommonComment.class,
                "select * from " + WKFL_COMMON_COMMENT_TABLE + " where 1=1");
    }


    /**
     * dashBoard 流程相关
     */
    @GetMapping(value = "/dashboard")
    @ResponseBody
    public void getWorkflowForDashBoard(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        request.setCharacterEncoding("UTF-8");
        JSONObject json = new JSONObject();
        User user = AuthHolder.getUser();
        if (null == user) {
            return;
        }

        //sql 后期可以考虑移植到专门的sql文本文件中 采用读取sql文件的方式

        //待办任务数量
        int selectCount = beanCruder.selectCount(
            "SELECT WP.PROC_ID, WT.TASK_ID,WT.TASK_NAME FROM WKFL_TASK WT, " + " WKFL_PROC WP "
                + " WHERE WT.PROC_ID = WP.PROC_ID "
                + " AND WT.FINISH_TYPE IS NULL AND(WT.OWNER = :CUR_USER OR WT.ASSIGNEE = :"
                + "CUR_USER OR(WT.ASSIGNEE IS NULL "
                + " AND WT.TASK_ID IN ("
                + " SELECT " + " WT.TASK_ID " + " FROM " + " WKFL_CANDIDATE WC,"
                + " AUTH_USER_ROLE AUR " + " WHERE " + " WT.TASK_ID = WC.TASK_ID "
                + " AND( " + " WC.USER_ID = :CUR_USER " + " OR( "
                + " WC.SCOPE_ROLE_ID = AUR.ROLE_ID " + " AND AUR.USER_ID = :CUR_USER )))))",
            MapKit.mapOf("CUR_USER", user.getId()));
        json.put("pendingTaskCount", selectCount);
        //获取系统连接的数据库类型
        DBType dbType = beanCruder.getDBType();
//        SqlDialectType sqlDialectType = sqlProperties.getSqlDialectType();
        if (dbType == DBType.ORACLE) {
            //如果数据库类型是oracle
            getSqlForOracle(json, user);
        } else if (dbType == DBType.MYSQL) {
            //如果数据库类型是 mysql
            getSqlForMysql(json, user);
        }
        response.getWriter().print(json);

    }

    private void getSqlForOracle(JSONObject json, User user) throws JSONException {
        //本周已完成任务数量
        //oracle 数据库支持 mysql 不支持 待解决
        Integer selectCount1 = beanCruder.selectCount(
            "SELECT * from WKFL_TASK_ALL WHERE  TO_CHAR(FINISH_TIME,'iw')=to_char(sysdate,'iw') "
                + "and ASSIGNEE = :CUR_USER",
            MapKit.mapOf("CUR_USER", user.getId()));
        json.put("completedTaskCount", selectCount1.toString());

        //本周处理任务平均时间
        //使用jdbcTemplate 查询返回结果
        //oracle 数据库支持 mysql 不支持 待解决
        int averageTimeOfTask = findAverageTimeOfByOracle(user.getId());
        json.put("averageTimeOfTask", averageTimeOfTask);
    }


    //为mysql 服务
    private void getSqlForMysql(JSONObject json, User user) throws JSONException {

        //本周已完成任务数量

        Date mondayOfThisWeek = DateKit.getMondayOfWeek(DateKit.now());
        Date sundayOfThisWeek = DateKit.getSundayOfWeek(DateKit.now());
        String partSql =
            "SELECT * from WKFL_TASK_ALL WHERE FINISH_TIME BETWEEN :startTime AND :endTime AND  "
                + " ASSIGNEE=:CUR_USER ";
        int selectCount1 =
            beanCruder.selectCount(partSql, MapKit
                .mapOf("startTime", mondayOfThisWeek, "endTime", sundayOfThisWeek, "CUR_USER",
                    user.getId()));
        json.put("completedTaskCount", selectCount1);

        //本周处理任务平均时间
        int averageTimeOfTask =
            findAverageTimeByMysql(mondayOfThisWeek, sundayOfThisWeek, user.getId());
        json.put("averageTimeOfTask", averageTimeOfTask);
    }

    private Integer findAverageTimeOfByOracle(String userId) {
        String sql =
            String.format(
                " WITH TOTALMINUTES AS (SELECT SUM(MINUTES) AS MINUTES FROM(SELECT ROUND"
                    + "(TO_NUMBER(FINISH_TIME - ARRIVAL_TIME) * 24 * 60) AS MINUTES   FROM(SELECT"
                    + " * FROM WKFL_TASK_ALL WHERE  TO_CHAR(FINISH_TIME,'IW')= TO_CHAR(SYSDATE,"
                    + "'IW')  AND ASSIGNEE = ? ))),TOTALNUMTASK AS (SELECT COUNT(*) AS "
                    + "NUMBERTASKS FROM WKFL_TASK_ALL WHERE  TO_CHAR(FINISH_TIME,'IW')=TO_CHAR"
                    + "(SYSDATE,'IW') AND ASSIGNEE = ? ) SELECT NVL(T1.MINUTES / T2.NUMBERTASKS, "
                    + "0) FROM TOTALMINUTES T1,TOTALNUMTASK T2");

        return jdbcTemplate.queryForObject(sql, new Object[] {userId, userId}, Integer.class);
    }


    private Integer findAverageTimeByMysql(Date mondayOfThisWeek, Date sundayOfThisWeek,
        String userId) {
        String partSql =
            "SELECT IFNULL((( SELECT SUM(minutes) AS totalMinutes FROM( SELECT  ROUND(( "
                + "UNIX_TIMESTAMP(FINISH_TIME) - UNIX_TIMESTAMP(ARRIVAL_TIME)) / 60) AS minutes "
                + "FROM( SELECT * FROM WKFL_TASK_ALL WHERE FINISH_TIME BETWEEN  ? AND ? AND "
                + "ASSIGNEE = ?) AS aa)  AS bb) /( SELECT COUNT(*) FROM WKFL_TASK_ALL WHERE "
                + "FINISH_TIME BETWEEN ? AND ? AND ASSIGNEE = ?)), 0)";
        return jdbcTemplate.queryForObject(partSql,
            new Object[] {mondayOfThisWeek, sundayOfThisWeek, userId, mondayOfThisWeek,
                sundayOfThisWeek, userId}, Integer.class);
    }
}
