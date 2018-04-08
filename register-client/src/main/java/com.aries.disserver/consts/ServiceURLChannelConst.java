package com.aries.disserver.consts;


import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceURLChannelConst {

    private static Map<String, Channel> SERVICE_ADD_CHANNEL_MAP = new ConcurrentHashMap<>();

    public static Map<String, Channel> getServiceAddChannelMap() {
        return SERVICE_ADD_CHANNEL_MAP;
    }

}
