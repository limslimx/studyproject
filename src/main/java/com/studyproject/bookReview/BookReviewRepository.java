package com.studyproject.bookReview;

import com.studyproject.domain.Account;
import com.studyproject.domain.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(readOnly = true)
public interface BookReviewRepository extends JpaRepository<BookReview, Long> {

    List<BookReview> findByAccount(Account account);

    BookReview findBookReviewById(Long id);
}
