package com.aries.register.boot;

import com.aries.register.annotations.AriesRpcDiscovery;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

/**
 * create by aries 2018-2-25
 * <p>
 * 启动项
 */
public class RegisterServerBoot {

    public static final byte[] DELIMITER = "_$$".getBytes();

    private void start(int port) {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            /**
                             * tcp粘包
                             */
                            socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(10000,
                                    Unpooled.copiedBuffer("_$$".getBytes())));
                            socketChannel.pipeline().addLast(new RegisterServerHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("===============");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    /**
     * 启动客户端
     *
     * @param clz
     */
    public static void run(Class<?> clz) {
        if (clz.isAnnotationPresent(AriesRpcDiscovery.class)) {
            AriesRpcDiscovery ariesRpcDiscovery = clz.getDeclaredAnnotation(AriesRpcDiscovery.class);
            new RegisterServerBoot().start(ariesRpcDiscovery.port());
        }
    }

    public static void main(String[] args) {
        new RegisterServerBoot().start(9999);
    }

}
