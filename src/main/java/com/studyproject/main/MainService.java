package com.studyproject.main;

import com.studyproject.domain.Music;
import com.studyproject.music.MusicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MainService {

    private final MusicRepository musicRepository;

    public List<Music> musicRecommend() {

        List<Music> musicRecommendList = new ArrayList<Music>();
        // 1 ~ 4까지 랜덤 숫자 구하기
        int random = (int) (Math.random() * 4) + 1;

        if (random == 1) {
            musicRecommendList = musicRepository.findTop3BySearchDateAndGenre(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "발라드");
        } else if (random == 2) {
            musicRecommendList = musicRepository.findTop3BySearchDateAndGenre(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "트로트");
        } else if (random == 3) {
            musicRecommendList = musicRepository.findTop3BySearchDateAndGenre(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "팝송");
        } else {
            musicRecommendList = musicRepository.findTop3BySearchDateAndGenre(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "힙합");
        }
        return musicRecommendList;
    }
}
