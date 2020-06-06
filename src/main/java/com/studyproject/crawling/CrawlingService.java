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
import java.util.Iterator;
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

            //소제목
            String subName = doc2.select("h1.title span.back").text();
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
                        .subName(subName)
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

    public void getBestCellar() throws IOException {
        String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=A&range=1&kind=0&orderClick=DAa&targetPage=1";
        Document doc = Jsoup.connect(url).get();

        Elements elements = doc.select("ul.list_type01");

        Iterator<Element> bookList = elements.select(" > li").iterator();

        while (bookList.hasNext()) {
            Element bookInfo = bookList.next();
            String rank = bookInfo.select("div.cover a strong.rank").text();
            String img = bookInfo.select("div.cover a img").attr("src");
            String bookUrl = bookInfo.select("div.detail div.title a").attr("href");
            String subTitle = bookInfo.select("div.detail div.subtitle").text();
            String title = bookInfo.select("div.detail div.title a strong").text();

            Document doc2 = Jsoup.connect(bookUrl).get();
            Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
            String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
            int categoryCount = bookDetailInfo.select("div.rank a:nth-child(3)").text().indexOf(" ");
            String category = bookDetailInfo.select("div.rank a:nth-child(3)").text().substring(0, categoryCount).trim();
            String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
        }
    }
}
