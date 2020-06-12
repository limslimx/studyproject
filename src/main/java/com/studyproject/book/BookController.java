package com.studyproject.book;

import com.studyproject.account.CurrentUser;
import com.studyproject.book.form.BookSearchForm;
import com.studyproject.domain.Account;
import com.studyproject.domain.Book;
import com.studyproject.favorBook.FavorBookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BookController {

    private final BookService bookService;
    private final BookRepository bookRepository;
    private final FavorBookRepository favorBookRepository;

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
        List<Book> bookList = bookService.getBookInfo(bookSearchForm.getSearchBy(), account);
        List<String> favorBookList = favorBookRepository.findByAccountId(account.getId());

        attributes.addFlashAttribute("bookList", bookList);
        attributes.addFlashAttribute("favorBookList", favorBookList);

        return "redirect:/book/search";
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
        List<String> favorBookList = favorBookRepository.findByAccountId(account.getId());
        for (int i = 0; i < favorBookList.size(); i++) {
            log.info(favorBookList.get(i));
        }
        model.addAttribute("account", account);
        model.addAttribute("bookBestCellarList", bookBestCellarList);
        model.addAttribute("favorBookList", favorBookList);
        return "book/category/literature-list";
    }

    //인문 베스트셀러 리스트 화면 핸들러
    @GetMapping("/book/category/humanities")
    public String bookBestCellarByHumanities(@CurrentUser Account account, Model model) throws Exception {
        String category = "인문";
        List<Book> bookBestCellarList = bookService.getCategoryBookBestCellarInfo(category);
        List<String> favorBookList = favorBookRepository.findByAccountId(account.getId());
        model.addAttribute("account", account);
        model.addAttribute("bookBestCellarList", bookBestCellarList);
        model.addAttribute("favorBookList", favorBookList);
        return "book/category/humanities-list";
    }

    //실용 베스트셀러 리스트 화면 핸들러
    @GetMapping("/book/category/real")
    public String bookBestCellarByReal(@CurrentUser Account account, Model model) throws Exception {
        String category = "실용";
        List<Book> bookBestCellarList = bookService.getCategoryBookBestCellarInfo(category);
        List<String> favorBookList = favorBookRepository.findByAccountId(account.getId());
        model.addAttribute("account", account);
        model.addAttribute("bookBestCellarList", bookBestCellarList);
        model.addAttribute("favorBookList", favorBookList);
        return "book/category/real-list";
    }

    //자기계발 베스트셀러 리스트 화면 핸들러
    @GetMapping("/book/category/selfDevelopment")
    public String bookBestCellarBySelfDevelopment(@CurrentUser Account account, Model model) throws Exception {
        String category = "자기계발";
        List<Book> bookBestCellarList = bookService.getCategoryBookBestCellarInfo(category);
        List<String> favorBookList = favorBookRepository.findByAccountId(account.getId());
        model.addAttribute("account", account);
        model.addAttribute("bookBestCellarList", bookBestCellarList);
        model.addAttribute("favorBookList", favorBookList);
        return "book/category/selfDevelopment-list";
    }

    //관심도서 추가 기능 핸들러
    @PostMapping("/favorBook/add/{bookName}")
    public @ResponseBody String favorBookAdd(@CurrentUser Account account, @PathVariable String bookName, Model model) {
        bookService.addFavorBook(account, bookName);
        return bookName;
    }

    //관심도서 삭제 기능 핸들러
    @PostMapping("/favorBook/delete/{bookName}")
    public @ResponseBody String favorBookDelete(@CurrentUser Account account, @PathVariable String bookName, Model model) {
        bookService.deleteFavorBook(account, bookName);
        return bookName;
    }
}
