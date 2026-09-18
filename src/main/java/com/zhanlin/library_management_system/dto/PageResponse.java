package com.zhanlin.library_management_system.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {

    private List<T> content;
    private Pagination pagination;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pagination {

        private long totalElements;
        private int size;
        private int page;
        private int totalPages;
        private boolean first;
        private boolean last;
    }
}
