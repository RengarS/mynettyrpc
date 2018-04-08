package com.aries.disserver.utils;

import com.aries.commons.domains.ObjectDataRequest;
import com.aries.commons.domains.ObjectDataResponse;
import com.aries.commons.utils.SerializableUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RegisterClientHandler extends SimpleChannelInboundHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

        System.out.println("client recved");

        ByteBuf byteBuf = (ByteBuf) o;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        ObjectDataResponse response = SerializableUtils.UnSerializableObject(bytes, ObjectDataResponse.class);
        ObjectDataRequest request = (ObjectDataRequest) RegisterClientUtil.objectMap.get(response.getResponseId());
        RegisterClientUtil.objectMap.put(request.getRequestId(), response);
        synchronized (request) {
            request.notify();
        }
    }
}
