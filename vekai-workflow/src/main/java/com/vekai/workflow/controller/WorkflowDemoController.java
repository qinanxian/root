package com.vekai.workflow.controller;

import com.vekai.auth.entity.User;
import com.vekai.auth.holder.AuthHolder;
import cn.fisok.raw.kit.DateKit;
import com.vekai.workflow.entity.WorkflowProc;
import com.vekai.workflow.entity.WorkflowTask;
import com.vekai.workflow.liteflow.service.LiteFlowService;
import com.vekai.workflow.model.ProcInstance;
import com.vekai.workflow.nopub.service.WorkflowEntityService;
import com.vekai.workflow.service.WorkflowProcService;
import com.vekai.workflow.service.WorkflowTaskService;
import io.swagger.annotations.Api;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "流程演示接口")
@Controller
@RequestMapping("/workflow/demo")
public class WorkflowDemoController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected WorkflowProcService workflowProcService;
    @Autowired
    protected WorkflowTaskService workflowTaskService;
    @Autowired
    private WorkflowEntityService workflowEntityService;
    @Autowired
    private LiteFlowService liteFlowService;
    @Autowired
    private RepositoryService repositoryService;


    @RequestMapping(value = "/model/start/{modelId}", method = RequestMethod.GET)
    @ResponseBody
    public ProcInstance start(@PathVariable String modelId) {
        String userId = AuthHolder.getUser().getId();

        Map<String, Object> map = new HashMap<>();

        ProcInstance procInstance = this.workflowStart(modelId);
        return procInstance;
    }


    private ProcInstance workflowStart(String modelId) {
        User user = AuthHolder.getUser();
        Model model = repositoryService.getModel(modelId);
        Map<String, Object> variables = new HashMap<>();
        variables.put("ObjectId", "10010");
        variables.put("ObjectType", "demo");
        variables.put("Summary", "测试流程-"+ DateKit.now().toString());
        ProcInstance procInstance = workflowProcService.start(model.getKey(), variables, user.getId());
        return procInstance;
    }


    /**
     * 简式流程 启动 + 提交 demo
     * @param expr
     * @param comment
     * @param summary
     * @return
     */

    @RequestMapping(path = "/startAndCommitDemo", method = RequestMethod.POST)
    @ResponseBody
    public Object start(@RequestParam("expr") String expr,
        @RequestParam("comment") String comment, @RequestParam("summary") String summary) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("ObjectId", "10010");
        variables.put("ObjectType", "测试类型");
        variables.put("Summary", summary);

        User user = AuthHolder.getUser();
        if (null == user) {
            throw new RuntimeException("请重新登录用户");
        }

        String userId = user.getId();

        Map<String,Object> map = new HashMap<>();
        WorkflowProc workflowProc =null;
        String procDefKey ="简式流程测试";
        try {
            workflowProc=liteFlowService.start(expr,procDefKey, variables, userId);
            if (null!=workflowProc){
                map.put("result","简式流程启动成功");
                logger.info("简式流程启动成功");
            } else {
                map.put("result", "简式流程启动失败");
                logger.error("简式流程启动失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        List<WorkflowTask> workflowTaskList = null;
        if (workflowProc != null) {
            workflowTaskList =
                workflowEntityService.getWorkflowTaskByProcId(workflowProc.getProcId());
            for (WorkflowTask workflowTask : workflowTaskList) {
                String taskId = workflowTask.getTaskId();
                try {

                    liteFlowService.commit(taskId, variables, userId, comment);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return map;
    }
}
