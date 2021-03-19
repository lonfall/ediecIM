package com.lh.im.entity.msg.result;

import com.lh.im.entity.Group;
import com.lh.im.entity.Msg;

/**
 * @auther: loneyfall
 * @date: 2021/3/17
 * @description:
 */
public class MsgTextResult extends BaseMsgResult {
    private String msgResCode;
    private Group group;

    public MsgTextResult(String type) {
        super(type);
    }

    public String getMsgResCode() {
        return msgResCode;
    }

    public void setMsgResCode(String msgResCode) {
        this.msgResCode = msgResCode;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
