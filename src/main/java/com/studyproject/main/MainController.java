package com.studyproject.main;

import com.studyproject.account.CurrentUser;
import com.studyproject.domain.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class MainController {

    @GetMapping("/")
    public String index(@CurrentUser Account account, Model model) {
        if (account != null) {
            model.addAttribute(account);
        }
        return "index";
    }

    //로그인 폼 핸들러
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
