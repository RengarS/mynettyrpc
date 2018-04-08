package com.aries.register.boot;

import com.aries.commons.domains.ObjectDataRequest;
import com.aries.commons.domains.ObjectDataResponse;
import com.aries.commons.domains.RegisterData;
import com.aries.commons.matcher.RpcMsgMatcher;
import com.aries.commons.utils.SerializableUtils;
import com.aries.commons.consts.ServiceConst;
import com.aries.register.util.ServiceUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RegisterServerHandler extends SimpleChannelInboundHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        ByteBuf msg = (ByteBuf) o;
        byte[] content = new byte[msg.readableBytes()];
        msg.readBytes(content);
        //byte数组反序列化成对象
        ObjectDataRequest request = SerializableUtils.UnSerializableObject(content, ObjectDataRequest.class);

//        System.out.println(((RegisterData) request.getData()).getServiceName() + "    " + ((RegisterData) request.getData()).getUrl());

        ObjectDataResponse response = dispatch(request, channelHandlerContext.channel());
        if (null != response) {
            byte[] bytes = SerializableUtils.SerializableObject(response, ObjectDataResponse.class);
            ByteBuf byteBuf = Unpooled.directBuffer();
            byteBuf.writeBytes(bytes);
            byteBuf.writeBytes(RegisterServerBoot.DELIMITER);
            channelHandlerContext.writeAndFlush(byteBuf);
        }
    }

    /**
     * 服务端处理客户端请求
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    private static ObjectDataResponse dispatch(ObjectDataRequest request, Channel channel) {
        ObjectDataResponse objectDataResponse = null;
        if (request.getServiceId().equals(ServiceConst.DO_CHECK)) {
            //检查service
            RpcMsgMatcher.getObjectMap().put(request.getRequestId(), request);
        } else if (request.getServiceId().equals(ServiceConst.DO_REGISTER)) {
            //注册
            RegisterData data = (RegisterData) request.getData();

            System.out.println(data.getServiceName() + "  " + data.getUrl());
            ServiceUtil.register(data.getServiceName(), data.getUrl(), channel);
        } else if (request.getServiceId().equals(ServiceConst.GET_SERVICE)) {
            //获取service
            objectDataResponse = new ObjectDataResponse<String>(null, null);
            objectDataResponse.setResponseId(request.getRequestId());
            objectDataResponse.setData(ServiceUtil.getServiceIpByName((String) request.getData()));
            System.out.println("get service did !");
        }

        return objectDataResponse;
    }
}
