package com.studyproject.book;

import com.studyproject.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findBySearchDateAndSearchBy(String yyyyMMdd, String searchBy);
}
