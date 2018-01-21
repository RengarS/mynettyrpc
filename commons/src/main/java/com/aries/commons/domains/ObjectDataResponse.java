package com.aries.commons.domains;

import lombok.Data;

/**
 * create by aries 2018-1-21
 * 普通类型的返回值
 *
 * @param <T>
 */
@Data
public class ObjectDataResponse<T> {

    private String responseId;
    private int code = 200;
    private String msg = "OK";
    private T data;

    public ObjectDataResponse(String responseId, T data) {
        this.responseId = responseId;
        this.data = data;
    }
}
