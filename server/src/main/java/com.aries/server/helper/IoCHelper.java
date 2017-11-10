package com.aries.server.helper;

import com.aries.server.annotation.AutoWired;
import com.aries.server.utils.ReflectionUtil;

import java.util.Arrays;
import java.util.Set;

public class IoCHelper {
    private static Set<Class<?>> CLASS_SET = BeanHelper.getBeanMap().keySet();

    public IoCHelper() {

        CLASS_SET
                .stream()
                .forEach(clz -> {
                    Arrays
                            .stream(clz.getDeclaredFields())
                            .forEach(field -> {
                                if (field.isAnnotationPresent(AutoWired.class)) {
                                    try {
                                        ReflectionUtil
                                                .setField(
                                                        BeanHelper
                                                                .getBeanMap()
                                                                .get(clz), field,
                                                        BeanHelper
                                                                .getBeanMap()
                                                                .get(field.getClass())
                                                );
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                });
    }
}
