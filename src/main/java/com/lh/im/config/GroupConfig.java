package com.lh.im.config;

import com.lh.im.entity.Group;
import com.lh.im.entity.Msg;
import com.lh.im.util.IdGenerator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @auther: loneyfall
 * @date: 2021/3/12
 * @description:
 */
public class GroupConfig {
    public volatile static Map<String, Group> groups = new LinkedHashMap<String, Group>();

    public volatile static Map<String, String> users = new HashMap<String, String>();

    static {
        groups.put("大佬匿名交友群", new Group("大佬匿名交友群"));
    }

    /**
     * 创建群组，如果存在同名群组返回 false，创建成功返回 true
     *
     * @param name
     * @return
     */
    public synchronized static boolean createGroup(String name) {
        if (groups.containsKey(name)) {
            System.out.println("群组创建失败：已有同名群组【" + name + "】存在");
            return false;
        }
        if (groups.entrySet().size() >= 5000) {
            System.out.println("当前群组已经超过最大限制，不允许创建");
            return false;
        }
        System.out.println("开始创建群组【" + name + "】");
        groups.put(name, new Group(name));
        return true;
    }

    /**
     * 用户注册，添加用户到缓存需要同步操作
     *
     * @param userName
     * @return
     */
    public synchronized static String register(String userName) {
        // 生成唯一ID
        String id = IdGenerator.getInstance().batchId(Constant.TENANT_ID, Constant.MODULE);
        users.put(id, userName);
        return id;
    }


    public static boolean hasGroup(String name) {
        return groups.containsKey(name);
    }

    public static boolean hasUser(String userId) {
        return users.containsKey(userId);
    }

    public static Group addMsg(Msg msg) {
        msg.setUserName(users.get(msg.getUserId()));
        Group group = groups.get(msg.getGroupName());
        group.addMsg(msg);
        return group;
    }
}
