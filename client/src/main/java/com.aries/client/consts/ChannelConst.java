package com.aries.client.consts;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author Aries
 */
public class ChannelConst {
    /**
     * 存放各个channel
     */
    public static Map<String, Channel> ADDRESS_CHANNEL_MAP = new ConcurrentHashMap<>();
    /**
     * 存放来自server端的Response和client端的request。当用户发送请求的时候，将调用request.wait()阻塞线程，同时requestId和request
     * 会存在此容器中。当客户端接收到服务端的response的时候，会调用map.get(id)得到request对象，并调用map.put(id,response)将服务
     * 端返回的response放进进此容器中，随后调用request.notify()唤醒之前阻塞的线程，随后此线程将调用map.remove(id)得到服务端得返
     * 回值体，完成整个流程。
     * <p>
     * 客户发送请求request (线程A)----> 从队列中取出一个channel  ----> channel.writeAndFlush(request)  ----> 将channel返回
     * 给队列  ----> map.put(id,request) ----> request.wait() (阻塞线程A) ---->服务端收到request，并进行处理，最后将结果返回
     * 到客户端  ---->客户端线程B(专门处理服务端返回值的线程) :map.get(id)获得request对象  ----> map.put(id,response)
     * ---->request.notify()  ---->线程A重新开始执行，map.remove(id)得到response对象并返回。
     *
     * @see com.aries.client.rpcclient.RpcClientHandler
     * @see com.aries.client.utils.AriesRpc
     */
    public static final ConcurrentHashMap<String, Object> RESPONSE_MAP = new ConcurrentHashMap<>();
}
