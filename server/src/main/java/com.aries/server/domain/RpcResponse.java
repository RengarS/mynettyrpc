package com.aries.server.domain;

public class RpcResponse {
    private String responseId;
    private Object responseData;

    private int statu;

    public RpcResponse() {
    }

    public RpcResponse(String responseId, Object responseData) {
        this.responseId = responseId;
        this.responseData = responseData;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public Object getResponseData() {
        return responseData;
    }

    public void setResponseData(Object responseData) {
        this.responseData = responseData;
    }
}
