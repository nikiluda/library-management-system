package com.zhanlin.library_management_system.repository;

import com.zhanlin.library_management_system.models.LibraryUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<LibraryUser, Long> {

    Optional<LibraryUser> findByEmail(String email);

    boolean existsByEmail(String email);
}
