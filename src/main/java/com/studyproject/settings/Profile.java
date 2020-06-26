package com.studyproject.settings;

import com.studyproject.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // @PostMapping("/settings/profile")에서 @Valid Profile profile 로직 실행 시에 Profile내의 생성자를 살펴보고 생성해주는데 현재 Profile에는 account를 이용하여 생성하기 때문에 account가 없는 지금 오류가 뜰 것이다. 그러므로 기본 생성자를 하나 추가해주어야 한다.
public class Profile {

    private String bio;

    private String location;

    private String profileImage;

    public Profile(Account account) {
        this.bio = account.getBio();
        this.location = account.getLocation();
        this.profileImage = account.getProfileImage();
    }
}
