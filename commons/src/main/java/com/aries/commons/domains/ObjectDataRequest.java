package com.aries.commons.domains;

import lombok.Data;

/**
 * create by aries 2018-1-21
 * 普通类型的请求
 *
 * @param <T>
 */
@Data
public class ObjectDataRequest<T> {
    private String requestId;
    private String serviceId;
    private T data;

    public ObjectDataRequest(String requestId, String serviceId, T data) {
        this.requestId = requestId;
        this.serviceId = serviceId;
        this.data = data;
    }
}
