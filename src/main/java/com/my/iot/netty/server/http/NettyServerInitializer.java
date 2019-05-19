package com.my.iot.netty.server.http;

import java.util.concurrent.ScheduledExecutorService;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * NettyServerInitializer.java
 * Netty서버 초기화용 클래스
 * @author 효민영♥
 *
 */
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    private ScheduledExecutorService service;

    public NettyServerInitializer(ScheduledExecutorService service) {
        this.service = service;
    }


    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast("encoder", new HttpResponseEncoder());
        p.addLast("decoder", new HttpRequestDecoder(4096, 8192, 8192, false));
        p.addLast("handler", new ServerRequestHandler(service));
    }
}
