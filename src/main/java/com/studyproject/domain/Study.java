package com.studyproject.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
@Entity
public class Study {

    @Id @GeneratedValue
    private Long id;

    //스터디 관리자
    @ManyToMany
    private Set<Account> managers = new HashSet<>();

    //스터디 회원
    @ManyToMany
    private Set<Account> members = new HashSet<>();

    //url 경로
    @Column(unique = true)
    private String path;

    //스터디 이름
    private String title;

    //스터디 짧은 소개
    private String shortDescription;

    //스터디 상세 소개
    @Lob @Basic(fetch = FetchType.EAGER)
    private String fullDescription;

    //스터디 이미지
    @Lob @Basic(fetch = FetchType.EAGER)
    private String image;

    //스터디 공개 시간
    private LocalDateTime publishedDateTime;

    //스터디 종료 시간
    private LocalDateTime closedDateTime;

    //스터디 인원모집 시간
    private LocalDateTime recruitingUpdatedDateTime;

    //스터디 인원모집 중인지의 여부
    private boolean recruiting;

    //스터디 종료 여부
    private boolean closed;

    public void addManager(Account account) {
        this.managers.add(account);
    }
}
