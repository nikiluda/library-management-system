package com.zhanlin.library_management_system.service;

import com.zhanlin.library_management_system.dto.PageResponse;
import com.zhanlin.library_management_system.dto.reader.ReaderRequestDto;
import com.zhanlin.library_management_system.dto.reader.ReaderResponseDto;
import org.springframework.data.domain.Pageable;


public interface ReaderService {

    ReaderResponseDto createReader(ReaderRequestDto dto);

    ReaderResponseDto getReaderById(Long id);

    ReaderResponseDto updateReader(Long id, ReaderRequestDto dto);

    void deleteReader(Long id);

    PageResponse<ReaderResponseDto> getAllReaders(Pageable pageable);
}
