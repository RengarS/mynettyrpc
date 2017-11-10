package com.aries.client.rpcclient;

import com.aries.client.consts.ChannelConst;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author Aries
 */
public class RpcClient {
    public void connect(String host, int port) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup(2);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new RpcClientHandler());
                        }
                    });
            ChannelFuture future = null;
            for (int i = 0; i < 10; i++) {
                future = bootstrap.connect(host, port).sync();
                ChannelConst.channelBlockingQueue.put(future.channel());
            }
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        final RpcClient client = new RpcClient();
        client.connect("127.0.0.1", 8888);
    }
}
