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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BookController {

    private final BookService bookService;
    private final BookRepository bookRepository;

    //책 검색 폼 핸들러
    @GetMapping("/book/search")
    public String bookSearchForm(@CurrentUser Account account, Model model) {
        model.addAttribute("account", account);
        model.addAttribute("bookSearchForm", new BookSearchForm());
        return "book/search";
    }

    //책 검색하여 크롤링 후 저장하는 기능 핸들러
    @PostMapping("/book/search/save")
    public String bookSearch(@CurrentUser Account account, @Valid BookSearchForm bookSearchForm, Errors errors, Model model, RedirectAttributes attributes) throws Exception {
        if (errors.hasErrors()) {
            model.addAttribute("account", account);
            model.addAttribute("bookSearchForm", bookSearchForm);
            return "/book/search";
        }
        List<Book> bookList = bookService.getBookInfo(bookSearchForm.getSearchBy());
        attributes.addFlashAttribute("bookList", bookList);

        return "redirect:/book/search";
    }

    //관심도서 등록 기능 핸들러
    @PostMapping("/favorBook/{bookId}")
    public String favorBook(@CurrentUser Account account, @PathVariable Long bookId, Model model) {
        boolean validateBookById = bookService.validateBookById(bookId);
        if (validateBookById) {
            boolean isFavorBookExists = bookService.validateFavorBookById(bookId, account);
            if (isFavorBookExists) {
                bookService.addFavorBook(account, bookId);

            } else {
                bookService.deleteFavorBook(account, bookId);
                model.addAttribute("isFavorBookExists", false);
            }
        } else {
            throw new IllegalArgumentException("해당 책이 존재하지 않습니다.");
        }
        return "book/search";
    }

    //책 카테고리별 분류 화면 핸들러
    @GetMapping("/book/category")
    public String bookCategory(@CurrentUser Account account, Model model) {
        model.addAttribute("account", account);
        return "book/category-form";
    }

    //문학 베스트셀러 리스트 화면 핸들러
    @GetMapping("/book/category/literature")
    public String bookBestCellarByLiterature(@CurrentUser Account account, Model model) throws Exception {
        String category = "문학";
        List<Book> bookBestCellarList = bookService.getCategoryBookBestCellarInfo(category);
        model.addAttribute("account", account);
        model.addAttribute("bookBestCellarList", bookBestCellarList);
        return "book/category/literature-list";
    }

    //인문 베스트셀러 리스트 화면 핸들러
    @GetMapping("/book/category/humanities")
    public String bookBestCellarByHumanities(@CurrentUser Account account, Model model) throws Exception {
        String category = "인문";
        List<Book> bookBestCellarList = bookService.getCategoryBookBestCellarInfo(category);
        model.addAttribute("account", account);
        model.addAttribute("bookBestCellarList", bookBestCellarList);
        return "book/category/humanities-list";
    }

    //실용 베스트셀러 리스트 화면 핸들러
    @GetMapping("/book/category/real")
    public String bookBestCellarByReal(@CurrentUser Account account, Model model) throws Exception {
        String category = "실용";
        List<Book> bookBestCellarList = bookService.getCategoryBookBestCellarInfo(category);
        model.addAttribute("account", account);
        model.addAttribute("bookBestCellarList", bookBestCellarList);
        return "book/category/real-list";
    }

    //자기계발 베스트셀러 리스트 화면 핸들러
    @GetMapping("/book/category/selfDevelopment")
    public String bookBestCellarBySelfDevelopment(@CurrentUser Account account, Model model) throws Exception {
        String category = "자기계발";
        List<Book> bookBestCellarList = bookService.getCategoryBookBestCellarInfo(category);
        model.addAttribute("account", account);
        model.addAttribute("bookBestCellarList", bookBestCellarList);
        return "book/category/selfDevelopment-list";
    }
}
