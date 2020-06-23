package com.studyproject.main;

import com.studyproject.account.CurrentUser;
import com.studyproject.bookReview.BookReviewRepository;
import com.studyproject.bookReview.BookReviewService;
import com.studyproject.domain.Account;
import com.studyproject.domain.Book;
import com.studyproject.domain.BookReview;
import com.studyproject.favorBook.FavorBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final MainService mainService;
    private final FavorBookRepository favorBookRepository;
    private final BookReviewRepository bookReviewRepository;

    @GetMapping("/")
    public String index(@CurrentUser Account account, Model model) {
        if (account != null) {
            List<Book> bookRecommendList = mainService.bookRecommend(account);
            List<String> favorBookList = favorBookRepository.findBookNameByAccountId(account.getId());
            model.addAttribute("bookRecommendList", bookRecommendList);
            model.addAttribute("favorBookList", favorBookList);
            model.addAttribute(account);
        }

        return "index";
    }

    //로그인 폼 핸들러
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/search/bookReview")
    public String searchBookReview(@PageableDefault(size = 6, page = 0, sort = "modifiedDate", direction = Sort.Direction.DESC) Pageable pageable, String keyword, Model model) {
        Page<BookReview> bookReviewPageList = bookReviewRepository.findByKeyword(keyword, pageable);
        model.addAttribute("bookReviewPageList", bookReviewPageList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortProperty", "modifiedDate");
        return "search";
    }
}
