package com.studyproject.bookReview;

import com.studyproject.account.AccountRepository;
import com.studyproject.account.CurrentUser;
import com.studyproject.book.BookRepository;
import com.studyproject.domain.Account;
import com.studyproject.domain.Book;
import com.studyproject.domain.BookReview;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BookReviewController {

    private final BookRepository bookRepository;
    private final BookReviewService bookReviewService;
    private final AccountRepository accountRepository;
    private final BookReviewRepository bookReviewRepository;

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

    @GetMapping("/bookReview/list/{nickname}")
    public String bookReviewList(@CurrentUser Account account, @PathVariable String nickname, Model model) {
        Account byNickname = accountRepository.findByNickname(nickname);
        if (byNickname == null) {
            throw new IllegalArgumentException(nickname + "에 해당되는 사용자가 없습니다.");
        }
        List<BookReview> bookReviewList = bookReviewRepository.findByAccount(account);
        model.addAttribute("isOwner", byNickname.equals(account));
        model.addAttribute("bookReviewList", bookReviewList);
        return "bookReview/list";
    }

    @GetMapping("/bookReview/detail/{id}")
    public String bookReviewDetail(@CurrentUser Account account, @PathVariable Long bookReviewId, Model model) {
        BookReview bookReviewById = bookReviewRepository.findBookReviewById(bookReviewId);

        model.addAttribute("account", account);
        return "bookReview/detail";
    }
}
