package com.aries.register.util;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * create by aries 2018-1-21
 * service 容器
 */
public class RegisterContainer {
    private static final ConcurrentHashMap<String, List<String>> SERVICE_CONTAINER = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, List<String>> getServiceContainer() {
        return SERVICE_CONTAINER;
    }
}
