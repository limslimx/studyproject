package com.studyproject.book;

import com.studyproject.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findBySearchDateAndSearchBy(String yyyyMMdd, String searchBy);

    Book findBookById(Long id);

    @Query("select b from Book b where b.searchDate=:searchDate and b.bestCellar=:bestCellar and b.category='문학'")
    List<Book> findLiterature(@Param("searchDate") String searchDate, @Param("bestCellar") boolean bestCellar);

    @Query("select b from Book b where b.searchDate=:searchDate and b.bestCellar=:bestCellar and b.category='인문'")
    List<Book> findHumanities(@Param("searchDate") String searchDate, @Param("bestCellar") boolean bestCellar);

    @Query("select b from Book b where b.searchDate=:searchDate and b.bestCellar=:bestCellar and b.category='실용'")
    List<Book> findReal(@Param("searchDate") String searchDate, @Param("bestCellar") boolean bestCellar);

    @Query("select b from Book b where b.searchDate=:searchDate and b.bestCellar=:bestCellar and b.category='자기계발'")
    List<Book> findSelfDevelopment(@Param("searchDate") String searchDate, @Param("bestCellar") boolean bestCellar);

    int countBySearchDateAndDetailCategoryAndBestCellar(String searchDate, String detailCategory, boolean bestCellar);

    Book findOneByNameAndSearchDate(String bookName, String yyyyMMdd);
}
