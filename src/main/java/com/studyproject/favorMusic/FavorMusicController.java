package com.studyproject.favorMusic;

import com.studyproject.account.CurrentUser;
import com.studyproject.domain.Account;
import com.studyproject.domain.FavorMusic;
import com.studyproject.music.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class FavorMusicController {

    private final FavorMusicRepository favorMusicRepository;
    private final MusicService musicService;

    @GetMapping("/favorMusic/list")
    public String favorMusicList(@CurrentUser Account account, Model model) {
        List<FavorMusic> favorMusicList = favorMusicRepository.findFavorMusicByAccountId(account.getId());
        model.addAttribute("account", account);
        model.addAttribute("favorMusicList", favorMusicList);
        return "favorMusic/list";
    }

    //관심음악 추가 기능 핸들러
    @PostMapping("/favorMusic/add/{musicName}")
    public @ResponseBody String favorMusicAdd(@CurrentUser Account account, @PathVariable String musicName) {
        musicService.addFavorMusic(account, musicName);
        return musicName;
    }

    //관심음악 삭제 기능 핸들러
    @PostMapping("/favorMusic/delete/{musicName}")
    public @ResponseBody String favorMusicDelete(@CurrentUser Account account, @PathVariable String musicName) {
        musicService.deleteFavorMusic(account, musicName);
        return musicName;
    }
}
