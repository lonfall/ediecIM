package com.lh.im.handler;

import com.lh.im.config.Constant;
import com.lh.im.config.GroupConfig;
import com.lh.im.entity.Group;
import com.lh.im.entity.msg.GroupListInfo;
import com.lh.im.entity.msg.result.GroupListMsgResult;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @auther: loneyfall
 * @date: 2021/3/12
 * @description:
 */
public class GroupListHandler implements BaseTextMsgHandler<GroupListInfo, GroupListMsgResult> {

    public GroupListMsgResult handle(GroupListInfo groupListInfo) {
        GroupListMsgResult result = new GroupListMsgResult(Constant.GROUP_LIST);
        result.setGroups(GroupConfig.groups.entrySet().stream().map(m -> m.getValue()).collect(Collectors.toList()));
        result.setSuccess(true);
        return result;
    }
}
