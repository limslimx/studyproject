package com.studyproject.favorBook;

import com.studyproject.domain.FavorBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavorBookRepository extends JpaRepository<FavorBook, Long> {

    FavorBook findByBookNameAndAccountId(String bookName, Long accountId);
}
