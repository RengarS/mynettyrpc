package com.aries.commons.domains;

import java.util.Map;

/**
 * create by aries 2018-1-21
 * Map类型的返回值
 *
 * @param <K>
 * @param <V>
 */
public class ObjectMapResponse<K, V> {
    private String responseId;
    private int code = 200;
    private String msg = "OK";
    private Map<K, V> data;

    public ObjectMapResponse(String responseId, Map<K, V> data) {
        this.responseId = responseId;
        this.data = data;
    }
}
