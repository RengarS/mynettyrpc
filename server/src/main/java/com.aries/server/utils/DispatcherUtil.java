package com.aries.server.utils;

import com.aries.server.domain.Handler;
import com.aries.server.helper.BeanHelper;
import com.aries.server.helper.ServiceHelper;
import io.netty.bootstrap.ServerBootstrap;

public class DispatcherUtil {

    public static Object invoke(String requestId, Object param) {
        System.out.println(ServiceHelper.getHandlerHashMap().size());
        Handler handler = ServiceHelper.getHandler(requestId);
        System.out.println(handler.getActionClass().getName() + "      " + handler.getMethod().getName());
        Object actionBean = BeanHelper.getBean(handler.getActionClass());
        Object result = ReflectionUtil.invokeMethod(actionBean, handler.getMethod(), param);
        return result;
    }

}
