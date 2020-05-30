package com.studyproject.settings;

import com.studyproject.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class NicknameFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return NicknameForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NicknameForm nicknameForm = (NicknameForm) target;
        if (accountRepository.findByNickname(nicknameForm.getNickname()) != null) {
            errors.rejectValue("nickname", "wrong.nickname", "사용중인 닉네임입니다.");
        }
    }
}
