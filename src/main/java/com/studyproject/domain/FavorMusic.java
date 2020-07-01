package com.studyproject.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FavorMusic {

    @Id @GeneratedValue
    @Column(name = "favorMusic_id")
    private Long id;

    private String musicName;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "music_id")
    private Music music;
}
