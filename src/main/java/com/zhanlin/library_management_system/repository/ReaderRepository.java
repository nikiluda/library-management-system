package com.zhanlin.library_management_system.repository;
import com.zhanlin.library_management_system.models.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long>, JpaSpecificationExecutor<Reader> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

}
