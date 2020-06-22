package com.studyproject.bookReview;

import com.querydsl.jpa.JPQLQuery;
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
        JPQLQuery<BookReview> query = from(bookReview).where(bookReview.isOpen.isTrue()
                .and(bookReview.account.id.isNotNull())
                .and(bookReview.title.containsIgnoreCase(keyword))
                .or(bookReview.book.name.containsIgnoreCase(keyword))
                .or(bookReview.book.subName.containsIgnoreCase(keyword))
                .or(bookReview.book.tag.containsIgnoreCase(keyword)));
        return query.fetch();
    }
}
