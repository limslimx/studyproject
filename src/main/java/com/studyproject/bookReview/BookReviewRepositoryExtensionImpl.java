package com.studyproject.bookReview;

import com.studyproject.domain.BookReview;
import com.studyproject.domain.QBookReview;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BookReviewRepositoryExtensionImpl extends QuerydslRepositorySupport implements BookReviewRepositoryExtension {

    public BookReviewRepositoryExtensionImpl() {
        super(BookReview.class);
    }

    @Override
    public List<BookReview> findByKeyword(String keyword) {
        QBookReview bookReview = QBookReview.bookReview;
        return null;
    }
}
