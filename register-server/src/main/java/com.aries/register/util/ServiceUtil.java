package com.aries.register.util;

import com.aries.commons.domains.ObjectDataRequest;
import com.aries.commons.domains.ObjectDataResponse;
import com.aries.commons.matcher.RpcMsgMatcher;
import com.aries.commons.utils.IDGenerator;
import com.aries.commons.consts.ServiceConst;
import io.netty.channel.Channel;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * create by aries 2018-1-21
 * 服务相关类
 */
public class ServiceUtil {
    private static ConcurrentHashMap<String, Channel> map = ChannelContainer.getIpChannelMap();
    private static Random random = new Random();

    /**
     * 微服务注册(启动时注册)
     *
     * @param serviceName
     * @param ipAddress
     * @return
     */
    public static boolean register(String serviceName, String ipAddress, Channel channel) {
        ConcurrentHashMap<String, List<String>> container = RegisterContainer.getServiceContainer();
        if (container.get(serviceName) == null) {
            List<String> ipAddresses = new LinkedList<>();
            ipAddresses.add(ipAddress);
            container.put(serviceName, ipAddresses);
        } else {
            List<String> ipAdds = container.get(serviceName);
            ipAdds.add(ipAddress);
            container.putIfAbsent(serviceName, ipAdds);
        }
        map.put(ipAddress, channel);
        return true;
    }

    /**
     * 检查微服务
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static void checkService() {
        RegisterContainer.getServiceContainer().forEach((key, value) -> {
            Iterator<String> stringIterator = value.iterator();

            while (stringIterator.hasNext()) {
                String serviceIp = stringIterator.next();
                Channel channel = map.get(serviceIp);
                try {
                    ObjectDataResponse<String> response = (ObjectDataResponse<String>) RpcMsgMatcher
                            .requestAsync(new ObjectDataRequest(IDGenerator.getRandomId(), ServiceConst.DO_CHECK, null), channel)
                            .get(5L, TimeUnit.MILLISECONDS);

                    if (response == null) {
                        stringIterator.remove();
                        map.remove(serviceIp);
                    }

                } catch (Exception e) {
                    stringIterator.remove();
                    map.remove(serviceIp);
                }
            }

            RegisterContainer.getServiceContainer().put(key, value);
        });

    }

    /**
     * 根据serviceName返回经过负载均衡处理的service url
     *
     * @param serviceName
     * @return
     */
    public static String getServiceIpByName(String serviceName) {
        List<String> serviceIps = RegisterContainer.getServiceContainer().get(serviceName);
        String serviceIp = "";

        //负载均衡
        if (serviceIps != null) {
            if (serviceIps.size() == 1) {
                serviceIp = serviceIps.get(0);
            } else {
                int index = random.nextInt(serviceIps.size() - 1);
                serviceIp = serviceIps.get(index);
            }
        }

        return serviceIp;
    }


    public static void requestSync() {
    }

    public void requestAsync() {
    }

    public void connectService() {
    }


}
