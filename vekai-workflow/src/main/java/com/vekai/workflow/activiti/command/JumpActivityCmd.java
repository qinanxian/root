package com.vekai.workflow.activiti.command;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;

/**
 * @Author: qyyao
 * @Description:
 * @Date: Created in 16:02 2019/2/18
 */
//任意节点跳转
public class JumpActivityCmd implements Command<ExecutionEntity> {

    private String activityId;
    private String processInstanceId;
    public JumpActivityCmd(String processInstanceId,String activityId){
        this.processInstanceId=processInstanceId;
        this.activityId=activityId;
    }
    @Override
    public ExecutionEntity execute(CommandContext commandContext) {

        ExecutionEntity executionEntity =
            commandContext.getExecutionEntityManager().findExecutionById(processInstanceId);
        executionEntity.destroyScope("jump");
        ProcessDefinitionImpl processDefinition = executionEntity.getProcessDefinition();

        ActivityImpl activity = processDefinition.findActivity(activityId);

        executionEntity.executeActivity(activity);
        return executionEntity;
    }
}
