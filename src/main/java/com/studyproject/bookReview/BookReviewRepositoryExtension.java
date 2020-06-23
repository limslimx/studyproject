package com.studyproject.bookReview;

import com.studyproject.domain.BookReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface BookReviewRepositoryExtension {

    Page<BookReview> findByKeyword(String keyword, Pageable pageable);
}
