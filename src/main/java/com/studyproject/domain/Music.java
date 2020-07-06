package com.studyproject.domain;

import lombok.*;

import javax.persistence.*;

@Builder @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
public class Music {

    @Id @GeneratedValue
    private Long id;

    private String searchDate;

    private String searchBy;

    //가사로 검색인지 그냥 검색인지 구분하기 위한 값
    private boolean isLyric;

    private String rank;

    private String name;

    private String img;

    private String genre;

    private String artist;

    @Lob @Basic(fetch = FetchType.LAZY)
    private String lyric;

    private String rate;

    private String detailGenre;

    private String publicationDate;

    private String publicationBy;
}
