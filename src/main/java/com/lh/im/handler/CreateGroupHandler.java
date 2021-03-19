package com.lh.im.handler;

import com.lh.im.config.Constant;
import com.lh.im.config.GroupConfig;
import com.lh.im.entity.msg.CreateGroupInfo;
import com.lh.im.entity.msg.result.CreateGroupMsgResult;

/**
 * @auther: loneyfall
 * @date: 2021/3/12
 * @description:
 */
public class CreateGroupHandler implements BaseTextMsgHandler<CreateGroupInfo, CreateGroupMsgResult> {

    public CreateGroupMsgResult handle(CreateGroupInfo info) {
        CreateGroupMsgResult result = new CreateGroupMsgResult(Constant.CREATE_GROUP);
        if (GroupConfig.createGroup(info.getGroupName())) {
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }
        result.setGroupName(info.getGroupName());
        return result;
    }
}
