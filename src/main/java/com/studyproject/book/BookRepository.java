package com.studyproject.book;

import com.studyproject.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findBySearchDateAndSearchBy(String yyyyMMdd, String searchBy);

    Book findBookById(Long id);

    @Query("select b from Book b where b.searchDate=:searchDate and b.bestCellar=:bestCellar and b.category='문학'")
    List<Book> findLiterature(String searchDate, boolean bestCellar);
}
