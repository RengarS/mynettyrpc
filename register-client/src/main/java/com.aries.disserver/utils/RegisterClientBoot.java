package com.aries.disserver.utils;

import com.aries.disserver.consts.ServiceURLChannelConst;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

public class RegisterClientBoot {

//    public static Channel channel = null;

    private static EventLoopGroup group = new NioEventLoopGroup(5);

    /**
     * 连接到注册中心
     *
     * @param host
     * @param port
     * @throws Exception
     */
    public static void connect(String host, int port) throws Exception {
        //EventLoopGroup group = new NioEventLoopGroup(2);

        RegisterClientUtil.setRegisterServerHost(host + ":" + port);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //处理tcp粘包
                            socketChannel
                                    .pipeline()
                                    .addLast(new DelimiterBasedFrameDecoder(10000,
                                            Unpooled.copiedBuffer("_$$".getBytes()))
                                    );

                            socketChannel
                                    .pipeline().
                                    addLast(new RegisterClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
//            channel = channelFuture.channel();
            ServiceURLChannelConst.getServiceAddChannelMap().put(host + ":" + port, channelFuture.channel());
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
