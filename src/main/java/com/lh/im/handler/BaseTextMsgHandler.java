package com.lh.im.handler;

import com.lh.im.entity.msg.BaseInfo;
import com.lh.im.entity.msg.result.BaseMsgResult;

/**
 * @auther: loneyfall
 * @date: 2021/3/12
 * @description:
 */
public interface BaseTextMsgHandler<T extends BaseInfo, R extends BaseMsgResult> {

    R handle(T t);
}
