package com.studyproject.scheduler;

import com.studyproject.book.BookRepository;
import com.studyproject.domain.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class BookCrawlingScheduler {

    private final BookRepository bookRepository;

    //소설 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 0/5 10 * * *")
    public void literature_novelCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "소설", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "문학";
            String detailCategory = "소설";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=B&range=1&kind=0&orderClick=DAb";
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

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

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
                        .publicationDate(publicationDate)
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
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //에세이 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 0/5 10 * * *")
    public void literature_essayCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "에세이", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "문학";
            String detailCategory = "에세이";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=C&range=1&kind=0&orderClick=DAb";
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

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

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
                        .publicationDate(publicationDate)
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
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //시 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 0/5 10 * * *")
    public void literature_poemCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "시", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "문학";
            String detailCategory = "시";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=F&range=1&kind=0&orderClick=DAb";
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

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

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
                        .publicationDate(publicationDate)
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
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //인문 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 0/5 10 * * *")
    public void humanities_humanitiesCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "인문", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "인문";
            String detailCategory = "인문";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=I&range=1&kind=0&orderClick=DAb";
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

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

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
                        .publicationDate(publicationDate)
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
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //정치사회 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 0/5 10 * * *")
    public void humanities_societyCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "정치사회", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "인문";
            String detailCategory = "정치사회";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=J&range=1&kind=0&orderClick=DAb";
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

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

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
                        .publicationDate(publicationDate)
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
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //경제경영 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 0/5 10 * * *")
    public void humanities_economyCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "경제경영", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "인문";
            String detailCategory = "경제경영";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=K&range=1&kind=0&orderClick=DAb";
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

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

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
                        .publicationDate(publicationDate)
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
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //역사문화 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 0/5 10 * * *")
    public void humanities_historyCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "역사문화", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "인문";
            String detailCategory = "역사문화";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=b&range=1&kind=0&orderClick=DAb";
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

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

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
                        .publicationDate(publicationDate)
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
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //교양과학 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 0/5 10 * * *")
    public void real_scienceCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "교양과학", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "실용";
            String detailCategory = "교양과학";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=M&range=1&kind=0&orderClick=DAb";
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

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

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
                        .publicationDate(publicationDate)
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
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //외국어 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 0/5 10 * * *")
    public void real_languageCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "외국어", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "실용";
            String detailCategory = "외국어";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=N&range=1&kind=0&orderClick=DAb";
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

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

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
                        .publicationDate(publicationDate)
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
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //예술 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 0/5 10 * * *")
    public void real_artCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "예술", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "실용";
            String detailCategory = "예술";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=Q&range=1&kind=0&orderClick=DAb";
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

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

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
                        .publicationDate(publicationDate)
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
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //여행 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 0/5 10 * * *")
    public void real_tripCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "여행", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "실용";
            String detailCategory = "여행";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=d&range=1&kind=0&orderClick=DAb";
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

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

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
                        .publicationDate(publicationDate)
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
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }

    //자기계발 베스트셀러 크롤링 메서드
    @Scheduled(cron = "0 0/5 10 * * *")
    public void selfDevelopment_selfDevelopmentCrawling() throws IOException {
        int count = bookRepository.countBySearchDateAndDetailCategoryAndBestCellar(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "자기계발", true);
        log.info("#######count: "+count);
        if (count == 0) {
            log.info("---------------crawling start---------------");
            List<Book> bookList = new ArrayList<Book>();

            String categoryValue = "자기계발";
            String detailCategory = "자기계발";

            String url = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=c&range=1&kind=0&orderClick=DAb";
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

                Document doc2 = Jsoup.connect(bookUrl).get();
                Elements bookDetailInfo = doc2.select("div.content_middle div.box_detail_point");
                String title = bookDetailInfo.select("h1.title > strong").text();
                String author = bookDetailInfo.select("div.author span.name:nth-child(1) a:nth-child(1)").text();
                String tag = doc2.select("div.content_middle div.box_detail_content div.tag_list").text();
                String[] splitTag = tag.split(" ");
                if (splitTag.length > 3) {
                    tag = splitTag[0] + " " + splitTag[1] + " " + splitTag[2];
                }
                int publicationDateLength = bookDetailInfo.select("div.author span.date").text().length();
                String publicationDate = bookDetailInfo.select("div.author span.date").text().substring(0, publicationDateLength-3);

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
                        .publicationDate(publicationDate)
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
                log.info("publicationDate: " + publicationDate);
                log.info("--------------------");
                i++;
            }
            log.info("---------------crawling end---------------");
        }
    }
}

