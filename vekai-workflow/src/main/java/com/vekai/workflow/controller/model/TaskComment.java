package com.vekai.workflow.controller.model;

import java.util.Date;

/**
 * @Author: qyyao
 * @Description:
 * @Date: Created in 17:20 18/05/2018
 */
public class TaskComment {
    private String commentId;
    private String type;
    private String userName;
    private String content;
    private Date createdTime;


    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getType() {
        return type;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }



    @Override
    public String toString() {
        return "TaskComment{" +
            "commentId='" + commentId + '\'' +
            ", type='" + type + '\'' +
            ", userName='" + userName + '\'' +
            ", content='" + content + '\'' +
            ", createdTime=" + createdTime +
            '}';
    }
}
