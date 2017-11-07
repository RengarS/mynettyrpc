package com.aries.mynettyrpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启动类注解,Rpc服务端的地址和port
 *
 * @author Aries
 * @date 2017-11-7
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcServerAdd {
    String value();

    int port();
}
