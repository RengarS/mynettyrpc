package com.aries.commons.domains;

import lombok.Data;

import java.util.Collection;

/**
 * create by aries 2018-1-21
 * 集合类型的Request
 *
 * @param <T>
 */
@Data
public class ObjectCollectionRequest<T> {
    private String requestId;
    private String serviceId;
    private Collection<T> data;

    public ObjectCollectionRequest(String requestId, String serviceId, Collection<T> data) {
        this.requestId = requestId;
        this.serviceId = serviceId;
        this.data = data;
    }
}
