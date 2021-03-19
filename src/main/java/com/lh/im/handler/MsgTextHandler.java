package com.lh.im.handler;

import com.lh.im.config.Constant;
import com.lh.im.config.GroupConfig;
import com.lh.im.config.MsgResCode;
import com.lh.im.entity.Group;
import com.lh.im.entity.Msg;
import com.lh.im.entity.msg.MsgTextInfo;
import com.lh.im.entity.msg.result.MsgTextResult;

/**
 * @auther: loneyfall
 * @date: 2021/3/16
 * @description:
 */
public class MsgTextHandler implements BaseTextMsgHandler<MsgTextInfo, MsgTextResult> {

    public MsgTextResult handle(MsgTextInfo msgTextInfo) {
        MsgTextResult result = new MsgTextResult(Constant.MSG_TEXT);
        Msg msg = msgTextInfo.getMsg();
        if (!GroupConfig.hasGroup(msg.getGroupName())) {
            result.setMsgResCode(MsgResCode.NO_FOUND_GROUP);
            result.setSuccess(false);
            return result;
        }
        if (!GroupConfig.hasUser(msg.getUserId())) {
            result.setMsgResCode(MsgResCode.NO_FOUND_USER);
            result.setSuccess(false);
            return result;
        }
        Group group = GroupConfig.addMsg(msg);
        result.setMsgResCode(MsgResCode.OK);
        result.setSuccess(true);
        result.setGroup(group);
        return result;
    }
}
