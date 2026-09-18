package com.zhanlin.library_management_system.dto;

public record BookFilterDto(
        String title,
        String author,
        String isbn,
        Integer publicationYear,
        Boolean available
){
}
