package com.aries.mynettyrpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务提供端的方法的id，用于识别客户端的调用请求
 *
 * @author Aries
 * @date 2017-11-7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ServiceId {
    /**
     * 服务提供者的id
     *
     * @return
     */
    String value();

    /**
     * 请求体的Class类型
     *
     * @return
     */
    Class<?> requestClass();

    /**
     * 返回体的Class类型
     *
     * @return
     */
    Class<?> responseClass();
}
