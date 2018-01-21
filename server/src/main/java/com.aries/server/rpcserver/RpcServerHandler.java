package com.aries.server.rpcserver;

import com.aries.server.utils.DispatcherUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 分布式系统消费者Handler
 *
 * @author Aries
 */
public class RpcServerHandler extends ChannelInboundHandlerAdapter {
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
        Thread.sleep(5);
        ByteBuf byteBuf = ((ByteBuf) msg);
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        RpcRequest request = SerializableUtils.UnSerializableObject(bytes, RpcRequest.class);
        ByteBuf response = ctx.alloc().directBuffer();
        Object result = DispatcherUtil.invoke(request.getServiceId(), request.getRequestData());
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
