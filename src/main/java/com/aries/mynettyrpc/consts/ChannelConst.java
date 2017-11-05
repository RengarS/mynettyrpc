package com.aries.mynettyrpc.consts;

import com.aries.mynettyrpc.domain.RpcResponse1;
import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Aries
 */
public class ChannelConst {
    public static Channel clientChannel = null;
    /**
     * 存放来自server端的Response
     */
    public static final ConcurrentHashMap<String, RpcResponse1> RESPONSE_MAP =
            new ConcurrentHashMap<>();
}
