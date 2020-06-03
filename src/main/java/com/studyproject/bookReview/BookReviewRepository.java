package com.studyproject.bookReview;

import com.studyproject.domain.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
}
