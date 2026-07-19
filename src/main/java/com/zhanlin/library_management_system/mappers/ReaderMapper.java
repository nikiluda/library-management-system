package com.zhanlin.library_management_system.mappers;

import com.zhanlin.library_management_system.dto.reader.ReaderRequestDto;
import com.zhanlin.library_management_system.dto.reader.ReaderResponseDto;
import com.zhanlin.library_management_system.models.Reader;
import org.springframework.stereotype.Component;

@Component
public class ReaderMapper {

    public Reader toEntity(ReaderRequestDto dto) {
        if (dto == null) return null;

        Reader reader = new Reader();
        reader.setFirstName(dto.firstName());
        reader.setLastName(dto.lastName());
        reader.setEmail(dto.email());
        reader.setPhone(dto.phone());
        return reader;

    }

    public ReaderResponseDto toDto(Reader reader) {
        if (reader == null) return null;

        return new ReaderResponseDto(reader.getId(), reader.getFirstName(), reader.getLastName(),
                reader.getEmail(), reader.getPhone(),reader.getRegDate()
        );
    }
}
