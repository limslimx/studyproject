package com.studyproject.music;

import com.studyproject.crawling.CrawlingService;
import com.studyproject.domain.Account;
import com.studyproject.domain.FavorMusic;
import com.studyproject.domain.Music;
import com.studyproject.favorMusic.FavorMusicRepository;
import lombok.RequiredArgsConstructor;
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
public class MusicService {

    private final RedisMusicRepository redisMusicRepository;
    private final MusicRepository musicRepository;
    private final FavorMusicRepository favorMusicRepository;
    private final CrawlingService crawlingService;

    //redis를 이용한 음악 검색 기능
    public List<Music> getMusicInfo(String searchBy) throws Exception {
        List<Music> musicList = null;
        String key = "MUSIC_SEARCH_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "_" + searchBy;

        //redis에 키가 존재하면 redis에서 값 가져오고 ttl 설정함. redis에 키가 존재하지 않으면 db에서 값 가져오고 그래도 없으면 크롤링해서 다시 가져와서 db에 넣고 조회함
        if (redisMusicRepository.getExists(key)) {
            musicList = redisMusicRepository.getBookInfoFromRedis(key);

            if (musicList == null) {
                musicList = new ArrayList<Music>();
            }
            redisMusicRepository.setTimeOutHour(key, 6);
        } else {
            musicList = musicRepository.findBySearchDateAndSearchBy(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), searchBy);

            if (musicList == null) {
                musicList = new ArrayList<Music>();
            }
            if (musicList.size() == 0) {
                musicList = crawlingService.musicSearchCrawling(searchBy);

                if (musicList == null) {
                    musicList = new ArrayList<Music>();
                }
            }
            if (musicList.size() > 0) {
                redisMusicRepository.setBookInfoInRedis(key, musicList);
                redisMusicRepository.setTimeOutHour(key, 6);
            }
        }
        return musicList;
    }

//    //redis없이 크롤링 기능 메서드
//    public List<Book> getBookInfo(String searchBy, Account account) throws IOException {
//        List<Book> bookList = bookRepository.findBySearchDateAndSearchBy(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), searchBy);
//
//        if (bookList == null) {
//            bookList = new ArrayList<Book>();
//        }
//        if (bookList.size() == 0) {
//            bookList = crawlingService.getBookInfoFromCrawling(searchBy, account);
//
//            if (bookList == null) {
//                bookList = new ArrayList<Book>();
//            }
//        }
//        return bookList;
//    }

    //해당 책이 Book에 존재하는지 확인하는 메서드
    private Music musicExistsValidation(String musicName) {
        Music music = musicRepository.findTop1ByName(musicName);
        if (music == null) {
            throw new IllegalArgumentException(musicName + "라는 이름을 가진 음악이 존재하지 않습니다.");
        }
        return music;
    }

    //관심음악 추가 기능
    public void addFavorMusic(Account account, String musicName) {
        Music music = musicExistsValidation(musicName);
        FavorMusic favorMusic = FavorMusic.builder()
                .musicName(musicName)
                .music(music)
                .account(account)
                .build();
        favorMusicRepository.save(favorMusic);
    }

    //관심음악 삭제 기능
    public void deleteFavorMusic(Account account, String musicName) {
        musicExistsValidation(musicName);
        FavorMusic favorMusic = favorMusicRepository.findByMusicNameAndAccountId(musicName, account.getId());
        favorMusicRepository.delete(favorMusic);
    }

    public List<Music> getSearchByLyric(String keyword) throws IOException {
        return crawlingService.getSearchByLyric(keyword);
    }
}
