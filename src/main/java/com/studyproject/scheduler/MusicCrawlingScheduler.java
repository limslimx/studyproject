package com.studyproject.scheduler;

import com.studyproject.domain.Music;
import com.studyproject.music.MusicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@Component
public class MusicCrawlingScheduler {

    private final MusicRepository musicRepository;

    @Scheduled(cron = "0 0/5 2 * * *")
    public void baladCrawling() throws IOException {

        int i = 1;
        for (int page = 1; page < 3; page++) {
            String url = "https://music.naver.com/listen/genre/top100.nhn?domain=DOMESTIC&genre=K01&page=" + page;
            Document doc = Jsoup.connect(url).get();

            for (int n = 2; n < 52; n++) {
                Elements trElement = doc.select("tr._tracklist_move:nth-child(" + n + ")");

                String rank = trElement.select("td.ranking").text();
                log.info(rank);
                String name = trElement.select("td.name a._title span.ellipsis").text();
                String detailUrl = "https://music.naver.com" + trElement.select("td.name a.thumb").attr("href");
                String img = Jsoup.connect(detailUrl).get().select("div#content div.thumb a img").attr("src");
                String rate = Jsoup.connect(detailUrl).get().select("div.star span._album_rating em").text();
                String detailGenre = Jsoup.connect(detailUrl).get().select("dl.desc dd:nth-child(4)").text();
                String publicationDate = Jsoup.connect(detailUrl).get().select("dl.desc dd:nth-child(6)").text();
                String publicationBy = Jsoup.connect(detailUrl).get().select("dl.desc dd:nth-child(8)").text();
                String genre = "발라드";
                String artist = trElement.select("td._artist a._artist span.ellipsis").text();
                log.info(trElement.select("td.name a._title").attr("href"));
                String lyricUrl = "https://music.naver.com/lyric/index.nhn?trackId=" + trElement.select("td.name a._title").attr("href").substring(1);
                Document lyricDoc = Jsoup.connect(lyricUrl).get();
                String lyric = lyricDoc.select("div.section_lyrics div#lyricText").text();

                log.info("------------------------------");
                log.info(i + "번째 노래 크롤링 시작");
                log.info("순위: " + rank);
                log.info("제목: " + name);
                log.info("상세 URL: " + detailUrl);
                log.info("이미지: " + img);
                log.info("장르: " + genre);
                log.info("상세 장르: " + detailGenre);
                log.info("가수: " + artist);
                log.info("가사: " + lyric);
                log.info("평점: " + rate);
                log.info("발매일: " + publicationDate);
                log.info("배급사: " + publicationBy);
                log.info("------------------------------");
                i++;

                Music music = Music.builder()
                        .rank(rank)
                        .name(name)
                        .img(img)
                        .genre(genre)
                        .artist(artist)
                        .lyric(lyric)
                        .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .detailGenre(detailGenre)
                        .rate(rate)
                        .publicationDate(publicationDate)
                        .publicationBy(publicationBy)
                        .build();

                musicRepository.save(music);
            }
        }
    }

    @Scheduled(cron = "0 0/5 2 * * *")
    public void trotCrawling() throws IOException {

        int i = 1;
        for (int page = 1; page < 3; page++) {
            String url = "https://music.naver.com/listen/genre/top100.nhn?domain=DOMESTIC&genre=K05&page=" + page;
            Document doc = Jsoup.connect(url).get();

            for (int n = 2; n < 52; n++) {
                Elements trElement = doc.select("tr._tracklist_move:nth-child(" + n + ")");

                String rank = trElement.select("td.ranking").text();
                log.info(rank);
                String name = trElement.select("td.name a._title span.ellipsis").text();
                String detailUrl = "https://music.naver.com" + trElement.select("td.name a.thumb").attr("href");
                String img = Jsoup.connect(detailUrl).get().select("div#content div.thumb a img").attr("src");
                String rate = Jsoup.connect(detailUrl).get().select("div.star span._album_rating em").text();
                String detailGenre = Jsoup.connect(detailUrl).get().select("dl.desc dd:nth-child(4)").text();
                String publicationDate = Jsoup.connect(detailUrl).get().select("dl.desc dd:nth-child(6)").text();
                String publicationBy = Jsoup.connect(detailUrl).get().select("dl.desc dd:nth-child(8)").text();
                String genre = "트로트";
                String artist = trElement.select("td._artist a._artist span.ellipsis").text();
                log.info(trElement.select("td.name a._title").attr("href"));
                String lyricUrl = "https://music.naver.com/lyric/index.nhn?trackId=" + trElement.select("td.name a._title").attr("href").substring(1);
                Document lyricDoc = Jsoup.connect(lyricUrl).get();
                String lyric = lyricDoc.select("div.section_lyrics div#lyricText").text();

                log.info("------------------------------");
                log.info(i + "번째 노래 크롤링 시작");
                log.info("순위: " + rank);
                log.info("제목: " + name);
                log.info("상세 URL: " + detailUrl);
                log.info("이미지: " + img);
                log.info("장르: " + genre);
                log.info("상세 장르: " + detailGenre);
                log.info("가수: " + artist);
                log.info("가사: " + lyric);
                log.info("평점: " + rate);
                log.info("발매일: " + publicationDate);
                log.info("배급사: " + publicationBy);
                log.info("------------------------------");
                i++;

                Music music = Music.builder()
                        .rank(rank)
                        .name(name)
                        .img(img)
                        .genre(genre)
                        .artist(artist)
                        .lyric(lyric)
                        .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .detailGenre(detailGenre)
                        .rate(rate)
                        .publicationDate(publicationDate)
                        .publicationBy(publicationBy)
                        .build();

                musicRepository.save(music);
            }
        }
    }

    @Scheduled(cron = "0 0/5 2 * * *")
    public void hiphopCrawling() throws IOException {

        int i = 1;
        for (int page = 1; page < 3; page++) {
            String url = "https://music.naver.com/listen/genre/top100.nhn?domain=DOMESTIC&genre=K03&page=" + page;
            Document doc = Jsoup.connect(url).get();

            for (int n = 2; n < 52; n++) {
                Elements trElement = doc.select("tr._tracklist_move:nth-child(" + n + ")");

                String rank = trElement.select("td.ranking").text();
                log.info(rank);
                String name = trElement.select("td.name a._title span.ellipsis").text();
                String detailUrl = "https://music.naver.com" + trElement.select("td.name a.thumb").attr("href");
                String img = Jsoup.connect(detailUrl).get().select("div#content div.thumb a img").attr("src");
                String rate = Jsoup.connect(detailUrl).get().select("div.star span._album_rating em").text();
                String detailGenre = Jsoup.connect(detailUrl).get().select("dl.desc dd:nth-child(4)").text();
                String publicationDate = Jsoup.connect(detailUrl).get().select("dl.desc dd:nth-child(6)").text();
                String publicationBy = Jsoup.connect(detailUrl).get().select("dl.desc dd:nth-child(8)").text();
                String genre = "힙합";
                String artist = trElement.select("td._artist a._artist span.ellipsis").text();
                log.info(trElement.select("td.name a._title").attr("href"));
                String lyricUrl = "https://music.naver.com/lyric/index.nhn?trackId=" + trElement.select("td.name a._title").attr("href").substring(1);
                Document lyricDoc = Jsoup.connect(lyricUrl).get();
                String lyric = lyricDoc.select("div.section_lyrics div#lyricText").text();

                log.info("------------------------------");
                log.info(i + "번째 노래 크롤링 시작");
                log.info("순위: " + rank);
                log.info("제목: " + name);
                log.info("상세 URL: " + detailUrl);
                log.info("이미지: " + img);
                log.info("장르: " + genre);
                log.info("상세 장르: " + detailGenre);
                log.info("가수: " + artist);
                log.info("가사: " + lyric);
                log.info("평점: " + rate);
                log.info("발매일: " + publicationDate);
                log.info("배급사: " + publicationBy);
                log.info("------------------------------");
                i++;

                Music music = Music.builder()
                        .rank(rank)
                        .name(name)
                        .img(img)
                        .genre(genre)
                        .artist(artist)
                        .lyric(lyric)
                        .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .detailGenre(detailGenre)
                        .rate(rate)
                        .publicationDate(publicationDate)
                        .publicationBy(publicationBy)
                        .build();

                musicRepository.save(music);
            }
        }
    }

    @Scheduled(cron = "0 0/5 2 * * *")
    public void popCrawling() throws IOException {

        int i = 1;
        for (int page = 1; page < 3; page++) {
            String url = "https://music.naver.com/listen/top100.nhn?domain=OVERSEA_V2&page=" + page;
            Document doc = Jsoup.connect(url).get();

            for (int n = 2; n < 52; n++) {
                Elements trElement = doc.select("tr._tracklist_move:nth-child(" + n + ")");

                String rank = trElement.select("td.ranking").text();
                log.info(rank);
                String name = trElement.select("td.name a._title span.ellipsis").text();
                String detailUrl = "https://music.naver.com" + trElement.select("td.name a.thumb").attr("href");
                String img = Jsoup.connect(detailUrl).get().select("div#content div.thumb a img").attr("src");
                String rate = Jsoup.connect(detailUrl).get().select("div.star span._album_rating em").text();
                String detailGenre = Jsoup.connect(detailUrl).get().select("dl.desc dd:nth-child(4)").text();
                String publicationDate = Jsoup.connect(detailUrl).get().select("dl.desc dd:nth-child(6)").text();
                String publicationBy = Jsoup.connect(detailUrl).get().select("dl.desc dd:nth-child(8)").text();
                String genre = "팝송";
                String artist = trElement.select("td._artist a._artist span.ellipsis").text();
                log.info(trElement.select("td.name a._title").attr("href"));
                String lyricUrl = "https://music.naver.com/lyric/index.nhn?trackId=" + trElement.select("td.name a._title").attr("href").substring(1);
                Document lyricDoc = Jsoup.connect(lyricUrl).get();
                String lyric = lyricDoc.select("div.section_lyrics div#lyricText").text();

                log.info("------------------------------");
                log.info(i + "번째 노래 크롤링 시작");
                log.info("순위: " + rank);
                log.info("제목: " + name);
                log.info("상세 URL: " + detailUrl);
                log.info("이미지: " + img);
                log.info("장르: " + genre);
                log.info("상세 장르: " + detailGenre);
                log.info("가수: " + artist);
                log.info("가사: " + lyric);
                log.info("평점: " + rate);
                log.info("발매일: " + publicationDate);
                log.info("배급사: " + publicationBy);
                log.info("------------------------------");
                i++;

                Music music = Music.builder()
                        .rank(rank)
                        .name(name)
                        .img(img)
                        .genre(genre)
                        .artist(artist)
                        .lyric(lyric)
                        .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .detailGenre(detailGenre)
                        .rate(rate)
                        .publicationDate(publicationDate)
                        .publicationBy(publicationBy)
                        .build();

                musicRepository.save(music);
            }
        }
    }
}
