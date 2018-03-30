package com.aries.client.rpcclient;

import com.aries.client.consts.ChannelConst;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import lombok.Data;

/**
 * @author Aries
 */
@Data
public class RpcClient {
    private String host;
    private int port;

    public static Channel channel;

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public RpcClient() {
    }

    public static void connect(String host, int port) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup(2);
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
                                    addLast(new RpcClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            channel = channelFuture.channel();
            ChannelConst.ADDRESS_CHANNEL_MAP.put(host + ":" + port, channelFuture.channel());
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}
