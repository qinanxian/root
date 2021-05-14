package com.vekai.workflow.activiti.command;

import com.vekai.workflow.model.enums.TaskStatusEnum;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

import java.io.Serializable;


/**
 * 任务删除命令
 * @author 左晓敏 <xmzuo@amarsoft.com>
 * @date 2016-12-22
 */
public class DeleteRunningTaskCmd implements Command<Void>, Serializable {
    private static final long serialVersionUID = -8120576486682403105L;
    
    private TaskEntity taskEntity;
	private TaskStatusEnum deleteReason;

	public DeleteRunningTaskCmd(TaskEntity taskEntity, TaskStatusEnum deleteReason) {
		this.taskEntity = taskEntity;
		this.deleteReason = deleteReason;
	}


	@Override
	public Void execute(CommandContext commandContext) {
		Context.getCommandContext().getTaskEntityManager().deleteTask(taskEntity, deleteReason.getName(), false);
		return null;
	}

}
