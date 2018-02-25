package com.aries.commons.matcher;

import com.aries.commons.domains.ObjectDataRequest;
import com.aries.commons.utils.SerializableUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.*;

/**
 * create by aries 2018-2-24
 * 请求结果匹配
 */
public class RpcMsgMatcher {
    private static Map<String, Object> objectMap = new ConcurrentHashMap<>();
    private static ExecutorService service = Executors.newFixedThreadPool(2);

    @SuppressWarnings("unchecked")
    public static <T> T requestSync(final ObjectDataRequest request, Channel channel, T resType) {
        byte[] msgBytes = SerializableUtils.SerializableObject(request, request.getClass());
        ByteBuf byteBuf = Unpooled.copiedBuffer(msgBytes);
        objectMap.put(request.getRequestId(), request);
        channel.writeAndFlush(byteBuf);
        synchronized (request) {
            if (objectMap.get(request.getRequestId()) == null) {

                try {
                    request.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return (T) objectMap.remove(request.getRequestId());
        }
    }

    @SuppressWarnings("unchecked")
    public static Future requestAsync(final ObjectDataRequest request, Channel channel) {
        byte[] bytes = SerializableUtils.SerializableObject(request, request.getClass());
        ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
        objectMap.put(request.getRequestId(), request);
        channel.writeAndFlush(byteBuf);

        Future future = service.submit(() -> {
            synchronized (request) {
                request.wait();
                return objectMap.remove(request.getRequestId());
            }
        });
        return future;
    }

    public static Map<String, Object> getObjectMap() {
        return objectMap;
    }
}
