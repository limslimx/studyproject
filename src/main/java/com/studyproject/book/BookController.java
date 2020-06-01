package com.studyproject.book;

import com.studyproject.account.CurrentUser;
import com.studyproject.book.form.BookSearchForm;
import com.studyproject.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class BookController {

    @GetMapping("/book/search")
    public String bookSearchForm(@CurrentUser Account account, Model model) {
        model.addAttribute("account", account);
        model.addAttribute("bookSearchForm", new BookSearchForm());
        return "/book/search";
    }

    @PostMapping("/book/search")
    public String bookSearch(@CurrentUser Account account, @Valid BookSearchForm bookSearchForm, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("account", account);
            model.addAttribute("bookSearchForm", bookSearchForm);
            return "/book/search";
        }
        //TODO 도서 크롤링해서 RDB 또는 Redis에 넣고 화면단에 보여주는 기능 구현
        return null;
    }
}
