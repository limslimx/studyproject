package com.studyproject.domain;

import com.studyproject.account.UserAccount;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

//StudyRepository에서 findByPath 메서드 실행 시에 아래서 지정한 managers, members 즉, 필요한 정보들을 한번에 조인해서 다 가져옴
@NamedEntityGraph(name = "Study.withAll", attributeNodes = {
        @NamedAttributeNode("managers"),
        @NamedAttributeNode("members")})
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

    //스터디 공개 여부
    private boolean published;

    //스터디 인원모집 중인지의 여부
    private boolean recruiting;

    //스터디 종료 여부
    private boolean closed;

    //스터디의 관리자 등록
    public void addManager(Account account) {
        this.managers.add(account);
    }

    //가입가능 여부 확인
    public boolean isJoinable(UserAccount userAccount) {
        Account account = userAccount.getAccount();
        return this.isPublished() && this.isRecruiting() && !this.members.contains(account) && !this.managers.contains(account);
    }

    //스터디 맴버인지 확인
    public boolean isMember(UserAccount userAccount) {
        return this.members.contains(userAccount.getAccount());
    }

    //스터디 관리자인지 확인
    public boolean isManager(UserAccount userAccount) {
        return this.managers.contains(userAccount.getAccount());
    }
}
