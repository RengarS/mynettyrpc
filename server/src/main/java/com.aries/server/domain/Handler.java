package com.aries.server.domain;

import lombok.Data;

import java.lang.reflect.Method;

@Data
public class Handler {
    private Class<?> actionClass;
    private Method method;

    public Handler(Class<?> actionClass, Method method) {
        this.actionClass = actionClass;
        this.method = method;
    }
}
