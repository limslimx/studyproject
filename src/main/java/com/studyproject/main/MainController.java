package com.studyproject.main;

import com.studyproject.account.CurrentUser;
import com.studyproject.domain.Account;
import com.studyproject.domain.Music;
import com.studyproject.favorMusic.FavorMusicRepository;
import com.studyproject.music.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final MainService mainService;
    private final MusicService musicService;
    private final FavorMusicRepository favorMusicRepository;

    @GetMapping("/")
    public String index(@CurrentUser Account account, Model model) {
        if (account != null) {
            List<Music> musicRecommendList = mainService.musicRecommend();
            List<String> favorMusicList = favorMusicRepository.findMusicNameByAccountId(account.getId());
            model.addAttribute("musicRecommendList", musicRecommendList);
            model.addAttribute("favorMusicList", favorMusicList);
            model.addAttribute(account);
        }

        return "index";
    }

    //로그인 폼 핸들러
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/search/musicByLyric")
    public String searchByLyric(String keyword, Model model) throws IOException {
        List<Music> musicList = musicService.getSearchByLyric(keyword);
        model.addAttribute("musicList", musicList);
        return "/music/searchResult";
    }
}
