package com.studyproject.account;

import com.studyproject.domain.Account;
import com.studyproject.settings.PasswordForm;
import com.studyproject.settings.PasswordFormValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    //회원가입 시에 입력한 닉네임 또는 이메일이 db에 이미 존재하면 오류 잡아줌
    @InitBinder("signUpForm")
    public void signupInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @InitBinder("passwordForm")
    public void passwordInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new PasswordFormValidator());
    }

    //회원가입 폼 핸들러
    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
        return "account/sign-up2";
    }

    //회원가입 처리 핸들러
    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {

        //아래처럼만 작성해줘도 form에 들어갔던 데이터들과 errors을 model에 넣어 자동으로 화면에 보여준다
        if (errors.hasErrors()) {
            return "account/sign-up2";
        }

        //회원가입form에 입력한 정보를 db에 저장하고 account엔티티 안에 이메일을 인증했는지 이메일 인증 토큰을 랜덤으로 생성한 후에 회원가입 시 입력한 이메일로 회원가입 시 메일 인증 링크를 보내줌
        Account account = accountService.processNewAccount(signUpForm);

        //로그인 시에 이 사용자가 인증된 사용자임을 SecurityContextHolder에 등록해주는 로직
        accountService.login(account);

        //TODO 회원가입 처리
        return "redirect:/";
    }

    //이메일 인증 폼 핸들러
    @GetMapping("/check-email")
    public String checkEmail(@CurrentUser Account account, Model model) {
        model.addAttribute("email", account.getEmail());
        return "account/check-email";
    }

    @PostMapping("/update/password")
    public String updatePassword(@CurrentUser Account account, @Valid PasswordForm passwordForm, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("account", account);
            return "account/update-password-by-email";
        }
        accountService.updatePassword(account, passwordForm.getNewPassword());
        return "redirect:/";
    }

    @PostMapping("/delete/account/{nickname}")
    public @ResponseBody boolean deleteAccount(@PathVariable String nickname) {
        boolean validation = accountService.deleteValidate(nickname);
        if (validation) {
            accountService.deleteAccount(nickname);
        }
        return validation;
    }
}
