package com.lh.im.entity.msg.result;

import com.lh.im.entity.Group;

import java.util.List;
import java.util.Map;

/**
 * @auther: loneyfall
 * @date: 2021/3/12
 * @description:
 */
public class GroupListMsgResult extends BaseMsgResult {
    private List<Group> groups;

    public GroupListMsgResult(String type) {
        super(type);
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
