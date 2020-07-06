package com.studyproject.crawling;

import com.studyproject.domain.Music;
import com.studyproject.music.MusicRepository;
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
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CrawlingService {

    private final MusicRepository musicRepository;

    public List<Music> musicSearchCrawling(String searchName) throws Exception {
        String url = "https://music.naver.com/search/search.nhn?query=" + searchName + "&target=track";
        Document doc = Jsoup.connect(url).get();

        List<Music> musicList = new ArrayList<>();
        int i=1;
        for (int n = 2; n < 52; n++) {
            Elements trElement = doc.select("tr._tracklist_move:nth-child(" + n + ")");

            String number = trElement.select("td.order").text();
            log.info(number);
            String name = trElement.select("td.name a._title span.ellipsis").text();
            String detailUrl = "https://music.naver.com" + trElement.select("td.name a.thumb").attr("href");
            String img = Jsoup.connect(detailUrl).get().select("div#content div.thumb a img").attr("src");
            String genre = "발라드";
            String artist = trElement.select("td._artist a._artist span.ellipsis").text();
            log.info(trElement.select("td.name a._title").attr("href"));
            String lyricUrl = "https://music.naver.com/lyric/index.nhn?trackId=" + trElement.select("td.name a._title").attr("href").substring(1);
            Document lyricDoc = Jsoup.connect(lyricUrl).get();
            String lyric = lyricDoc.select("div.section_lyrics div#lyricText").text();

            log.info("------------------------------");
            log.info(i + "번째 노래 크롤링 시작");
            log.info("번호: " + number);
            log.info("제목: " + name);
            log.info("상세 URL: " + detailUrl);
            log.info("이미지: " + img);
            log.info("장르: " + genre);
            log.info("가수: " + artist);
            log.info("------------------------------");
            i++;

            Music music = Music.builder()
                    .name(name)
                    .img(img)
                    .genre(genre)
                    .artist(artist)
                    .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                    .build();

            musicRepository.save(music);
            musicList.add(music);
        }
        return musicList;
    }

    public List<Music> getSearchByLyric(String keyword) throws IOException {

        List<Music> musicList = new ArrayList<Music>();
        String url = "https://music.naver.com/search/search.nhn?query=" + keyword + "&target=lyric";
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select("ul.lst_detail5 li");
        for (Element element : elements) {
            String name = element.select("span.ico_play a").text();
            String lyric = element.select("p").text();
            String artist = element.select("span.dsc a").text();

            log.info("제목: " + name);
            log.info("가사: " + lyric);
            log.info("가수: " + artist);

            Music music = Music.builder()
                    .name(name)
                    .lyric(lyric)
                    .artist(artist)
                    .searchBy(keyword)
                    .isLyric(true)
                    .searchDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                    .build();

            musicRepository.save(music);

            musicList.add(music);
        }
        return musicList;
    }
}
