package com.zhanlin.library_management_system.repository;

import com.zhanlin.library_management_system.models.Reader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReaderRepository extends JpaRepository<Reader, Long> {
}
