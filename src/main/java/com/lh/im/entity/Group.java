package com.lh.im.entity;

import com.lh.im.util.RelativeDateFormatTool;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther: loneyfall
 * @date: 2021/3/12
 * @description:
 */
public class Group {
    private String name;
    private String lastMsg;
    private String lastTime;
    private List<Msg> msgs;

    public Group(String name) {
        this.name = name;
        this.msgs = new ArrayList<Msg>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public List<Msg> getMsgs() {
        return msgs;
    }

    public void setMsgs(List<Msg> msgs) {
        this.msgs = msgs;
    }

    public synchronized boolean addMsg(Msg msg) {
        this.lastMsg = msg.getContent();
        this.lastTime = RelativeDateFormatTool.relativeDateFormat(msg.getTime());
        this.msgs.add(msg);
        return true;
    }
}
