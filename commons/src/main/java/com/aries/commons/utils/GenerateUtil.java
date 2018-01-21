package com.aries.commons.utils;

import com.aries.commons.domains.*;

import java.util.Collection;
import java.util.Map;

/**
 * create by aries 2018-1-21
 * 生成request和response工具类
 */
public class GenerateUtil {
    /**
     * 生成data是map的请求对象
     *
     * @param serviceId
     * @param data
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> ObjectMapRequest<K, V> generateMapRequest(String serviceId, Map<K, V> data) {

        return new ObjectMapRequest<>(IDGenerator.getRandomId(), serviceId, data);
    }

    /**
     * 生成data是集合类的请求对象
     *
     * @param serviceId
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ObjectCollectionRequest<T> generateCollectionRequest(String serviceId, Collection<T> data) {
        return new ObjectCollectionRequest<>(IDGenerator.getRandomId(), serviceId, data);
    }

    /**
     * 生成data是普通类的请求对象
     *
     * @param serviceId
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ObjectDataRequest<T> generateDataRequest(String serviceId, T data) {
        return new ObjectDataRequest<>(IDGenerator.getRandomId(), serviceId, data);
    }

    /**
     * 生成data是普通类的响应对象
     *
     * @param responseId
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ObjectDataResponse<T> generateDataResponse(String responseId, T data) {
        return new ObjectDataResponse<>(responseId, data);
    }

    /**
     * 生成data是集合类的响应对象
     *
     * @param responseId
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ObjectCollectionResponse<T> generateCollectionResponse(String responseId, Collection<T> data) {
        return new ObjectCollectionResponse<>(responseId, data);
    }

    /**
     * 生成data是Map类的相应对象
     *
     * @param responseId
     * @param data
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> ObjectMapResponse<K, V> generateMapResponse(String responseId, Map<K, V> data) {
        return new ObjectMapResponse<>(responseId, data);
    }
}
