package com.aries.mynettyrpc.rpcclient;


import com.aries.mynettyrpc.consts.ChannelConst;
import com.aries.mynettyrpc.domain.RpcResponse1;
import com.aries.mynettyrpc.utils.SerializableUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ConcurrentHashMap;


/**
 * @author Aries
 */
public class RpcClientHandler extends ChannelInboundHandlerAdapter {
    private ConcurrentHashMap<String, RpcResponse1> map = ChannelConst.RESPONSE_MAP;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("get");
        ByteBuf byteBuf = ((ByteBuf) msg);
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        RpcResponse1 response = SerializableUtils.UnSerializableObject(bytes, RpcResponse1.class);
        System.out.println(response);
        this.map.putIfAbsent(response.getResponseId(), response);
        System.out.println(this.map.size());
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
