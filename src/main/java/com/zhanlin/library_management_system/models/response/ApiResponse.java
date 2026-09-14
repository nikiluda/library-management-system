package com.zhanlin.library_management_system.models.response;

public class ApiResponse<P> {

    private String message;

    private P payload;

    private boolean success;

    public ApiResponse(String empty, P payload, boolean b) {
        this.message = message;
        this.payload = payload;
        this.success = success;
    }

    public ApiResponse() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public P getPayload() {
        return payload;
    }

    public void setPayload(P payload) {
        this.payload = payload;
    }

    public static <P> ApiResponse<P> createSuccessful(P payload) {
        return new ApiResponse<>("", payload, true);
    }


}
