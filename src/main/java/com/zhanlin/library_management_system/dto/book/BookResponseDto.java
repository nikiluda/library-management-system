package com.zhanlin.library_management_system.dto.book;

public record BookResponseDto(
        Long id,
        String title,
        String author,
        String isbn,
        Integer publicationYear,
        Integer totalCopies,
        Integer availableCopies
) {
}
