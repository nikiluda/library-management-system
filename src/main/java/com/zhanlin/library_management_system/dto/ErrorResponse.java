package com.zhanlin.library_management_system.dto;

import java.time.LocalDateTime;


//TODO: удалить данный Response
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
