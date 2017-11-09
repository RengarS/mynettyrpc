package com.aries.client.domain;
public class RpcRequest1 {
    /**
     * 请求id
     */
    private String requestId;
    /**
     * 请求体
     */
    private String requestData;

    public RpcRequest1() {
    }

    public RpcRequest1(String requestId, String requestData) {
        this.requestId = requestId;
        this.requestData = requestData;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }
}
