package com.aries.client.consts;

import com.aries.client.domain.RpcResponse1;
import io.netty.channel.Channel;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Aries
 */
public class ChannelConst {
    //public static Channel clientChannel = null;
    public static BlockingDeque<Channel> channelBlockingDeque = new LinkedBlockingDeque<>(20);
    /**
     * 存放来自server端的Response
     */
    public static final ConcurrentHashMap<String, RpcResponse1> RESPONSE_MAP =
            new ConcurrentHashMap<>();
}
