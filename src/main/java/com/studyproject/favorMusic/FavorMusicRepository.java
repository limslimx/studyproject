package com.studyproject.favorMusic;

import com.studyproject.domain.FavorMusic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavorMusicRepository extends JpaRepository<FavorMusic, Long> {

    List<FavorMusic> findFavorMusicByAccountId(Long id);

    @Query("select fm.musicName from FavorMusic fm where fm.account.id=:id")
    List<String> findMusicNameByAccountId(@Param("id") Long id);

    FavorMusic findByMusicNameAndAccountId(String musicName, Long id);
}
