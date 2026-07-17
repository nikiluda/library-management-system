package com.zhanlin.library_management_system.repository;

import com.zhanlin.library_management_system.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepository extends JpaRepository<Book,Long> {
}
