package com.aries.client.utils;


import com.aries.client.consts.ChannelConst;
import com.aries.client.consts.ThreadPool;
import com.aries.client.domain.RpcRequest;
import com.aries.client.domain.RpcResponse;
import com.aries.client.rpcclient.RpcClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public class AriesRpc {
    public static ConcurrentHashMap<String, RpcRequest> rpcRequest1HashMap;

    private static final byte[] DELIMITER = "_$$".getBytes();

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
    public RpcResponse requestSync(final RpcRequest request) throws Exception {
        // ByteBuf byteBuf = Unpooled.copiedBuffer(SerializableUtils.SerializableObject(request, RpcRequest1.class));

        Channel channel = ChannelConst.channelBlockingQueue.take();
        ByteBuf byteBuf = Unpooled.directBuffer();
        byteBuf.writeBytes(SerializableUtils.SerializableObject(request, RpcRequest.class));
        byteBuf.writeBytes(DELIMITER);
        String requestId = request.getRequestId();
        rpcRequest1HashMap.put(requestId, request);
        channel.writeAndFlush(byteBuf);
        /**
         * channel的数目是定值，因此不存在add操作阻塞。
         */
        ChannelConst.channelBlockingQueue.add(channel);
        /**
         * 此处虽然使用 synchronized 来修饰变量，但是变量是一个局部变量，线程私有的。
         * 因此锁的状态仅仅是偏向锁，不存在竞争关系，不影响效率以及吞吐量。
         * 仅作为线程调度。
         */
        synchronized (request) {
            /**
             * 发送完请求后线程即进入阻塞状态，直到收到响应并唤醒该线程。
             */
            request.wait();
            try {
                return ChannelConst.RESPONSE_MAP.remove(requestId);

            } finally {
                rpcRequest1HashMap.remove(requestId);
            }
        }

    }

    /**
     * 发送异步请求，只返回一个future对象
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public Future<RpcResponse> requestAsync(final RpcRequest request) {
        Future<RpcResponse> future = ThreadPool.submit(() -> {

            ByteBuf byteBuf = Unpooled.copiedBuffer(SerializableUtils.SerializableObject(request, RpcRequest.class));
            Channel channel = ChannelConst.channelBlockingQueue.take();
            channel.writeAndFlush(byteBuf);
            String requestId = request.getRequestId();
            rpcRequest1HashMap.put(requestId, request);
            synchronized (request) {
                request.wait();
                try {
                    return ChannelConst.RESPONSE_MAP.remove(requestId);

                } finally {
                    rpcRequest1HashMap.remove(requestId);
                    ChannelConst.channelBlockingQueue.put(channel);
                }
            }
        });
        return future;
    }
}
