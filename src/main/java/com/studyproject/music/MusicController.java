package com.studyproject.music;

import com.studyproject.account.CurrentUser;
import com.studyproject.domain.Account;
import com.studyproject.domain.Music;
import com.studyproject.favorMusic.FavorMusicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MusicController {

    private final MusicService musicService;
    private final MusicRepository musicRepository;
    private final FavorMusicRepository favorMusicRepository;

    @GetMapping("/music/search")
    public String musicSearchForm(@CurrentUser Account account, Model model) {
        model.addAttribute("account", account);
        model.addAttribute("musicSearchForm", new MusicSearchForm());
        return "music/search";
    }

    @PostMapping("/music/search/save")
    public String musicSearch(@CurrentUser Account account, @Valid MusicSearchForm musicSearchForm, Errors errors, Model model, RedirectAttributes attributes) throws Exception {
        if (errors.hasErrors()) {
            model.addAttribute("account", account);
            model.addAttribute("musicSearchForm", musicSearchForm);
            return "/music/search";
        }
        List<Music> musicList = musicService.getMusicInfo(musicSearchForm.getSearchBy());
//        List<String> favorBookList = favorBookRepository.findBookNameByAccountId(account.getId());

        attributes.addFlashAttribute("musicList", musicList);
//        attributes.addFlashAttribute("favorBookList", favorBookList);

        return "redirect:/music/search";
    }

    //책 카테고리별 분류 화면 핸들러
    @GetMapping("/music/category")
    public String musicCategory(@CurrentUser Account account, Model model) {
        model.addAttribute("account", account);
        return "music/category-form";
    }

    //발라드 베스트셀러 리스트 화면 핸들러
    @GetMapping("/music/category/ballad")
    public String musicBestCellarByBallad(@CurrentUser Account account, Model model) throws Exception {
        String genre = "발라드";
        List<Music> musicList = musicRepository.findBySearchDateAndGenre(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), genre);
        List<String> favorMusicList = favorMusicRepository.findMusicNameByAccountId(account.getId());
        for (int i = 0; i < favorMusicList.size(); i++) {
            log.info(favorMusicList.get(i));
        }
        model.addAttribute("account", account);
        model.addAttribute("musicList", musicList);
        model.addAttribute("favorMusicList", favorMusicList);

        return "music/category/ballad-list";
    }

    //트로트 베스트셀러 리스트 화면 핸들러
    @GetMapping("/music/category/trot")
    public String musicBestCellarByTrot(@CurrentUser Account account, Model model) throws Exception {
        String genre = "트로트";
        List<Music> musicList = musicRepository.findBySearchDateAndGenre(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), genre);
        List<String> favorMusicList = favorMusicRepository.findMusicNameByAccountId(account.getId());
        for (int i = 0; i < favorMusicList.size(); i++) {
            log.info(favorMusicList.get(i));
        }
        model.addAttribute("account", account);
        model.addAttribute("musicList", musicList);
        model.addAttribute("favorMusicList", favorMusicList);

        return "music/category/trot-list";
    }

    //힙합 베스트셀러 리스트 화면 핸들러
    @GetMapping("/music/category/hiphop")
    public String musicBestCellarByHipHop(@CurrentUser Account account, Model model) throws Exception {
        String genre = "힙합";
        List<Music> musicList = musicRepository.findBySearchDateAndGenre(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), genre);
        List<String> favorMusicList = favorMusicRepository.findMusicNameByAccountId(account.getId());
        for (int i = 0; i < favorMusicList.size(); i++) {
            log.info(favorMusicList.get(i));
        }
        model.addAttribute("account", account);
        model.addAttribute("musicList", musicList);
        model.addAttribute("favorMusicList", favorMusicList);

        return "music/category/hiphop-list";
    }

    //팝송 베스트셀러 리스트 화면 핸들러
    @GetMapping("/music/category/pop")
    public String musicBestCellarByPop(@CurrentUser Account account, Model model) throws Exception {
        String genre = "팝송";
        List<Music> musicList = musicRepository.findBySearchDateAndGenre(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), genre);
        List<String> favorMusicList = favorMusicRepository.findMusicNameByAccountId(account.getId());
        for (int i = 0; i < favorMusicList.size(); i++) {
            log.info(favorMusicList.get(i));
        }
        model.addAttribute("account", account);
        model.addAttribute("musicList", musicList);
        model.addAttribute("favorMusicList", favorMusicList);

        return "music/category/pop-list";
    }

    @GetMapping("/music/detail/{id}")
    public String musicDetailForm(@CurrentUser Account account, @PathVariable Long id, Model model) {
        Music music = musicRepository.findMusicById(id);
        model.addAttribute("music", music);
        return "music/detail";
    }
}
