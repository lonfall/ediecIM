package com.lh.im;

/**
 * @auther: loneyfall
 * @date: 2021/3/9
 * @description:
 */
public class Main {
    public static final int port = 8848;

    public static void main(String[] args) {
        new WebSocketServer().run(port);
    }
}
