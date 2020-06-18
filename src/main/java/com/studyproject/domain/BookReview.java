package com.studyproject.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
@Entity
public class BookReview {

    @Id @GeneratedValue
    @Column(name = "bookReview_id")
    private Long id;

    private String title;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String content;

    private boolean isOpen;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
