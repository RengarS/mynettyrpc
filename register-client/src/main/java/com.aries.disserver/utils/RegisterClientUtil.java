package com.aries.disserver.utils;

import com.aries.commons.consts.ServiceConst;
import com.aries.commons.domains.ObjectDataRequest;
import com.aries.commons.domains.ObjectDataResponse;
import com.aries.commons.domains.RegisterData;
import com.aries.commons.utils.IDGenerator;
import com.aries.commons.utils.SerializableUtils;
import com.aries.disserver.consts.ServiceURLChannelConst;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegisterClientUtil {

    //注册中心的ip和host
    private static String registerServerHost = "";

    public static void setRegisterServerHost(String host) {
        registerServerHost = host;
    }

    public static Map<String, Object> objectMap = new ConcurrentHashMap<>();


    private static NioEventLoopGroup group = new NioEventLoopGroup(2);

    private static final byte[] DELIMITER = "_$$".getBytes();

    public static void doRegister(int port, String serviceName) {

        RegisterData data = new RegisterData();
        String ip = null;
        try {
            ip = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        data.setUrl(ip + ":" + port);
        data.setServiceName(serviceName);

        ObjectDataRequest<RegisterData> request = new ObjectDataRequest<>(IDGenerator.getRandomId(), ServiceConst.DO_REGISTER, data);

        byte[] bytes = SerializableUtils.SerializableObject(request, ObjectDataRequest.class);
        ByteBuf byteBuf = Unpooled.directBuffer();
        byteBuf.writeBytes(bytes);
        byteBuf.writeBytes(DELIMITER);
        ServiceURLChannelConst.getServiceAddChannelMap().get(registerServerHost).writeAndFlush(byteBuf);
    }


    @SuppressWarnings("unchecked")
    public static String getServiceUrl(String serviceName) {
        ObjectDataRequest<String> request = new ObjectDataRequest<>(IDGenerator.getRandomId(), ServiceConst.GET_SERVICE, serviceName);
        byte[] bytes = SerializableUtils.SerializableObject(request, ObjectDataRequest.class);
        ByteBuf byteBuf = Unpooled.directBuffer();
        byteBuf.writeBytes(bytes);
        byteBuf.writeBytes(DELIMITER);
        objectMap.put(request.getRequestId(), request);
        ServiceURLChannelConst.getServiceAddChannelMap().get(registerServerHost).writeAndFlush(byteBuf);
        synchronized (request) {
            try {
                request.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ObjectDataResponse<String> response = (ObjectDataResponse<String>) objectMap.remove(request.getRequestId());
        return response.getData();
    }

    public static void connectServiceHost(String serviceIp) {

        String[] ipAndPort = serviceIp.split(":");
        String ip = ipAndPort[0];
        int port = Integer.valueOf(ipAndPort[1]);

        new Thread(() -> {
            try {
                connect(ip, port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();


    }

    /**
     * 连接到远程服务器
     *
     * @param host
     * @param port
     * @throws Exception
     */
    public static void connect(String host, int port) throws Exception {
//        EventLoopGroup group = new NioEventLoopGroup(2);
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
                                    addLast(new SimpleChannelInboundHandler<Object>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
                                            ByteBuf byteBuf = (ByteBuf) o;
                                            byte[] data = new byte[byteBuf.readableBytes()];
                                            byteBuf.writeBytes(data);
                                            ObjectDataResponse response = SerializableUtils.UnSerializableObject(data, ObjectDataResponse.class);

                                            ObjectDataRequest request = (ObjectDataRequest) objectMap.get(response.getResponseId());

                                            objectMap.put(response.getResponseId(), response);
                                            synchronized (request) {
                                                request.notify();
                                            }
                                        }
                                    });
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            ServiceURLChannelConst.getServiceAddChannelMap().put(host + ":" + port, channelFuture.channel());
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}
