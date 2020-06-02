package com.studyproject.book;

import com.studyproject.account.CurrentUser;
import com.studyproject.book.form.BookSearchForm;
import com.studyproject.domain.Account;
import com.studyproject.domain.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BookController {

    private final BookService bookService;

    @GetMapping("/book/search")
    public String bookSearchForm(@CurrentUser Account account, Model model) {
        model.addAttribute("account", account);
        model.addAttribute("bookSearchForm", new BookSearchForm());
        return "/book/search";
    }

    @PostMapping("/book/search")
    public String bookSearch(@CurrentUser Account account, @Valid BookSearchForm bookSearchForm, Errors errors, Model model, RedirectAttributes attributes) throws Exception {
        if (errors.hasErrors()) {
            model.addAttribute("account", account);
            model.addAttribute("bookSearchForm", bookSearchForm);
            return "/book/search";
        }
        //TODO 도서 크롤링해서 RDB 또는 Redis에 넣고 화면단에 보여주는 기능 구현
        List<Book> bookList = bookService.getBookInfo(bookSearchForm.getSearchBy());
        attributes.addFlashAttribute("bookList", bookList);

        return "redirect:/book/search";
    }
}
