package com.studyproject.music;

import com.studyproject.domain.Music;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicRepository extends JpaRepository<Music, Long> {

    List<Music> findBySearchDateAndSearchBy(String searchDate, String searchBy);

    List<Music> findBySearchDateAndGenre(String searchDate, String genre);

    Music findMusicById(Long id);

    Music findTop1ByName(String musicName);

    List<Music> findTop3BySearchDateAndGenre(String searchDate, String genre);
}
