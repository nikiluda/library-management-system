package com.zhanlin.library_management_system.models.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<P> {

    private String message;

    private P payload;


    public static <P> ApiResponse<P> createSuccessful(P payload) {
        return new ApiResponse<>(null, payload);
    }

    public static <P> ApiResponse<P> createSuccessful(String message, P payload) {
        return new ApiResponse<>(message, payload);
    }


}
