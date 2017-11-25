package com.aries.client.domain;

public class RpcRequest {
        /**
         * 请求id
         */
        private String requestId;

        private String serviceId;

        /**
         * 请求体
         */
        private Object requestData;

    public RpcRequest() {
    }

    public RpcRequest(String requestId, String requestData, String serviceId) {
        this.requestId = requestId;
        this.requestData = requestData;
        this.serviceId = serviceId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getRequestData() {
        return requestData;
    }

    public void setRequestData(Object requestData) {
        this.requestData = requestData;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
