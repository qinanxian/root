package com.vekai.workflow.model;

import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.task.TaskDefinition;

import java.util.*;

public class ProcTaskDefinition {
    private static final long serialVersionUID = 1L;
    protected String key;
    protected Expression nameExpression;
    protected Expression ownerExpression;
    protected Expression assigneeExpression;
    protected Set<Expression> candidateUserIdExpressions = new HashSet();
    protected Set<Expression> candidateGroupIdExpressions = new HashSet();

    private TaskDefinition taskDefinition;

    public ProcTaskDefinition(TaskDefinition taskDefinition) {
        this.taskDefinition = taskDefinition;
    }

    public String getKey() {
        return taskDefinition.getKey();
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Expression getNameExpression() {
        return taskDefinition.getNameExpression();
    }

    public void setNameExpression(Expression nameExpression) {
        this.nameExpression = nameExpression;
    }

    public Expression getOwnerExpression() {
        return taskDefinition.getOwnerExpression();
    }

    public void setOwnerExpression(Expression ownerExpression) {
        this.ownerExpression = ownerExpression;
    }

    public Expression getAssigneeExpression() {
        return taskDefinition.getAssigneeExpression();
    }

    public void setAssigneeExpression(Expression assigneeExpression) {
        this.assigneeExpression = assigneeExpression;
    }

    public Set<Expression> getCandidateUserIdExpressions() {
        return taskDefinition.getCandidateUserIdExpressions();
    }

    public void setCandidateUserIdExpressions(Set<Expression> candidateUserIdExpressions) {
        this.candidateUserIdExpressions = candidateUserIdExpressions;
    }

    public Set<Expression> getCandidateGroupIdExpressions() {
        return taskDefinition.getCandidateGroupIdExpressions();
    }

    public void setCandidateGroupIdExpressions(Set<Expression> candidateGroupIdExpressions) {
        this.candidateGroupIdExpressions = candidateGroupIdExpressions;
    }
}
