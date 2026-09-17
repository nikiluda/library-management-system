package com.zhanlin.library_management_system.mappers;

import com.zhanlin.library_management_system.dto.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageMapper {

    public PageResponse toPageResponse(Page page) {

        PageResponse pageResponse = new PageResponse();


        PageResponse.Pagination pagination = new PageResponse.Pagination(
                page.getTotalElements(),
                page.getSize(),
                page.getNumber(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );

        pageResponse.setContent(page.getContent());
        pageResponse.setPagination(pagination);

        return pageResponse;

    }
}
