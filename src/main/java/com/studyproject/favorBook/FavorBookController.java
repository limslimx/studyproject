package com.studyproject.favorBook;

import com.studyproject.account.CurrentUser;
import com.studyproject.book.BookService;
import com.studyproject.domain.Account;
import com.studyproject.domain.FavorBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class FavorBookController {

    private final FavorBookRepository favorBookRepository;
    private final BookService bookService;

    @GetMapping("/favorBook/list")
    public String favorBookList(@CurrentUser Account account, Model model) {
        List<FavorBook> favorBookList = favorBookRepository.findFavorBookByAccountId(account.getId());
        model.addAttribute("account", account);
        model.addAttribute("favorBookList", favorBookList);
        return "favorBook/list";
    }

    //관심도서 추가 기능 핸들러
    @PostMapping("/favorBook/add/{bookName}")
    public @ResponseBody
    String favorBookAdd(@CurrentUser Account account, @PathVariable String bookName, Model model) {
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
