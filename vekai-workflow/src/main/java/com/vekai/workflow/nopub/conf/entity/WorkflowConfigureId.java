package com.vekai.workflow.nopub.conf.entity;

import javax.persistence.IdClass;
import java.io.Serializable;

/**
 * Created by apachechen on 2018/2/24.
 */
@IdClass(WorkflowConfigureId.class)
public class WorkflowConfigureId implements Serializable{
    String bomId;
    String procDefKey;
}
