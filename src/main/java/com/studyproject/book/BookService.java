package com.studyproject.book;

import com.studyproject.crawling.CrawlingService;
import com.studyproject.domain.Account;
import com.studyproject.domain.Book;
import com.studyproject.domain.FavorBook;
import com.studyproject.favorBook.FavorBookRepository;
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
    private final FavorBookRepository favorBookRepository;
    private final CrawlingService crawlingService;

    //도서 검색 기능
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

    //도서 베스트셀러 조회 기능
    public List<Book> getCategoryBookBestCellarInfo(String category) throws Exception {
        List<Book> bookList = null;
        if (category.equals("문학")) {
            bookList = bookRepository.findLiterature(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), true);
        } else if (category.equals("인문")) {
            bookList = bookRepository.findHumanities(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), true);
        } else if (category.equals("실용")) {
            bookList = bookRepository.findReal(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), true);
        } else if (category.equals("자기계발")) {
            bookList = bookRepository.findSelfDevelopment(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), true);
        }


        if (bookList == null) {
            bookList = new ArrayList<Book>();
        }
        if (bookList.size() == 0) {
            if (category.equals("문학")) {
                crawlingService.getCategoryBookBestCellarFromCrawling("B");
                crawlingService.getCategoryBookBestCellarFromCrawling("C");
                crawlingService.getCategoryBookBestCellarFromCrawling("F");
                bookList = bookRepository.findLiterature(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), true);
            } else if (category.equals("인문")) {
                crawlingService.getCategoryBookBestCellarFromCrawling("I");
                crawlingService.getCategoryBookBestCellarFromCrawling("J");
                crawlingService.getCategoryBookBestCellarFromCrawling("K");
                crawlingService.getCategoryBookBestCellarFromCrawling("b");
                bookList = bookRepository.findHumanities(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), true);
            } else if (category.equals("실용")) {
                crawlingService.getCategoryBookBestCellarFromCrawling("M");
                crawlingService.getCategoryBookBestCellarFromCrawling("N");
                crawlingService.getCategoryBookBestCellarFromCrawling("Q");
                crawlingService.getCategoryBookBestCellarFromCrawling("d");
                bookList = bookRepository.findReal(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), true);
            } else if (category.equals("자기계발")) {
                crawlingService.getCategoryBookBestCellarFromCrawling("c");
                bookList = bookRepository.findSelfDevelopment(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), true);
            }

            if (bookList == null) {
                bookList = new ArrayList<Book>();
            }
        }
        return bookList;
    }

    //관심도서 추가 기능
    public void addFavorBook(Account account, Long bookId) {
            Book book = bookRepository.findBookById(bookId);
            FavorBook favorBook = FavorBook.builder()
                    .book(book)
                    .account(account)
                    .build();
            favorBookRepository.save(favorBook);
    }

    //도서가 db에 있는지 확인하는 메서드
    public boolean validateBookById(Long bookId) {
        return bookRepository.existsById(bookId);
    }

    //관심도서가 db에 있는지 확인하는 메서드
    public boolean validateFavorBookById(Long bookId, Account account) {
        String bookName = bookRepository.findBookById(bookId).getName();
        FavorBook favorBook = favorBookRepository.findByBookNameAndAccountId(bookName, account.getId());
        if (favorBook == null) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteFavorBook(Account account, Long bookId) {
        String bookName = bookRepository.findBookById(bookId).getName();
        FavorBook favorBook = favorBookRepository.findByBookNameAndAccountId(bookName, account.getId());
        favorBookRepository.delete(favorBook);
    }
}
