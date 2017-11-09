package com.aries.server.rpcserver;

import com.aries.server.domain.RpcRequest1;
import com.aries.server.domain.RpcResponse1;
import com.aries.server.utils.SerializableUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 分布式系统消费者Handler
 *
 * @author Aries
 */
public class RpcServerHandler extends ChannelInboundHandlerAdapter {
    AtomicInteger integer = new AtomicInteger(0);

    /**
     * 收到客户端请求，请求处理后将结果封装成RpcResponse对象返回
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        int a = integer.incrementAndGet();
        System.out.println(a);
        //TODO   根据id调用方法并得到返回值
        ByteBuf byteBuf = ((ByteBuf) msg);
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        RpcRequest1 request = SerializableUtils.UnSerializableObject(bytes, RpcRequest1.class);
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
