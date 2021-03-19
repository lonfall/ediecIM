package com.lh.im.entity.msg.result;

/**
 * @auther: loneyfall
 * @date: 2021/3/12
 * @description:
 */
public class CreateGroupMsgResult extends BaseMsgResult {
    private String groupName;

    public CreateGroupMsgResult(String type) {
        super(type);
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
