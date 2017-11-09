package com.aries.client.utils;


import com.aries.client.consts.ChannelConst;
import com.aries.client.consts.ThreadPool;
import com.aries.client.domain.RpcRequest1;
import com.aries.client.domain.RpcResponse1;
import com.aries.client.rpcclient.RpcClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public class AriesRpc {
    public static ConcurrentHashMap<String, RpcRequest1> rpcRequest1HashMap;

    public AriesRpc() {
        rpcRequest1HashMap = new ConcurrentHashMap<>();
        ThreadPool.submit(() -> {
            try {
                new RpcClient().connect("127.0.0.1", 8888);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 发送同步请求，最多阻塞三秒
     *
     * @param request
     * @return
     * @throws Exception
     */
    public RpcResponse1 requestSync(final RpcRequest1 request) throws Exception {

        Future future = ThreadPool.submit(() -> {

                    ByteBuf byteBuf = Unpooled.copiedBuffer(SerializableUtils.
                            SerializableObject(request, RpcRequest1.class));
                    Channel channel = ChannelConst.channelBlockingDeque.take();
                    channel.writeAndFlush(byteBuf);
                    String requestId = request.getRequestId();
                    System.out.println(requestId + "-----" + request);
                    rpcRequest1HashMap.put(requestId, request);
                    synchronized (request) {
                        request.wait();
                        try {
                            RpcResponse1 response = ChannelConst.RESPONSE_MAP.get(requestId);
                            if (response != null) {
                                return response;
                            }
                        } finally {
                            ChannelConst.RESPONSE_MAP.remove(requestId);
                            rpcRequest1HashMap.remove(requestId);
                            ChannelConst.channelBlockingDeque.put(channel);
                        }
                    }
                    return null;
                }

        );
        //return (RpcResponse1) future.get(3000, TimeUnit.MILLISECONDS);
        return (RpcResponse1) future.get();
    }

    /**
     * 发送异步请求，只返回一个future对象
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public Future<RpcResponse1> requestAsync(final RpcRequest1 request) {
        Future<RpcResponse1> future = ThreadPool.submit(() -> {

            ByteBuf byteBuf = Unpooled.copiedBuffer(SerializableUtils.SerializableObject(request, RpcRequest1.class));
            Channel channel = ChannelConst.channelBlockingDeque.take();
            channel.writeAndFlush(byteBuf);
            String requestId = request.getRequestId();
            rpcRequest1HashMap.put(requestId, request);
            synchronized (request) {
                request.wait();
                try {
                    RpcResponse1 response = ChannelConst.RESPONSE_MAP.get(requestId);
                    if (response != null) {
                        return response;
                    }

                } finally {
                    int a;
                    ChannelConst.RESPONSE_MAP.remove(requestId);
                    rpcRequest1HashMap.remove(requestId);
                    ChannelConst.channelBlockingDeque.put(channel);
                }
            }
            return null;
        });
        return future;
    }
}
