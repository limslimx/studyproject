package com.studyproject.bookReview;

import com.studyproject.account.AccountRepository;
import com.studyproject.book.BookRepository;
import com.studyproject.domain.Account;
import com.studyproject.domain.Book;
import com.studyproject.domain.BookReview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Transactional
@Service
public class BookReviewService {

    private final AccountRepository accountRepository;
    private final BookRepository bookRepository;
    private final BookReviewRepository bookReviewRepository;

    public void createBookReview(Account account, Long id, BookReviewForm bookReviewForm) {
        Book bookById = bookRepository.findBookById(id);
        BookReview bookReview = BookReview.builder()
                .title(bookReviewForm.getTitle())
                .content(bookReviewForm.getContent())
                .createdTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .modifiedTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .account(account)
                .book(bookById)
                .build();

        bookReviewRepository.save(bookReview);
    }
}
