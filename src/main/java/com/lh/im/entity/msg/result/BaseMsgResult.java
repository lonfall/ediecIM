package com.lh.im.entity.msg.result;

/**
 * @auther: loneyfall
 * @date: 2021/3/12
 * @description:
 */
public class BaseMsgResult {
    private String type;
    private boolean success;

    public BaseMsgResult(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
