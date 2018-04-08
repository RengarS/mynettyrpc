package com.aries.client.utils;


import com.aries.client.consts.ChannelConst;
import com.aries.client.consts.ThreadPool;
import com.aries.client.rpcclient.RpcClient;
import com.aries.commons.domains.ObjectDataRequest;
import com.aries.commons.domains.ObjectDataResponse;
import com.aries.commons.utils.IDGenerator;
import com.aries.commons.utils.SerializableUtils;
import com.aries.disserver.consts.ServiceURLChannelConst;
import com.aries.disserver.utils.RegisterClientUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.concurrent.Future;

public final class AriesRpc {
    private static final byte[] DELIMITER = "_$$".getBytes();

    public AriesRpc(String host, int port) {
        ThreadPool.submit(() -> {
            try {
                new RpcClient().connect(host, port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 发送同步请求，最多阻塞三秒
     *
     * @param
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public <T> T requestSync(String serviceName, String serviceId, T data) throws Exception {
        ObjectDataRequest<T> request = new ObjectDataRequest(IDGenerator.getRandomId(), serviceId, data);
        Channel channel = getServiceChannel(serviceName);
        //从阻塞队列中取出一个channel
//        Channel channel = RpcClient.channel;
        //申请一个ByteBuf
        ByteBuf byteBuf = Unpooled.directBuffer();
        //将请求体序列化并写入到ByteBuf中
        byteBuf.writeBytes(SerializableUtils.SerializableObject(request, ObjectDataRequest.class));
        //向ByteBuf末尾添加分隔符，防止粘包
        byteBuf.writeBytes(DELIMITER);
        String requestId = request.getRequestId();
        //将id和request放进一个map中
        ChannelConst.RESPONSE_MAP.put(requestId, request);
        //将请求发送到服务端
        channel.writeAndFlush(byteBuf);
        synchronized (request) {
            request.wait();
            return (T) ((ObjectDataResponse) ChannelConst.RESPONSE_MAP.remove(requestId)).getData();
        }

    }

    /**
     * 发送异步请求，只返回一个future对象
     *
     * @param serviceName
     * @param serviceId
     * @param data
     * @param <T>
     * @param <B>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T, B> Future<T> requestAsync(String serviceName, String serviceId, B data) {

        ObjectDataRequest<B> request = new ObjectDataRequest(IDGenerator.getRandomId(), serviceId, data);

        Channel channel = getServiceChannel(serviceName);
        Future<T> future = ThreadPool.submit(() -> {
            /**
             * 将RpcResponse序列化成byte[],并转成ByteBuf对象
             */
            ByteBuf byteBuf = Unpooled.copiedBuffer(SerializableUtils.SerializableObject(request, ObjectDataRequest.class));
            /**
             * 将RpcRequest发送出去
             */
            channel.writeAndFlush(byteBuf);
            String requestId = request.getRequestId();
            /**
             * 将requestId和request放进map中
             */
            ChannelConst.RESPONSE_MAP.put(requestId, request);
            synchronized (request) {
                request.wait();
                return (T) ((ObjectDataResponse) ChannelConst.RESPONSE_MAP.remove(requestId)).getData();

            }
        });
        return future;
    }

    /**
     * 根据serviceName从注册中心获取serviceUrl，再连接到service host，获取channel并返回
     *
     * @param serviceName
     * @return
     */
    private static Channel getServiceChannel(String serviceName) {
        String serviceUrl = RegisterClientUtil.getServiceUrl(serviceName);
        Channel channel = ServiceURLChannelConst.getServiceAddChannelMap().get(serviceUrl);

        if (channel == null) {
            RegisterClientUtil.connectServiceHost(serviceUrl);
        }

        channel = ServiceURLChannelConst.getServiceAddChannelMap().get(serviceUrl);
        if (channel == null) {
            throw new RuntimeException("service host " + serviceName + "不存在！");
        }
        return channel;
    }
}
