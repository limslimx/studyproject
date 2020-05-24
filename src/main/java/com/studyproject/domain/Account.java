package com.studyproject.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
@Entity
public class Account {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String email;

    private String password;

    //이 계정이 이메일 인증이 되었는지 안되었는지 확인하기 위함
    private boolean emailVerified;

    //이메일 검증 시 사용할 토큰값
    private String emailCheckToken;

    //가입날짜
    private LocalDateTime joinedAt;

    private String bio;

    private String url;

    private String occupation;

    private String location;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    //스터디 관련 알림을 이메일로 받을것인가의 여부
    private boolean studyCreatedByEmail;

    //스터디 관련 알림을 웹으로 받을것인가의 여부
    private boolean studyCreatedByWeb;

    //스터디 등록 알림을 이메일로 받을것인가의 여부
    private boolean studyEnrollmentResultByEmail;

    //스터디 등록 알림을 웹으로 받을것인가의 여부
    private boolean studyEnrollmentResultByWeb;

    //스터디 변경 알림을 이메일로 받을것인가의 여부
    private boolean studyUpdateByEmail;

    ////스터디 변경 알림을 웹으로 받을것인가의 여부
    private boolean studyUpdateByWeb;

    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
    }
}
