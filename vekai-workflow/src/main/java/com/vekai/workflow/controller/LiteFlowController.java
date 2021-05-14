package com.vekai.workflow.controller;

import cn.fisok.raw.kit.ValidateKit;
import com.vekai.auth.entity.User;
import com.vekai.auth.holder.AuthHolder;
import com.vekai.auth.service.AuthService;
import com.vekai.workflow.entity.WorkflowComment;
import com.vekai.workflow.entity.WorkflowProc;
import com.vekai.workflow.entity.WorkflowTask;
import com.vekai.workflow.exception.WorkflowException;
import com.vekai.workflow.liteflow.entity.LiteflowForm;
import com.vekai.workflow.liteflow.entity.LiteflowResource;
import com.vekai.workflow.liteflow.entity.LiteflowTemplate;
import com.vekai.workflow.liteflow.service.LiteFlowService;
import com.vekai.workflow.nopub.service.WorkflowEntityService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



/**
 * @Author: qyyao
 * @Description: 简式流程接口
 * @Date: Created in 19:04 14/03/2018
 */

@Api(description = "简式流程接口")
@Controller
@RequestMapping("/workflow/liteFlow")
public class LiteFlowController {
    protected Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private LiteFlowService liteFlowService;
    @Autowired
    private WorkflowEntityService workflowEntityService;
    @Autowired
    private AuthService authService;



    /**
     * 简式流程 启动
     *
     * @param expr
     * @param objectId
     * @param objectType
     * @param summary
     * @return
     */
    @RequestMapping(path = "/start", method = RequestMethod.POST)
    @ResponseBody
    public WorkflowProc start(@RequestParam("Expr") String expr,
                              @RequestParam("ProcDefKey") String procDefKey,
                              @RequestParam("ObjectId") String objectId,
                              @RequestParam("ObjectType") String objectType,
                              @RequestParam("Summary") String summary) {

        User loginUser = AuthHolder.getUser();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("ObjectId", objectId);
        variables.put("ObjectType", objectType);
        variables.put("Summary", summary);

        return liteFlowService.start(expr, procDefKey, variables, loginUser.getId());
    }


    /**
     * 简式流程 提交
     *
     * @param taskId
     * @param comment
     * @return
     */
    @RequestMapping(value = "/commit", method = RequestMethod.POST)
    @ResponseBody
    public List<WorkflowTask> commit(@RequestParam("taskId") String taskId,
                                     @RequestParam("comment") String comment) {
        Map<String, Object> variables = new HashMap<>();

        User user = AuthHolder.getUser();
        return liteFlowService.commit(taskId, variables, user.getId(), comment);
    }

    /**
     * 简式流程 否决
     *
     * @param taskId
     * @param comment
     */
    @RequestMapping(path = "/reject", method = RequestMethod.POST)
    @ResponseBody
    public void reject(@RequestParam("taskId") String taskId,
        @RequestParam("comment") String comment) {
        User user = AuthHolder.getUser();
        liteFlowService.reject(taskId, user.getId(), comment);
    }


    /**
     * 简式流程 废弃
     *
     * @param taskId
     * @param comment
     */
    @RequestMapping(path = "/abolish", method = RequestMethod.POST)
    @ResponseBody
    public void abolish(@RequestParam("taskId") String taskId,
        @RequestParam("comment") String comment) {
        User user = AuthHolder.getUser();
        liteFlowService.abolish(taskId, user.getId(), comment);
    }

    /**
     * 简式流程 驳回
     *
     * @param srcTaskId
     * @param dscTaskKey
     * @param userId
     * @param comment
     * @return
     */
    @PostMapping(path = "/backOrigin")
    @ResponseBody
    public WorkflowTask backOrigin(@RequestParam("srcTaskId") String srcTaskId,
        @RequestParam("dscTaskKey") String dscTaskKey,
        @RequestParam("userId") String userId,
        @RequestParam("comment") String comment) {
        return liteFlowService.backOrigin(srcTaskId, dscTaskKey, userId, comment);
    }

    /**
     * 获取简式流程实例
     *
     * @param taskId
     * @return
     */
    @RequestMapping(path = "/procInst/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public WorkflowProc getProcInst(@PathVariable String taskId) {
        ValidateKit.notBlank(taskId, "传入的参数taskId为空");
        WorkflowTask workflowTask = workflowEntityService.getWorkflowTask(taskId);
        if (null == workflowTask) {
            throw new WorkflowException("任务:{0}不存在或已被处理", taskId);
        }
        String procId = workflowTask.getProcId();

        WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(procId);
        if (null == workflowProc) {
            throw new WorkflowException("该procId:{0}对应的流程实例不存在", procId);
        }
        String sponsor = workflowProc.getSponsor();
        if (null != sponsor) {
            String userName = authService.getUser(sponsor).getName();
            workflowProc.setSponsorName(userName);
        }
        return workflowProc;
    }



    /**
     * 首个意见
     *
     * @param taskId
     * @return
     */
    @RequestMapping(path = "/firstComment/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public WorkflowComment firstComment(@PathVariable String taskId) {
        return liteFlowService.firstComment(taskId);
    }

    /**
     * 获取简式流程资源
     * procDefKey 具有唯一性
     *
     * @param procDefKey
     * @return
     */
    @GetMapping(path = "/resources/{procDefKey}")
    @ResponseBody
    public List<LiteflowResource> getResourceList(@PathVariable String procDefKey) {
        return liteFlowService.getResourceList(procDefKey);
    }



    /**
     * 获取流程审批单
     *
     * @return
     */
    @GetMapping(path = "/liteForm")
    @ResponseBody
    public List<LiteflowForm> getLiteForm() {
        return liteFlowService.getLiteForm();
    }



    /**
     * 流程资源分组
     *
     * @param procDefKey
     * @return
     */
    @GetMapping(path = "/resources/group/{procDefKey}")
    @ResponseBody
    public Map<String, List<LiteflowResource>> getResourcesGroup(@PathVariable String procDefKey) {
        List<LiteflowResource> resourceList = liteFlowService.getResourceList(procDefKey);
        return resourceList.stream().collect(Collectors.groupingBy(LiteflowResource::getType));
    }



    /**
     * 简式流程 表达式模板获取
     *
     * @param category
     * @return
     */
    @GetMapping(path = "/template/{category}")
    @ResponseBody
    public List<LiteflowTemplate> getTemplate(@PathVariable String category) {
        return liteFlowService.getTemplate(category);
    }


    /**
     * 新增预设模板
     * @param name
     * @param expr
     */
    @PostMapping(path = "/template")
    @ResponseBody
    public void saveTemplate(@RequestParam("name") String name, @RequestParam("expr") String expr) {
        liteFlowService.saveTemplate(name, expr);
    }


    /**
     * 更新预设模板
     * @param templateId
     * @param expr
     */
    @PutMapping(path = "/template")
    @ResponseBody
    public void updateTemplate(@RequestParam("templateId") String templateId,
        @RequestParam("expr") String expr) {
        liteFlowService.updateTemplate(templateId,expr);
    }



}
