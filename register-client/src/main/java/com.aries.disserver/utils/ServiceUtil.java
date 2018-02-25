package com.aries.disserver.utils;

import com.aries.commons.consts.ServiceConst;
import com.aries.commons.domains.ObjectDataRequest;
import com.aries.commons.domains.ObjectDataResponse;
import com.aries.commons.matcher.RpcMsgMatcher;
import com.aries.commons.utils.IDGenerator;

import com.aries.commons.utils.SerializableUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.concurrent.Future;

public class ServiceUtil {

    @SuppressWarnings("unchecked")
    public static String getServiceUrl(String serviceName) {
        Channel channel = null;
        ObjectDataRequest<String> request = new ObjectDataRequest<>(IDGenerator.getRandomId(), ServiceConst.GET_SERVICE, serviceName);
        Future<ObjectDataResponse<String>> future = RpcMsgMatcher.requestAsync(request, channel);
        try {
            return future.get().getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void doRegister(String serviceName, int port) {
        Channel channel = null;
        try {
            String ip = Inet4Address.getLocalHost().getHostAddress();
            String address = ip + ":" + port;

            ObjectDataRequest<String> request = new ObjectDataRequest<>(IDGenerator.getRandomId(), ServiceConst.DO_REGISTER, address);
            byte[] bytes = SerializableUtils.SerializableObject(request, ObjectDataRequest.class);
            ByteBuf byteBuf = Unpooled.copiedBuffer("_$$".getBytes());
            byteBuf.writeBytes(bytes);
            channel.writeAndFlush(byteBuf);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

}
