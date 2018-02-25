package com.aries.register.util;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * create by aries 2018-2-24
 * ip和channel容器
 */
public class ChannelContainer {
    private static ConcurrentHashMap<String, Channel> IP_CHANNEL_MAP = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, Channel> getIpChannelMap() {
        return IP_CHANNEL_MAP;
    }

}
