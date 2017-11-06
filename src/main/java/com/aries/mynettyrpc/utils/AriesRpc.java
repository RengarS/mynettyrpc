package com.aries.mynettyrpc.utils;


import com.aries.mynettyrpc.consts.ChannelConst;
import com.aries.mynettyrpc.consts.ThreadPool;
import com.aries.mynettyrpc.domain.RpcRequest1;
import com.aries.mynettyrpc.domain.RpcResponse1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class AriesRpc {
    /**
     * 发送同步请求，最多阻塞三秒
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static RpcResponse1 requestSync(final RpcRequest1 request) throws Exception {

        Future future = ThreadPool.submit(() -> {
            ByteBuf byteBuf = Unpooled.copiedBuffer(SerializableUtils.
                    SerializableObject(request, RpcRequest1.class));
            ChannelConst.clientChannel.writeAndFlush(byteBuf);
            String requestId = request.getRequestId();
            try {
                for (; ; ) {
                    RpcResponse1 response = ChannelConst.RESPONSE_MAP.get(requestId);
                    if (response != null) {
                        return response;
                    }
                }
            } finally {
                ChannelConst.RESPONSE_MAP.remove(requestId);
            }

        });
        System.out.println(future);
        return (RpcResponse1) future.get(3000, TimeUnit.MILLISECONDS);
    }

    /**
     * 发送异步请求，只返回一个future对象
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Future<RpcResponse1> requestAsync(final RpcRequest1 request) {
        Future<RpcResponse1> future = ThreadPool.submit(() -> {

            ByteBuf byteBuf = Unpooled.copiedBuffer(SerializableUtils.SerializableObject(request, RpcRequest1.class));
            ChannelConst.clientChannel.writeAndFlush(byteBuf);
            String requestId = request.getRequestId();
            try {
                for (; ; ) {
                    RpcResponse1 response = ChannelConst.RESPONSE_MAP.get(requestId);
                    if (response != null) {
                        return response;
                    }
                }
            } finally {
                ChannelConst.RESPONSE_MAP.remove(requestId);
            }

        });
        return future;
    }
}
