package com.studyproject.domain;

import lombok.*;

import javax.persistence.*;

@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
@Entity
public class Book {

    @Column(name = "book_id")
    @Id @GeneratedValue
    private Long id;

    //검색 날짜 -> 크롤링 시 같은 책인데 매일매일 크롤링 시에 최근 데이터로 가져오기 위한 구분값
    private String searchDate;

    //검색어 -> 도서 검색 기능 구현 시에 검색어를 저장함으로써 나중에 다시 똑같은 검색어로 검색할 때 더 빠르게 조회할 수 있도록 함
    private String searchBy;

    //책 이름
    private String name;

    //소제목
    private String subName;

    //책 이미지
    private String img;

    //책 저자
    private String author;

    //책 상세 카테고리
    private String detailCategory;

    //책 순위
    private String rank;

    //책 태그
    private String tag;

    //책 상세 url
    private String url;

    //책 출판일
    private String publicationDate;

}
