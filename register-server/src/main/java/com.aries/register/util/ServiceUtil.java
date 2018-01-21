package com.aries.register.util;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * create by aries 2018-1-21
 * 服务相关类
 */
public class ServiceUtil {
    /**
     * 微服务注册(启动时注册)
     *
     * @param serviceName
     * @param ipAddress
     * @return
     */
    public static boolean register(String serviceName, String ipAddress) {
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
        return true;
    }


}
