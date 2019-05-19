package com.my.iot.netty.server.main;


import com.my.iot.netty.server.http.NettyServerInitializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;

/**
 * NettyWebServerMain.java
 * 
 * Netty 웹서버를 띄울 때, 사용할 수 있는 클래스
 * 단위 테스트를 http 기반에서 진행할수도 있는데, 크게 의미가 없는 듯 함.
 * 
 * @author 효민영♥
 *
 */
public class NettyWebServerMain {
    static {
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);
    }

    private final int port;

    public NettyWebServerMain(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        if (Epoll.isAvailable()) {
            doRun(new EpollEventLoopGroup(), EpollServerSocketChannel.class);
        } else {
            doRun(new NioEventLoopGroup(), NioServerSocketChannel.class);
        }
    }

    private void doRun(EventLoopGroup loupGroup, Class<? extends ServerChannel> serverChannelClass) throws InterruptedException {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.option(ChannelOption.SO_REUSEADDR, true);
            b.group(loupGroup).channel(serverChannelClass).childHandler(new NettyServerInitializer(loupGroup.next()));
            b.option(ChannelOption.MAX_MESSAGES_PER_READ, Integer.MAX_VALUE);
            b.childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(true));
            b.childOption(ChannelOption.SO_REUSEADDR, true);
            b.childOption(ChannelOption.MAX_MESSAGES_PER_READ, Integer.MAX_VALUE);

            Channel ch = b.bind(port).sync().channel();
            System.out.println("Server started successfully..");
            ((io.netty.channel.Channel) ch).closeFuture().sync();
        } finally {
            loupGroup.shutdownGracefully().sync();
        }
    }

    // 기본 8080 포트로 바인딩 한다.
    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new NettyWebServerMain(port).run();
    }
}
