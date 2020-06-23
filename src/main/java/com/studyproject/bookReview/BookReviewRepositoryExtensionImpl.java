package com.studyproject.bookReview;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import com.studyproject.domain.BookReview;
import com.studyproject.domain.QBookReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BookReviewRepositoryExtensionImpl extends QuerydslRepositorySupport implements BookReviewRepositoryExtension {

    public BookReviewRepositoryExtensionImpl() {
        super(BookReview.class);
    }

    @Override
    public Page<BookReview> findByKeyword(String keyword, Pageable pageable) {
        QBookReview bookReview = QBookReview.bookReview;
        JPQLQuery<BookReview> query = from(bookReview).where(bookReview.isOpen.isTrue()
                .and(bookReview.account.id.isNotNull())
                .and(bookReview.book.name.containsIgnoreCase(keyword)));

        JPQLQuery<BookReview> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<BookReview> fetchResults = pageableQuery.fetchResults();
        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }
}
