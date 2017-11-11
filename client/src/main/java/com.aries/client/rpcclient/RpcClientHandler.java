package com.aries.client.rpcclient;


import com.aries.client.consts.ChannelConst;
import com.aries.client.domain.RpcRequest;
import com.aries.client.domain.RpcResponse;
import com.aries.client.utils.AriesRpc;
import com.aries.client.utils.SerializableUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ConcurrentHashMap;


/**
 * @author Aries
 */
public class RpcClientHandler extends ChannelInboundHandlerAdapter {
    private ConcurrentHashMap<String, RpcResponse> map = ChannelConst.RESPONSE_MAP;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //System.out.println("get");
        ByteBuf byteBuf = ((ByteBuf) msg);
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        RpcResponse response = SerializableUtils.UnSerializableObject(bytes, RpcResponse.class);
        this.map.put(response.getResponseId(), response);
        RpcRequest request1 = AriesRpc.rpcRequest1HashMap.get(response.getResponseId());
        System.out.println(response.getResponseId() + "     responseID" + "    " + AriesRpc.rpcRequest1HashMap.size());
        System.out.println(request1 + "+++++");
        synchronized (request1) {
            request1.notifyAll();
            System.out.println("already notify");
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
