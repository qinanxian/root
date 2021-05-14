package com.vekai.workflow.model;

/**
 * 流程常量类
 * @author 左晓敏 <xmzuo@amarsoft.com>
 * @date 2017-12-26
 */
public class WorkflowConstant {
    public static final String SYSTEM_NAME = "SYSTEM";

    // Table相关
    public final static String ACT_RU_IDENTITY_LINK_TABLE = "ACT_RU_IDENTITYLINK";
    public final static String WKFL_PROC_TABLE = "WKFL_PROC";
    public final static String WKFL_CANDIDATE_TABLE = "WKFL_CANDIDATE";
    public final static String WKFL_COMMENT_TABLE = "WKFL_COMMENT";
    public final static String WKFL_TASK_TABLE = "WKFL_TASK";
    public final static String WKFL_TASK_HIST_TABLE = "WKFL_TASK_HIST";
    public final static String WKFL_COMMENT_HIST_TABLE = "WKFL_COMMENT_HIST";
    public final static String WKFL_SOLICIT_TABLE = "WKFL_SOLICIT";
    public final static String WKFL_SOLICIT_HIST_TABLE = "WKFL_SOLICIT_HIST";
    public final static String WKFL_TASK_TABLE_COL = "PROC_ID,TASK_ID,TASK_INST_ID,TASK_DEF_KEY,TASK_NAME,OWNER,ASSIGNEE,IS_CONCURRENT,ARRIVAL_TIME,FINISH_TIME,FINISH_TYPE,REVISION,CREATED_BY,CREATED_TIME,UPDATED_BY,UPDATED_TIME";
    public final static String WKFL_COMMENT_TABLE_COL = "PROC_ID,TASK_ID,COMMENT_ID,COMMENT_INST_ID,COMMENT_TYPE,MESSAGE,USER_ID,ACTION,ACTION_INTRO,REVISION,CREATED_BY,CREATED_TIME,UPDATED_BY,UPDATED_TIME";
    public final static String WKFL_SOLICIT_TABLE_COL = "PROC_ID,TASK_ID,SOLICIT_ID,SOLICIT_SUMMARY,SOLICITOR,SOLICIT_COMMENT,ASK_FOR,REPLY_COMMENT,REVISION,CREATED_BY,CREATED_TIME,UPDATED_BY,UPDATED_TIME";

}
