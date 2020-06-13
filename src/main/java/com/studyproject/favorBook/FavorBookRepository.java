package com.studyproject.favorBook;

import com.studyproject.domain.FavorBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavorBookRepository extends JpaRepository<FavorBook, Long> {

    FavorBook findByBookNameAndAccountId(String bookName, Long accountId);

    @Query("select fb.bookName from FavorBook fb where fb.account.id=:id")
    List<String> findBookNameByAccountId(@Param("id") Long id);

    List<FavorBook> findFavorBookByAccountId(Long accountId);
}
