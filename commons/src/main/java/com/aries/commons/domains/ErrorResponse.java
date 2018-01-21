package com.aries.commons.domains;

import lombok.Data;

/**
 * create by aries 2018-1-21
 * 当服务端出错时（发生了exception），返回Error返回值
 */
@Data
public class ErrorResponse {
    private String responseId;
    private int code = 404;
    private String msg = "服务端异常，请稍后再试！";

    public ErrorResponse(String responseId) {
        this.responseId = responseId;
    }
}
