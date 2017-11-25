package com.aries.server.utils;

import com.aries.server.domain.Handler;
import com.aries.server.helper.BeanHelper;
import com.aries.server.helper.ServiceHelper;
import io.netty.bootstrap.ServerBootstrap;

public class DispatcherUtil {

    public static Object invoke(String serviceId, Object param) {
        Handler handler = ServiceHelper.getHandler(serviceId);
        Object actionBean = BeanHelper.getBean(handler.getActionClass());
        Object result = ReflectionUtil.invokeMethod(actionBean, handler.getMethod(), param);
        return result;
    }

}
