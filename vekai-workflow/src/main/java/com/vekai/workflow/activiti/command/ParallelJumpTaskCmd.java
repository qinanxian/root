package com.vekai.workflow.activiti.command;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author: qyyao
 * @Description:
 * @Date: Created in 15:04 2019/3/20
 */
public class ParallelJumpTaskCmd implements Command<Void> {

    protected String executionId;  //执行实例ID
    protected String parentId;    //流程实例Id
    protected ActivityImpl desActivity; //目标节点
    protected Map<String,Object> paramvar; //变量
    protected ActivityImpl currentActivity; //当前节点
    protected String deleteReason;


    public ParallelJumpTaskCmd(String executionId, String parentId,
                               ActivityImpl desActivity, Map<String, Object> paramvar,
                               ActivityImpl currentActivity, String deleteReason) {
        this.executionId = executionId;
        this.parentId = parentId;
        this.desActivity = desActivity;
        this.paramvar = paramvar;
        this.currentActivity = currentActivity;
        this.deleteReason = deleteReason;
    }

    @Override
    public Void execute(CommandContext commandContext) {

        ExecutionEntityManager executionEntityManager =
            Context.getCommandContext().getExecutionEntityManager();
        ExecutionEntity executionEntity = executionEntityManager.findExecutionById(executionId);
        String id = null;
        if (executionEntity.getParent()!= null) {
            executionEntity = executionEntity.getParent();
            if (executionEntity.getParent() != null) {
                executionEntity = executionEntity.getParent();
                id = executionEntity.getId();
            } else {
                id = executionEntity.getId();
            }

        }

        executionEntity.setVariables(paramvar);
        executionEntity.setEventSource(this.currentActivity);
        executionEntity.setActivity(this.currentActivity);

        Iterator<TaskEntity> localIterator = Context.getCommandContext().getTaskEntityManager()
            .findTasksByExecutionId(this.executionId).iterator();
        while (localIterator.hasNext()){
            TaskEntity taskEntity = (TaskEntity) localIterator.next();
            taskEntity.fireEvent("complete");
            Context.getCommandContext().getTaskEntityManager().deleteTask(taskEntity,deleteReason,false);
        }

        List<ExecutionEntity> list =
            executionEntityManager.findChildExecutionsByParentExecutionId(parentId);
        for (ExecutionEntity ee:list){
            ee.remove();
        }

        executionEntity.executeActivity(this.desActivity);
        return null;
    }



}
