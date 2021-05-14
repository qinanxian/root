package com.vekai.workflow.activiti.command;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmException;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ScopeImpl;
import org.activiti.engine.impl.pvm.runtime.InterpretableExecution;

/**
 *
 * JD节点的跳转
 */
public class JDJumpTaskCmd implements Command<Void> {
    protected String executionId;
    protected ActivityImpl desActivity;
    protected String deleteReason; //删除理由
    protected Map<String, Object> paramvar;
    protected ActivityImpl currentActivity;

    public Void execute(CommandContext commandContext) {
        ExecutionEntityManager executionEntityManager = Context
                .getCommandContext().getExecutionEntityManager();
        // 获取当前流程的executionId，因为在并发的情况下executionId是唯一的。
        ExecutionEntity executionEntity = executionEntityManager
                .findExecutionById(executionId);
        
        executionEntity.setVariables(paramvar);
        executionEntity.setEventSource(this.currentActivity);
        executionEntity.setActivity(this.currentActivity);
        // 根据executionId 获取Task
        Iterator<TaskEntity> localIterator = Context.getCommandContext()
                .getTaskEntityManager().findTasksByExecutionId(this.executionId)
                .iterator();

        while (localIterator.hasNext()) {
            TaskEntity taskEntity = (TaskEntity) localIterator.next();
            // 触发任务监听
            taskEntity.fireEvent("complete");
            // 删除任务的原因--------------------------------------------------------------------------此处可标记删除任务的原因
            Context.getCommandContext().getTaskEntityManager()
                    .deleteTask(taskEntity, deleteReason, false);
        }

        //要激活交路径
        executionEntity.setActive(true);
        //触发事件监听器
        this.execute(executionEntity);
        InterpretableExecution propagatingExecution = null;
        if (this.desActivity.isScope()) {
            propagatingExecution = (InterpretableExecution) executionEntity.createExecution();
            executionEntity.setTransition(null);
            executionEntity.setActivity(null);
            executionEntity.setActive(false);
            propagatingExecution.initialize();

        }else {
            propagatingExecution = executionEntity;
        }


        propagatingExecution.executeActivity(this.desActivity);
        return null;
    }

    /*
     触发事件监听器
    */
    public void execute(InterpretableExecution execution) {
        ScopeImpl scope = getScope(execution);
        List<ExecutionListener> executionListeners = scope.getExecutionListeners(getEventName());
        for (ExecutionListener listener : executionListeners) {
            execution.setEventName(getEventName());
            execution.setEventSource(scope);
            try {
                listener.notify(execution);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new PvmException("couldn't execute event listener : " + e.getMessage(), e);
            }

        }
    }

    protected ScopeImpl getScope(InterpretableExecution execution) {
        return (ScopeImpl) execution.getActivity();
    }

    protected String getEventName() {
        return org.activiti.engine.impl.pvm.PvmEvent.EVENTNAME_END;
    }

    /**
     * 构造参数 可以根据自己的业务需要添加更多的字段
     * 
     * @param executionId
     * @param desActivity
     * @param paramvar
     * @param currentActivity
     */
    public JDJumpTaskCmd(String executionId, ActivityImpl desActivity,
            Map<String, Object> paramvar, ActivityImpl currentActivity, String deleteReason) {
        this.executionId = executionId;
        this.desActivity = desActivity;
        this.paramvar = paramvar;
        this.currentActivity = currentActivity;
        this.deleteReason = deleteReason;
    }
}