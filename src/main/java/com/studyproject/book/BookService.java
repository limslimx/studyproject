package com.studyproject.book;

import com.studyproject.crawling.CrawlingService;
import com.studyproject.domain.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class BookService {

    private final RedisBookRepository redisBookRepository;
    private final BookRepository bookRepository;
    private final CrawlingService crawlingService;

    public List<Book> getBookInfo(String searchBy) throws Exception {
        List<Book> bookList = null;
        String key = "BOOK_SEARCH_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "_" + searchBy;

        //redis에 키가 존재하면 redis에서 값 가져오고 ttl 설정함. redis에 키가 존재하지 않으면 db에서 값 가져오고 그래도 없으면 크롤링해서 다시 가져와서 db에 넣고 조회함
        if (redisBookRepository.getExists(key)) {
            bookList = redisBookRepository.getBookInfoFromRedis(key);

            if (bookList == null) {
                bookList = new ArrayList<Book>();
            }
            redisBookRepository.setTimeOutHour(key, 6);
        } else {
            bookList = bookRepository.findBySearchDateAndSearchBy(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), searchBy);

            if (bookList == null) {
                bookList = new ArrayList<Book>();
            }
            if (bookList.size() == 0) {
                bookList = crawlingService.getBookInfoFromCrawling(searchBy);

                if (bookList == null) {
                    bookList = new ArrayList<Book>();
                }
            }
            if (bookList.size() > 0) {
                redisBookRepository.setBookInfoInRedis(key, bookList);
                redisBookRepository.setTimeOutHour(key, 6);
            }
        }
        return bookList;
    }
}
