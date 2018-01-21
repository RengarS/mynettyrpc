package com.aries.commons.domains;

import lombok.Data;

import java.util.Collection;

/**
 * create by aries 2018-1-21
 * 集合类型的返回值
 *
 * @param <T>
 */
@Data
public class ObjectCollectionResponse<T> {
    private String responseId;
    private int code = 200;
    private String msg = "OK";
    private Collection<T> data;

    public ObjectCollectionResponse(String responseId, Collection<T> data) {
        this.responseId = responseId;
        this.data = data;
    }
}
