package com.studyproject.crawling;

import com.studyproject.book.BookRepository;
import com.studyproject.domain.Book;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class CrawlingService {

    private final BookRepository bookRepository;

    public List<Book> getBookInfoFromCrawling(String searchBy) throws IOException {
        List<Book> bookList = new ArrayList<Book>();
        String siteUrl = "https://search.kyobobook.co.kr/web/search?vPstrKeyWord=" + searchBy + "&orderClick=LAG";
        Document document = Jsoup.connect(siteUrl).get();
        Elements elements = document.select("tbody#search_list tr");
        for (Element element : elements) {
            //제목
            String name = element.select("div.title strong").text();
            //카테고리 --> db에는 안넣을거지만, 카테고리가 국내도서인 도서만 db에 insert하기 위한 구분값
            String category = element.select("div.category").text();
            //이미지
            Elements imageElements = element.select("div.cover img");
            String img = imageElements.attr("src");
            //상세카테고리
            String detailCategory = element.select("span.category2").text();
            //저자
            String author = element.select("div.author a:nth-child(1)").text();
            //태그
            String tag = element.select("div.tag a").text();
            //url
            Elements urlElements = element.select("div.title a");
            String url = urlElements.attr("href");

            // ---------------------------------- 여기서부터는 상세 url에서의 정보수집 -----------------------------------
            Document doc2 = Jsoup.connect(url).get();

            //출간일
            String publicationDate = doc2.select("span.date").text();
            //순위
            String rank = doc2.select("div.rank a:nth-child(3) em").text();

            //카테고리가 국내도서인 도서만 db에 저장함
            if (category.equals("국내도서")) {
                Book book = Book.builder()
                        .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .searchBy(searchBy)
                        .name(name)
                        .detailCategory(detailCategory)
                        .img(img)
                        .author(author)
                        .url(url)
                        .rank(rank)
                        .tag(tag)
                        .publicationDate(publicationDate)
                        .build();

                bookList.add(book);
                bookRepository.save(book);
            }
        }
        return bookList;
    }
}
