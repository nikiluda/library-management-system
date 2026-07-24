package com.zhanlin.library_management_system.repository;

import com.zhanlin.library_management_system.dto.reader.ReaderResponseDto;
import com.zhanlin.library_management_system.models.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long> {
}
