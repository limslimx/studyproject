package com.studyproject.crawling;

import com.studyproject.book.BookRepository;
import com.studyproject.domain.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CrawlingService {

    private final BookRepository bookRepository;

    //도서 검색 크롤링 메서드
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
                        .bestCellar(false)
                        .build();

                bookList.add(book);
                bookRepository.save(book);
            }
        }
        return bookList;
    }

    //카테고리별 도서 베스트셀러 크롤링 메서드
    public void getCategoryBookBestCellarFromCrawling(String linkClass) throws IOException {
        List<Book> bookList = new ArrayList<Book>();

        String categoryValue = "";
        String detailCategory = "";

        if (linkClass.equals("B")) {
            categoryValue = "문학";
            detailCategory = "소설";
        } else if (linkClass.equals("C")) {
            categoryValue = "문학";
            detailCategory = "에세이";
        } else if (linkClass.equals("F")) {
            categoryValue = "문학";
            detailCategory = "시";
        } else if (linkClass.equals("I")) {
            categoryValue = "인문";
            detailCategory = "인문";
        } else if (linkClass.equals("J")) {
            categoryValue = "인문";
            detailCategory = "정치사회";
        } else if (linkClass.equals("K")) {
            categoryValue = "인문";
            detailCategory = "경제경영";
        } else if (linkClass.equals("b")) {
            categoryValue = "인문";
            detailCategory = "역사문화";
        } else if (linkClass.equals("M")) {
            categoryValue = "실용";
            detailCategory = "교양과학";
        } else if (linkClass.equals("N")) {
            categoryValue = "실용";
            detailCategory = "외국어";
        } else if (linkClass.equals("Q")) {
            categoryValue = "실용";
            detailCategory = "예술";
        } else if (linkClass.equals("d")) {
            categoryValue = "실용";
            detailCategory = "여행";
        } else if (linkClass.equals("c")) {
            categoryValue = "자기계발";
            detailCategory = "자기계발";
        }

        String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=" + linkClass + "&range=1&kind=0&orderClick=DAb";
        Document doc = Jsoup.connect(url).get();

        Elements elements = doc.select("ul.list_type01");

        Iterator<Element> bookIterator = elements.select(" > li").iterator();

        int i = 1;
        while (bookIterator.hasNext()) {
            Element bookInfo = bookIterator.next();
            String rank = bookInfo.select("div.cover a strong.rank").text();
            String img = bookInfo.select("div.cover a img").attr("src");
            String bookUrl = bookInfo.select("div.detail div.title a").attr("href");
            String subTitle = bookInfo.select("div.detail div.subtitle").text();
            String title = bookInfo.select("div.detail div.title a strong").text();

            Document doc2 = Jsoup.connect(bookUrl).get();
            Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
            String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
//            int categoryCount = bookDetailInfo.select("div.rank a:nth-child(3)").text().indexOf(" ");
//            log.info("--------------------");
//            log.info(i + "번째 index");
//            log.info("text: " + bookDetailInfo.select("div.rank a:nth-child(3)").text());
//            log.info("indexOf: " + bookDetailInfo.select("div.rank a:nth-child(3)").text().indexOf(" "));
//            log.info("--------------------");
//            String category = bookDetailInfo.select("div.rank a:nth-child(3)").text().substring(0, categoryCount).trim();
            String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();

            Book book = Book.builder()
                    .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                    .searchBy(null)
                    .name(title)
                    .subName(subTitle)
                    .category(categoryValue)
                    .detailCategory(detailCategory)
                    .img(img)
                    .author(author)
                    .url(bookUrl)
                    .rank(rank)
                    .tag(tag)
                    .bestCellar(true)
                    .build();
            bookRepository.save(book);
            bookList.add(book);

            log.info("--------------------");
            log.info(i + "번째 도서 크롤링 시작");
            log.info("rank: " + rank);
            log.info("name: " + title);
            log.info("subName: " + subTitle);
            log.info("category: " + detailCategory);
            log.info("img: " + img);
            log.info("author: " + author);
            log.info("url: " + bookUrl);
            log.info("tag: " + tag);
            log.info("--------------------");
            i++;
        }
    }
}
