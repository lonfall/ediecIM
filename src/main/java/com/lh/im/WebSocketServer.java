package com.lh.im;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @auther: loneyfall
 * @date: 2021/3/8
 * @description:
 */
public class WebSocketServer {
    public void run(int port) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap boot = new ServerBootstrap();
            boot.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel sc) throws Exception {
                    ChannelPipeline pipeline = sc.pipeline();
                    pipeline.addLast("http-codec", new HttpServerCodec());
                    pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                    pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                    pipeline.addLast("hander", new WebSocketServerHandler());
                }
            });

            Channel ch = boot.bind(port).sync().channel();
            System.out.println("webSocket服务器启动成功：" + ch);
            ch.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            System.out.println("websocket服务器已关闭");
        }
    }
}
