package com.lh.im.entity.msg.result;

/**
 * @auther: loneyfall
 * @date: 2021/3/17
 * @description:
 */
public class RegisterResult extends BaseMsgResult {
    private String userId;

    public RegisterResult(String type) {
        super(type);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
