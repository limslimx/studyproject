package com.studyproject.bookReview;

import com.studyproject.domain.Account;
import com.studyproject.domain.BookReview;
import com.studyproject.main.BookRecommendDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(readOnly = true)
public interface BookReviewRepository extends JpaRepository<BookReview, Long>, BookReviewRepositoryExtension {

    List<BookReview> findByAccount(Account account);

    BookReview findBookReviewById(Long id);

    @Modifying
    @Query("update BookReview br set br.account.id=null where br.account.id=:accountId")
    void updateToNull(@Param("accountId") Long accountId);

    @Query("select new com.studyproject.main.BookRecommendDto(b.detailCategory, count(br)) from BookReview br join Book b on br.book.id=b.id and br.account=:account group by br.book.detailCategory")
    List<BookRecommendDto> findBookDetailCategoryAndCount(Account account);

    void deleteByAccount(Account account);
}
