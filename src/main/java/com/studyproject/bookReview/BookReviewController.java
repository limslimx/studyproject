package com.studyproject.bookReview;

import com.studyproject.account.CurrentUser;
import com.studyproject.book.BookRepository;
import com.studyproject.domain.Account;
import com.studyproject.domain.Book;
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

@Slf4j
@RequiredArgsConstructor
@Controller
public class BookReviewController {

    private final BookRepository bookRepository;
    private final BookReviewService bookReviewService;

    //독서록 폼 핸들러
    @GetMapping("/bookReview/{id}")
    public String bookReviewForm(@CurrentUser Account account, @PathVariable Long id, Model model) {
        Book bookById = bookRepository.findBookById(id);
        model.addAttribute("book", bookById);
        model.addAttribute("account", account);
        model.addAttribute("bookReviewForm", new BookReviewForm());
        return "bookReview/form";
    }

    //독서록 db에 저장 기능 핸들러
    @PostMapping("/bookReview/save/{id}")
    public String bookReviewSave(@CurrentUser Account account, @PathVariable Long id, @Valid BookReviewForm bookReviewForm, Errors errors, Model model) {
        Book bookById = bookRepository.findBookById(id);
        if (errors.hasErrors()) {
            log.info("##############fail");
            model.addAttribute("account", account);
            model.addAttribute("bookReviewForm", bookReviewForm);
            model.addAttribute("book", bookById);
            return "bookReview/form";
        }
        log.info("##############success");
        bookReviewService.createBookReview(account, id, bookReviewForm);
        return "redirect:/";
    }
}
