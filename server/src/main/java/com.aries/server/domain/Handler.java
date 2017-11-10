package com.aries.server.domain;

import java.lang.reflect.Method;

public class Handler {
    private Class<?> actionClass;
    private Method method;

    public Handler(Class<?> actionClass, Method method) {
        this.actionClass = actionClass;
        this.method = method;
    }

    public Class<?> getActionClass() {
        return actionClass;
    }

    public Method getMethod() {
        return method;
    }
}
