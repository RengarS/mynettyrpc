package com.aries.client.utils;


import com.aries.client.consts.ChannelConst;
import com.aries.client.consts.ThreadPool;
import com.aries.client.rpcclient.RpcClient;
import com.aries.commons.utils.SerializableUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

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
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public <T> T requestSync(final RpcRequest request) throws Exception {
        //从阻塞队列中取出一个channel
        Channel channel = ChannelConst.channelBlockingQueue.take();
        //申请一个ByteBuf
        ByteBuf byteBuf = Unpooled.directBuffer();
        //将请求体序列化并写入到ByteBuf中
        byteBuf.writeBytes(SerializableUtils.SerializableObject(request, RpcRequest.class));
        //向ByteBuf末尾添加分隔符，防止粘包
        byteBuf.writeBytes(DELIMITER);
        String requestId = request.getRequestId();
        //将id和request放进一个map中
        ChannelConst.RESPONSE_MAP.put(requestId, request);
        //将请求发送到服务端
        channel.writeAndFlush(byteBuf);
        // channel的数目是定值，因此不存在add操作阻塞。
        ChannelConst.channelBlockingQueue.add(channel);
        synchronized (request) {
            request.wait();
            return (T) ((RpcResponse) ChannelConst.RESPONSE_MAP.remove(requestId)).getResponseData();
        }

    }

    /**
     * 发送异步请求，只返回一个future对象
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> Future<T> requestAsync(final RpcRequest request) {
        Future<T> future = ThreadPool.submit(() -> {
            /**
             * 将RpcResponse序列化成byte[],并转成ByteBuf对象
             */
            ByteBuf byteBuf = Unpooled.copiedBuffer(SerializableUtils.SerializableObject(request, RpcRequest.class));
            /**
             * 从存放有channel的阻塞队列中取到一个channel
             */
            Channel channel = ChannelConst.channelBlockingQueue.take();
            /**
             * 将RpcRequest发送出去
             */
            channel.writeAndFlush(byteBuf);
            /**
             * 将channel返回给阻塞队列
             */
            ChannelConst.channelBlockingQueue.add(channel);
            String requestId = request.getRequestId();
            /**
             * 将requestId和request放进map中
             */
            ChannelConst.RESPONSE_MAP.put(requestId, request);
            synchronized (request) {
                request.wait();
                return (T) ((RpcResponse) ChannelConst.RESPONSE_MAP.remove(requestId)).getResponseData();

            }
        });
        return future;
    }
}
