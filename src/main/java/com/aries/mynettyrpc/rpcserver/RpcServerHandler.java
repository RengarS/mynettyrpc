package com.aries.mynettyrpc.rpcserver;


import com.aries.mynettyrpc.domain.RpcRequest1;
import com.aries.mynettyrpc.domain.RpcResponse1;
import com.aries.mynettyrpc.utils.SerializableUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Aries
 */
public class RpcServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("get");
        System.out.println(msg.getClass());
        ByteBuf byteBuf = ((ByteBuf) msg);
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        RpcRequest1 request = SerializableUtils.UnSerializableObject(bytes, RpcRequest1.class);
        System.out.println(request.getRequestId());
        ByteBuf response = ctx.alloc().directBuffer();
        response.writeBytes(SerializableUtils.SerializableObject(new RpcResponse1(
                request.getRequestId(), "response:" + request.getRequestId()), RpcResponse1.class));
        ctx.writeAndFlush(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }

}
