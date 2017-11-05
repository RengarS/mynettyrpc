package com.aries.mynettyrpc.domain;

public class RpcResponse1 {
    private String responseId;
    private String responseData;

    public RpcResponse1() {
    }

    public RpcResponse1(String responseId, String responseData) {
        this.responseId = responseId;
        this.responseData = responseData;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }
}
