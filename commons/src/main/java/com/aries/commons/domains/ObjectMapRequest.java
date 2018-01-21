package com.aries.commons.domains;

import lombok.Data;

import java.util.Map;

/**
 * create by aries 2018-1-21
 * Map类型的请求
 *
 * @param <K>
 * @param <V>
 */
@Data
public class ObjectMapRequest<K, V> {
    private String requestId;
    private String serviceId;
    private Map<K, V> data;

    public ObjectMapRequest(String requestId, String serviceId, Map<K, V> data) {
        this.data = data;
        this.requestId = requestId;
        this.serviceId = serviceId;
    }
}
