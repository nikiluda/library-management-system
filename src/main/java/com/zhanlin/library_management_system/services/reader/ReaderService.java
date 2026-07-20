package com.zhanlin.library_management_system.services.reader;

import com.zhanlin.library_management_system.dto.reader.ReaderRequestDto;
import com.zhanlin.library_management_system.dto.reader.ReaderResponseDto;


public interface ReaderService {

    ReaderResponseDto createReader(ReaderRequestDto dto);

    ReaderResponseDto getReaderById(Long id);

    ReaderResponseDto updateReader(Long id, ReaderRequestDto dto);

    void deleteReader(Long id);
}
