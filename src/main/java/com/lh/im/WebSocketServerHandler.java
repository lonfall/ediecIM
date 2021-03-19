package com.lh.im;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lh.im.config.Constant;
import com.lh.im.config.MsgResCode;
import com.lh.im.entity.msg.CreateGroupInfo;
import com.lh.im.entity.msg.GroupListInfo;
import com.lh.im.entity.msg.MsgTextInfo;
import com.lh.im.entity.msg.RegisterInfo;
import com.lh.im.entity.msg.result.CreateGroupMsgResult;
import com.lh.im.entity.msg.result.GroupListMsgResult;
import com.lh.im.entity.msg.result.MsgTextResult;
import com.lh.im.entity.msg.result.RegisterResult;
import com.lh.im.handler.CreateGroupHandler;
import com.lh.im.handler.GroupListHandler;
import com.lh.im.handler.MsgTextHandler;
import com.lh.im.handler.RegisterHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @auther: loneyfall
 * @date: 2021/3/8
 * @description:
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handshaker;

    private static final String host = "45.76.157.137";
    private static final String port = "8848";

    /**
     * 存储每一个客户端接入进来时的channel对象
     */
    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 客户端与服务端创建连接的时候调用
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        group.add(ctx.channel());  //保存channel
        System.out.println("客户端与服务端连接开启......");
    }

    /**
     * 客户端与服务器断开连接的时候调用
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        group.remove(ctx.channel());  //移除channel
        System.out.println("客户端与服务端连接关闭......");
    }

    /**
     * 服务器接收客户端发送过来的数据结束之后调用
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /**
     * 在工程出现异常的时候调用
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 服务端处理客户端websocket请求的核心方法
     *
     * @param channelHandlerContext
     * @param msg
     * @throws Exception
     */
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(channelHandlerContext, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(channelHandlerContext, (WebSocketFrame) msg);
        }
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        // 如果HTTP解码失败，返回HTTP异常
        if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://" + host + ":" + port + "/websocket", null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        //判断是否是关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), ((CloseWebSocketFrame) frame).retain());
            return;
        }
        //判断是否是ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        //判断是否是文本消息
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format("暂时仅支持文本消息", frame.getClass().getName()));
        }

        String request = ((TextWebSocketFrame) frame).text();
        try {
            JSONObject json = JSON.parseObject(request);
            String type = json.getString("type");
            if (Constant.CREATE_GROUP.equals(type)) {
                CreateGroupMsgResult result = new CreateGroupHandler().handle(JSON.parseObject(request, CreateGroupInfo.class));
                if (result.isSuccess()) {
                    TextWebSocketFrame tws = new TextWebSocketFrame(JSON.toJSONString(result));
                    group.writeAndFlush(tws);
                } else {
                    TextWebSocketFrame tws = new TextWebSocketFrame(JSON.toJSONString(result));
                    ctx.channel().writeAndFlush(tws);
                }
            } else if (Constant.GROUP_LIST.equals(type)) {
                GroupListMsgResult result = new GroupListHandler().handle(JSON.parseObject(request, GroupListInfo.class));
                if (result.isSuccess()) {
                    TextWebSocketFrame tws = new TextWebSocketFrame(JSON.toJSONString(result));
                    ctx.channel().writeAndFlush(tws);
                }
            } else if (Constant.MSG_TEXT.equals(type)) {
                MsgTextResult result = new MsgTextHandler().handle(JSON.parseObject(request, MsgTextInfo.class));
                if (result.isSuccess()) {
                    TextWebSocketFrame tws = new TextWebSocketFrame(JSON.toJSONString(result));
                    group.writeAndFlush(tws);
                } else {
                    if (MsgResCode.NO_FOUND_GROUP.equals(result.getMsgResCode())) {
                        TextWebSocketFrame tws = new TextWebSocketFrame(JSON.toJSONString(result));
                        ctx.channel().writeAndFlush(tws);
                    } else if (MsgResCode.NO_FOUND_USER.equals(result.getMsgResCode())) {
                        TextWebSocketFrame tws = new TextWebSocketFrame(JSON.toJSONString(result));
                        ctx.channel().writeAndFlush(tws);
                    }
                }
            } else if (Constant.REGISTER.equals(type)) {
                RegisterResult result = new RegisterHandler().handle(JSON.parseObject(request, RegisterInfo.class));
                if (result.isSuccess()) {
                    TextWebSocketFrame tws = new TextWebSocketFrame(JSON.toJSONString(result));
                    ctx.channel().writeAndFlush(tws);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//
//        TextWebSocketFrame tws = new TextWebSocketFrame(request + "【接收到websocket消息】（当前时间" + new Date().toString() + "）");
//
//        //群发，服务端向每个连接上来的客户端群发消息
//        group.writeAndFlush(tws);
    }

    /**
     * 返回应答给客户端
     *
     * @param ctx
     * @param req
     * @param res
     */
    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }

        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
