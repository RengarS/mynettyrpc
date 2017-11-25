package com.aries.server.helper;

import com.aries.server.annotation.ServiceId;
import com.aries.server.domain.Handler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class ServiceHelper {
    private static HashMap<String, Handler> handlerHashMap = new HashMap<>();
    private static Set<Class<?>> classSet = BeanHelper.getBeanMap().keySet();

    public ServiceHelper() {
        classSet.stream().forEach(aClass -> {
            Arrays.stream(aClass.getMethods()).forEach(method -> {
                if (method.isAnnotationPresent(ServiceId.class)) {
                    ServiceId serviceId = method.getDeclaredAnnotation(ServiceId.class);
                    String key = serviceId.value();
                    handlerHashMap.put(key, new Handler(aClass, method));
                }
            });
        });
    }

    public static Handler getHandler(String key) {
        return
                handlerHashMap.get(key);
    }

    public static HashMap<String, Handler> getHandlerHashMap() {
        return handlerHashMap;
    }
}

