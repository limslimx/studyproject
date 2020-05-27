package com.studyproject.account;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) //이 어노테이션은 파라미터에만 붙일 수 있도록 지정함
@Retention(RetentionPolicy.RUNTIME) //이 어노테이션은 런타임까지 유지되도록 지정함
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account") //AuthenticationPrincipal이 로그인 처리를 안했다면 anonymousUser여서 null값이 들어가고 로그인을 했다면 UserAccount객체의 account 필드값을 가져옴
public @interface CurrentUser {
}
