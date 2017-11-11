package com.aries.server.rpcserver;

import com.aries.server.domain.RpcRequest;
import com.aries.server.domain.RpcResponse;
import com.aries.server.utils.DispatcherUtil;
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
    //分隔符
    private static final byte[] DELIMITER = "_$$".getBytes();

    /**
     * 收到客户端请求，请求处理后将结果封装成RpcResponse对象返回
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("get");
        Thread.sleep(5);
        ByteBuf byteBuf = ((ByteBuf) msg);
        System.out.println(msg);
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        RpcRequest request = SerializableUtils.UnSerializableObject(bytes, RpcRequest.class);
        System.out.println("serviceId:  " + request.getServiceId());
        ByteBuf response = ctx.alloc().directBuffer();
        Object result = DispatcherUtil.invoke(request.getServiceId(), request.getRequestData());
        System.out.println(result);
        response.writeBytes(SerializableUtils.SerializableObject(new RpcResponse(
                request.getRequestId(), result), RpcResponse.class));

        response.writeBytes(DELIMITER);
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
