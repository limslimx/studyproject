package com.studyproject.bookReview;

import com.studyproject.account.AccountRepository;
import com.studyproject.book.BookRepository;
import com.studyproject.domain.Account;
import com.studyproject.domain.Book;
import com.studyproject.domain.BookReview;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
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
    private final ModelMapper modelMapper;

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

    public BookReview getBookReviewToUpdate(Account account, Long bookReviewId) {
        BookReview bookReview = this.getBookReview(bookReviewId);
        if (!account.isManagerOfBookReview(bookReview)) {
            throw new AccessDeniedException("독서록 수정 기능을 사용할 수 없습니다.");
        }
        return bookReview;
    }

    public BookReview getBookReview(Long bookReviewId) {
        BookReview bookReview = bookReviewRepository.findBookReviewById(bookReviewId);
        if (bookReview == null) {
            throw new IllegalArgumentException(bookReviewId + "에 해당하는 독서록이 존재하지 않습니다.");
        }
        return bookReview;
    }

    public void updateBookReview(BookReview bookReview, BookReviewForm bookReviewForm) {
        modelMapper.map(bookReviewForm, bookReview);
    }
}
