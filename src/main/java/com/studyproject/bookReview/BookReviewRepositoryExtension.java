package com.studyproject.bookReview;

import com.studyproject.domain.BookReview;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface BookReviewRepositoryExtension {

    List<BookReview> findByKeyword(String keyword);
}
