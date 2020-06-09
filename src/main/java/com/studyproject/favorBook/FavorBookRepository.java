package com.studyproject.favorBook;

import com.studyproject.domain.FavorBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavorBookRepository extends JpaRepository<FavorBook, Long> {

    @Query("select fb from FavorBook fb where fb.book.name=:bookName and fb.account.id=:accountId")
    FavorBook findByBookNameAndAccountId(@Param("bookName") String bookName, @Param("accountId") Long accountId);
}
