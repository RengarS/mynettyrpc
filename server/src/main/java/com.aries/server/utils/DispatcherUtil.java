package com.aries.server.utils;

import com.aries.server.domain.Handler;
import com.aries.server.helper.BeanHelper;
import com.aries.server.helper.ServiceHelper;

public class DispatcherUtil {

    public static Object invoke(String requestId, Object param) {
        Handler handler = ServiceHelper.getHandler(requestId);
        Object actionBean = BeanHelper.getBean(handler.getActionClass());
        Object result = ReflectionUtil.invokeMethod(actionBean, handler.getMethod(), param);
        return result;
    }

}
