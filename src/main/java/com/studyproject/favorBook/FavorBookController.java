package com.studyproject.favorBook;

import com.studyproject.account.CurrentUser;
import com.studyproject.domain.Account;
import com.studyproject.domain.FavorBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class FavorBookController {

    private final FavorBookRepository favorBookRepository;

    @GetMapping("/favorBook/list/{nickname}")
    public String favorBookList(@CurrentUser Account account, Model model) {
        List<FavorBook> favorBookList = favorBookRepository.findFavorBookByAccountId(account.getId());
        model.addAttribute("account", account);
        model.addAttribute("favorBookList", favorBookList);
        return "favorBook/list";
    }
}
