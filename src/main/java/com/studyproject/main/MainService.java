package com.studyproject.main;

import com.studyproject.book.BookRepository;
import com.studyproject.bookReview.BookReviewRepository;
import com.studyproject.domain.Account;
import com.studyproject.domain.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MainService {

    private final BookReviewRepository bookReviewRepository;
    private final BookRepository bookRepository;

    public List<Book> bookRecommend(Account account) {
        Long count = 0L;
        String bookCategory = null;
        List<BookRecommendDto> bookRecommendDtoList = bookReviewRepository.findBookDetailCategoryAndCount(account);
        for (int i = 0; i < bookRecommendDtoList.size(); i++) {
            log.info(i + "번째 bookCategory는 " + bookRecommendDtoList.get(i).getBookCategory());
            log.info(i + "번째 bookCategoryCount는 " + bookRecommendDtoList.get(i).getBookCategoryCount());
            if (bookRecommendDtoList.get(i).getBookCategoryCount() > count) {
                count = bookRecommendDtoList.get(i).getBookCategoryCount();
                bookCategory = bookRecommendDtoList.get(i).getBookCategory();
            }
        }
        log.info("최종 결과: " + bookCategory);

        List<Book> bookRecommendList = bookRepository.findTop3BySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), bookCategory, true);
        return bookRecommendList;
    }
}
