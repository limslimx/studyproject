package com.studyproject.bookReview;

import com.studyproject.account.AccountRepository;
import com.studyproject.account.CurrentUser;
import com.studyproject.book.BookRepository;
import com.studyproject.domain.Account;
import com.studyproject.domain.Book;
import com.studyproject.domain.BookReview;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    //독서록 폼 핸들러
    @GetMapping("/bookReview/{id}")
    public String bookReviewSaveForm(@CurrentUser Account account, @PathVariable Long id, Model model) {
        Book bookById = bookRepository.findBookById(id);
        model.addAttribute("book", bookById);
        model.addAttribute("account", account);
        model.addAttribute("bookReviewForm", new BookReviewForm());
        return "bookReview/save-form";
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
            return "bookReview/save-form";
        }
        log.info("##############success");
        bookReviewService.createBookReview(account, id, bookReviewForm);
        return "redirect:/";
    }

    //독서록 리스트 화면 핸들러
    @GetMapping("/bookReview/list/{nickname}")
    public String bookReviewList(@CurrentUser Account account, @PathVariable String nickname, Model model) {
        Account byNickname = accountRepository.findByNickname(nickname);
        if (byNickname == null) {
            throw new IllegalArgumentException(nickname + "에 해당되는 사용자가 없습니다.");
        }
        List<BookReview> bookReviewList = bookReviewRepository.findByAccount(account);
        model.addAttribute("bookReviewList", bookReviewList);
        return "bookReview/list";
    }

    //독서록 상세 화면 핸들러
    @GetMapping("/bookReview/detail/{bookReviewId}")
    public String bookReviewDetail(@CurrentUser Account account, @PathVariable Long bookReviewId, Model model) {
        BookReview bookReviewById = bookReviewRepository.findBookReviewById(bookReviewId);
        Account accountByBookReview = bookReviewById.getAccount();
        model.addAttribute("account", account);
        model.addAttribute("bookReview", bookReviewById);
        model.addAttribute("isOwner", accountByBookReview.equals(account));
        return "bookReview/detail";
    }

    //독서록 수정 화면 핸들러
    @GetMapping("/bookReview/update/{bookReviewId}")
    public String bookReviewUpdateForm(@CurrentUser Account account, @PathVariable Long bookReviewId, Model model) {
        BookReview bookReviewById = bookReviewRepository.findBookReviewById(bookReviewId);
        Book bookById = bookRepository.findBookById(bookReviewById.getBook().getId());
        model.addAttribute("book", bookById);
        model.addAttribute("account", account);
        model.addAttribute("bookReviewId", bookReviewById.getId());
        model.addAttribute("bookReviewForm", modelMapper.map(bookReviewById, BookReviewForm.class));
        return "bookReview/update-form";
    }

    @PostMapping("/bookReview/update/{bookReviewId}")
    public String bookReviewUpdate(@CurrentUser Account account, @PathVariable Long bookReviewId, @Valid BookReviewForm bookReviewForm, Errors errors, Model model) {
        if (errors.hasErrors()) {
            log.info("##################fail##################");
            model.addAttribute("bookReviewId", bookReviewId);
            model.addAttribute("bookReviewForm", bookReviewForm);
            model.addAttribute("account", account);
            return "bookReview/update-form";
        }
        log.info("##################success##################");
        return null;
    }
}
