package com.aries.client.rpcclient;


import com.aries.client.consts.ChannelConst;
import com.aries.commons.domains.ObjectDataRequest;
import com.aries.commons.domains.ObjectDataResponse;
import com.aries.commons.utils.SerializableUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ConcurrentHashMap;


/**
 * @author Aries
 */
public class RpcClientHandler extends ChannelInboundHandlerAdapter {
    private ConcurrentHashMap<String, Object> map = ChannelConst.RESPONSE_MAP;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = ((ByteBuf) msg);
        //新建一个数组，用于读取msg的内容
        byte[] bytes = new byte[byteBuf.readableBytes()];
        //读取byteBuf的内容
        byteBuf.readBytes(bytes);
        //将byte[]反序列化成响应体
        ObjectDataResponse response = SerializableUtils.UnSerializableObject(bytes, ObjectDataRequest.class);
        //根据id从map中获取request
        ObjectDataRequest request1 = (ObjectDataRequest) this.map.get(response.getResponseId());
        //将response放入map中
        this.map.put(response.getResponseId(), response);
        //唤醒之前阻塞的线程
        synchronized (request1) {
            request1.notify();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
