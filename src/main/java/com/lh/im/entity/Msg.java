package com.lh.im.entity;

import java.util.Date;

/**
 * @auther: loneyfall
 * @date: 2021/3/12
 * @description:
 */
public class Msg {
    private String userId;
    private String groupName;
    private String userName;
    private String content;
    private Date time;

    public Msg() {
        this.time = new Date();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
