package com.lh.im.handler;

import com.lh.im.config.GroupConfig;
import com.lh.im.entity.msg.RegisterInfo;
import com.lh.im.entity.msg.result.RegisterResult;

import static com.lh.im.config.Constant.REGISTER;

/**
 * @auther: loneyfall
 * @date: 2021/3/17
 * @description:
 */
public class RegisterHandler implements BaseTextMsgHandler<RegisterInfo, RegisterResult> {
    public RegisterResult handle(RegisterInfo registerInfo) {
        RegisterResult result = new RegisterResult(REGISTER);
        String userId = GroupConfig.register(registerInfo.getUserName());
        result.setUserId(userId);
        result.setSuccess(true);
        return result;
    }
}
