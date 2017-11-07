package com.aries.mynettyrpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Rpc服务端的地址和port,将初始化channel
 *
 * @author Aries
 * @date 2017-11-7
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcClient {
    String value();

    int port();
}
