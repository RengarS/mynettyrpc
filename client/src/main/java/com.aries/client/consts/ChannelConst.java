package com.aries.client.consts;

import com.aries.client.domain.RpcResponse;
import io.netty.channel.Channel;

import java.util.concurrent.*;

/**
 * @author Aries
 */
public class ChannelConst {
    //public static Channel clientChannel = null;
    public static BlockingQueue<Channel> channelBlockingQueue = new LinkedBlockingQueue<>(20);
    /**
     * 存放来自server端的Response
     */
    public static final ConcurrentHashMap<String, RpcResponse> RESPONSE_MAP =
            new ConcurrentHashMap<>();
}
